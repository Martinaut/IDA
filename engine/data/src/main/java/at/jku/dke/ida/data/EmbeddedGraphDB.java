package at.jku.dke.ida.data;

import at.jku.dke.ida.data.configuration.GraphDbEmbeddedConfig;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigException;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Creates an embedded GraphDB database.
 * <p>
 * Based on http://graphdb.ontotext.com/free/devhub/embeddedgraphdb.html
 */
class EmbeddedGraphDB implements Closeable {

    private static final String REPO_ID = "ida-embedded-database";
    private static final Logger LOGGER = LogManager.getLogger(EmbeddedGraphDB.class);

    private final GraphDbEmbeddedConfig config;
    private final LocalRepositoryManager repositoryManager;
    private final Repository repository;
    private boolean createdNow;

    /**
     * Instantiates a new instance of class {@linkplain EmbeddedGraphDB}.
     *
     * @param config The embedded GraphDB configuration.
     * @throws RepositoryConfigException If no repository could be created due to invalid or incomplete configuration data.
     */
    public EmbeddedGraphDB(GraphDbEmbeddedConfig config) {
        this.config = config;
        this.createdNow = false;

        this.repositoryManager = new LocalRepositoryManager(new File(config.getDirectory()));
        this.repositoryManager.initialize();

        this.repository = createRepository();
        if (this.createdNow)
            loadData();
    }

    private Repository createRepository() {
        if (!repositoryManager.hasRepositoryConfig(REPO_ID)) {
            LOGGER.info("Creating a new embedded repository {}.", REPO_ID);

            // Instantiate a repository graph model
            TreeModel graph = new TreeModel();

            // read repository configuration file
            try {
                InputStream config = EmbeddedGraphDB.class.getResourceAsStream("/repo-defaults.ttl");
                RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
                rdfParser.setRDFHandler(new StatementCollector(graph));
                rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
                config.close();
            } catch (IOException ex) {
                LOGGER.fatal("Could not load repository configuration file repo-defaults.ttl from resources.", ex);
                throw new RepositoryConfigException("The repository configuration could not be loaded or is invalid.", ex);
            }

            // Retrieve the repository node as a resource
            Resource repositoryNode = Models.subject(graph.filter(null, RDF.TYPE, RepositoryConfigSchema.REPOSITORY)).orElse(null);

            // Create a repository configuration object and add it to the repositoryManager
            RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
            repositoryManager.addRepositoryConfig(repositoryConfig);

            createdNow = true;
        }

        // Get the repository from repository manager, note the repository id set in configuration .ttl file
        return repositoryManager.getRepository(REPO_ID);
    }

    // region --- LOAD ---
    private void loadData() {
        try (RepositoryConnection connection = getConnection()) {
            // Data and wordnet
            try {
                connection.begin();
                loadDataFiles(connection);
                connection.commit();

                connection.begin();
                loadWordNet(connection);
                connection.commit();
            } catch (IOException ex) {
                connection.rollback();
                LOGGER.error("An error occurred while loading data and wordnet.", ex);
            }

            // Materialization and Indizes
            try {
                loadIndizes(connection);
                loadMaterialization(connection);
            } catch (IOException ex) {
                LOGGER.error("An error occurred while loading materialization and indizes.", ex);
            }
        }
    }

    private void loadDataFiles(RepositoryConnection connection) throws IOException {
        LOGGER.info("Loading data files");
        for (Path path : Files.walk(Paths.get(config.getDataDirectory())).collect(Collectors.toList())) {
            File file = path.toFile();
            if (file.isFile())
                connection.add(path.toFile(), "urn:base", RDFFormat.TURTLE);
        }
    }

    private void loadWordNet(RepositoryConnection connection) throws IOException {
        LOGGER.info("Loading wordnet files");
        Resource graph = SimpleValueFactory.getInstance().createIRI("http://dke.jku.at/ida/similarity#wordnet");
        for (String file : config.getWordnetFiles()) {
            connection.add(new File(file), "urn:base", RDFFormat.NTRIPLES, graph);
        }
    }

    private void loadMaterialization(RepositoryConnection connection) throws IOException {
        LOGGER.info("Loading materialization files");
        for (Path path : Files.walk(Paths.get(config.getMaterializationDirectory())).collect(Collectors.toList())) {
            File file = path.toFile();
            if (file.isFile()) {
                String queryString = IOUtils.toString(path.toUri(), StandardCharsets.UTF_8);
                Update query = connection.prepareUpdate(queryString);
                query.execute();
            }
        }
    }

    private void loadIndizes(RepositoryConnection connection) throws IOException {
        LOGGER.info("Loading index files");
        for (Path path : Files.walk(Paths.get(config.getIndexDirectory())).collect(Collectors.toList())) {
            File file = path.toFile();
            if (file.isFile()) {
                String queryString = IOUtils.toString(path.toUri(), StandardCharsets.UTF_8);
                Update query = connection.prepareUpdate(queryString);
                query.execute();
            }
        }
    }
    // endregion

    /**
     * Opens a connection that can be used for querying and updating the contents of the repository.
     * Created connections need to be closed to make sure that any resources they keep hold of are
     * released. The best way to do this is to use a try-finally-block.
     *
     * @return A connection that allows operations on the embedded repository.
     * @throws RepositoryException If something went wrong during the creation of the Connection.
     */
    public RepositoryConnection getConnection() {
        return repository.getConnection();
    }

    @Override
    public void close() throws IOException {
        repository.shutDown();
        repositoryManager.shutDown();
    }
}
