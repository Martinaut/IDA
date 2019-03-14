package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.configuration.GraphDbConfig;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A service class that provides a graph db connection.
 */
@Service
public class GraphDbHelper {

    private static final Logger LOGGER = LogManager.getLogger(GraphDbHelper.class);
    private GraphDbConfig config;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbHelper}.
     *
     * @param config the connection configuration
     */
    @Autowired
    public GraphDbHelper(GraphDbConfig config) {
        this.config = config;
    }

    // region --- Query ---

    /**
     * Executes the query and returns the result.
     *
     * @param queryFile              the query file
     * @param queryStringManipulator the query string manipulator to manipulate the query text before executing
     * @return the query result
     * @throws QueryException If an error occurred while reading the query file or executing the query.
     */
    public List<BindingSet> getQueryResult(String queryFile, Function<String, String> queryStringManipulator) throws QueryException {
        try (var conn = getConnection()) {
            String queryString = IOUtils.toString(GraphDbHelper.class.getResourceAsStream(queryFile), StandardCharsets.UTF_8);
            queryString = queryStringManipulator.apply(queryString);
            TupleQuery query = conn.prepareTupleQuery(queryString);
            try (var result = query.evaluate()) {
                List<BindingSet> list = new ArrayList<>();

                while (result.hasNext()) {
                    list.add(result.next());
                }

                return list;
            }
        } catch (IOException ex) {
            LOGGER.error("Could not load the query file.", ex);
            throw new QueryException("Could not load query file.", ex);
        } catch (QueryEvaluationException ex) {
            LOGGER.error("An error occurred while executing the query " + queryFile, ex);
            throw new QueryException("An error occurred while executing the query " + queryFile, ex);
        }
    }
    // endregion

    // region --- Connection ---

    /**
     * Gets a new connection to the GraphDB repository.
     *
     * @return the GraphDB connection
     */
    public RepositoryConnection getConnection() {
        return config.getEmbedded() == null ?
                getHttpConnection() :
                getEmbeddedConnection();
    }

    private RepositoryConnection getHttpConnection() {
        LOGGER.info("Creating a new HTTP-connection.");
        Repository repo = new HTTPRepository(config.getHttp().getServerUrl(), config.getHttp().getRepositoryId());
        repo.initialize();
        return repo.getConnection();
    }

    private RepositoryConnection getEmbeddedConnection() {
        LOGGER.info("Creating a new embedded connection.");

        //TODO: check if works and if this is true when already created
        // Instantiate a local repository manager and initialize it
        RepositoryManager repositoryManager = new LocalRepositoryManager(new File(config.getEmbedded().getDirectory()));
        repositoryManager.initialize();

        // Instantiate a repository graph model
        TreeModel graph = new TreeModel();

        // Read repository configuration file
        try {
            InputStream config = GraphDbHelper.class.getResourceAsStream("/repo-defaults.ttl");
            RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
            rdfParser.setRDFHandler(new StatementCollector(graph));
            rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
            config.close();
        } catch (IOException e) {
            return null;
        }

        // Retrieve the repository node as a resource
        Resource repositoryNode = graph.filter(null, RDF.TYPE, RepositoryConfigSchema.REPOSITORY).subjects().stream().findFirst().orElse(null);
        if (repositoryNode == null)
            return null;

        // Create a repository configuration object and add it to the repositoryManager
        RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
        repositoryManager.addRepositoryConfig(repositoryConfig);

        // Get the repository from repository manager, note the repository id set in configuration .ttl file
        Repository repository = repositoryManager.getRepository("inga-repo");

        // Open a connection to this repository
        return repository.getConnection();
    }
    // endregion
}
