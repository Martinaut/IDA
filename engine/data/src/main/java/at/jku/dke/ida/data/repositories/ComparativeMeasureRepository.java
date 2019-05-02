package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.SimpleCubeElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying comparative measures.
 */
@Service
public class ComparativeMeasureRepository extends SimpleCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public ComparativeMeasureRepository(GraphDbConnection connection) {
        super(connection, "repo_compmeasure", "comparative measures");
    }

}
