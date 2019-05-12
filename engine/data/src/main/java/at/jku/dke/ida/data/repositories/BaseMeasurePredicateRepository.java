package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying base measure predicates.
 */
@Service
public class BaseMeasurePredicateRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain BaseMeasurePredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public BaseMeasurePredicateRepository(GraphDbConnection connection) {
        super(connection, "repo_basemeasurepred", "base measure predicates");
    }

}
