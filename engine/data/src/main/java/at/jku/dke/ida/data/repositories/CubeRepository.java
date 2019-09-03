package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.repositories.base.ElementRepository;
import at.jku.dke.ida.shared.IRIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Repository for querying cubes.
 */
@Service
public class CubeRepository extends ElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain CubeRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public CubeRepository(GraphDbConnection connection) {
        super(connection, IRIConstants.TYPE_CUBE);
    }

}
