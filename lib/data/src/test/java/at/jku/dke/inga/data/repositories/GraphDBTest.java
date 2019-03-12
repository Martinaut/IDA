package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.shared.SharedSpringConfiguration;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.GraphUtil;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest(classes = {SharedSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class GraphDBTest {

    @Test
    void testHttpConnection() {
        var queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX qbx: <http://dke.jku.at/inga/cubes#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?subject ?label\n" +
                "WHERE {\n" +
                "\t?subject rdf:type qbx:BaseMeasure;\n" +
                "    \t\t rdfs:label ?label .\n" +
                "}";

        var conn = getRepoConnection();
        var query = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        TupleQueryResult result = null;
        try {
            result = query.evaluate();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();

                Value uri = bindingSet.getValue("subject");
                SimpleLiteral lbl = (SimpleLiteral) bindingSet.getValue("label");
                System.out.println(uri.stringValue() + ": " + lbl.stringValue());
            }
        } catch (QueryEvaluationException ex) {
            throw ex;
        } finally {
            if (result != null) {
                result.close();
            }
            conn.close();
        }
    }

    @Test
    void testLocalRepo() throws IOException {
        // Instantiate a local repository manager and initialize it
        RepositoryManager repoManager = new LocalRepositoryManager(new File("."));
        repoManager.initialize();

        // Instantiate a repository graph model
        TreeModel graph = new TreeModel();

        // Read repository configuration file
        InputStream config = GraphDBTest.class.getResourceAsStream("/repo-defaults.ttl");
        RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
        rdfParser.setRDFHandler(new StatementCollector(graph));
        rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
        config.close();

        // Retrieve the repository node as a resource
        Resource repositoryNode = GraphUtil.getUniqueSubject(graph, RDF.TYPE, RepositoryConfigSchema.REPOSITORY);

        // Create a repository configuration object and add it to the repositoryManager
        RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
        repoManager.addRepositoryConfig(repositoryConfig);

        // Get the repository from repository manager, note the repository id set in configuration .ttl file
        Repository repository = repoManager.getRepository("graphdb-repo");

        // Open a connection to this repository
        RepositoryConnection repositoryConnection = repository.getConnection();

        // ... use the repository

        // Shutdown connection, repository and manager
        repositoryConnection.close();
        repository.shutDown();
        repoManager.shutDown();
    }

    private static RepositoryConnection getRepoConnection() {
        Repository repo = new HTTPRepository("http://localhost:7200/", "drugs");
        repo.initialize();
        return repo.getConnection();
    }
}
