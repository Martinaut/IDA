package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import at.jku.dke.ida.shared.IRIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying comparative measure predicates.
 */
@Service
public class ComparativeMeasurePredicateRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasurePredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public ComparativeMeasurePredicateRepository(GraphDbConnection connection) {
        super(connection, IRIConstants.TYPE_COMPARATIVE_MEASURE_PREDICATE, "repo_compmeasurepred", "comparative measure predicate");
    }

}
