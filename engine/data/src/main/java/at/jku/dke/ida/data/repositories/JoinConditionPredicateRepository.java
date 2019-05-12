package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying join condition predicates.
 */
@Service
public class JoinConditionPredicateRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain JoinConditionPredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public JoinConditionPredicateRepository(GraphDbConnection connection) {
        super(connection, "repo_joinpred", "join condition predicates");
    }

}
