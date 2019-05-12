package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying aggregate measure predicates.
 */
@Service
public class AggregateMeasurePredicateRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain AggregateMeasurePredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public AggregateMeasurePredicateRepository(GraphDbConnection connection) {
        super(connection, "repo_aggmeasurepred", "aggregate measure predicates");
    }

}
