package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying aggregate measures.
 */
@Service
public class AggregateMeasureRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain AggregateMeasureRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public AggregateMeasureRepository(GraphDbConnection connection) {
        super(connection, "repo_aggmeasure", "aggregate measures");
    }

}
