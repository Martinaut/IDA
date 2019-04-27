package at.jku.dke.ida.data.configuration;

import at.jku.dke.ida.data.QueryException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A service class that provides GraphDB connections.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class GraphDbConnection {

    private static final Logger LOGGER = LogManager.getLogger(GraphDbConnection.class);
    private final GraphDbConfig config;

    /**
     * Instantiates a new instance of class {@linkplain GraphDbConnection}.
     *
     * @param config The GraphDB connection configuration.
     */
    @Autowired
    public GraphDbConnection(GraphDbConfig config) {
        this.config = config;
    }

    /**
     * Creates a new connection to the GraphDB repository.
     *
     * @return A new GraphDB connection.
     */
    public RepositoryConnection createConnection() {
        LOGGER.debug("Creating a new HTTP-connection to repository {} at {}.", config.getRepositoryId(), config.getServerUrl());

        Repository repo = new HTTPRepository(config.getServerUrl(), config.getRepositoryId());
        repo.initialize();
        return repo.getConnection();
    }

    /**
     * Executes the query and returns the result.
     *
     * @param queryFile              the query file
     * @param queryStringManipulator the query string manipulator to manipulate the query text before executing
     * @return the query result
     * @throws QueryException If an error occurred while reading the query file or executing the query.
     */
    public List<BindingSet> getQueryResult(String queryFile, Function<String, String> queryStringManipulator) throws QueryException {
        LOGGER.debug("Executing query file {}.", queryFile);
        try (var conn = createConnection()) {
            String queryString = IOUtils.toString(GraphDbConnection.class.getResourceAsStream(queryFile), StandardCharsets.UTF_8);
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
            LOGGER.error("Could not load the query file " + queryFile, ex);
            throw new QueryException("Could not load query file " + queryFile, ex);
        } catch (QueryEvaluationException ex) {
            LOGGER.error("An error occurred while executing the query " + queryFile, ex);
            throw new QueryException("An error occurred while executing the query " + queryFile, ex);
        }
    }
}
