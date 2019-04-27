package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.models.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Repository for querying cubes.
 */
@Service
public class CubeRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain CubeRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public CubeRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns all cubes.
     *
     * @return Set with all cube IRIs
     * @throws QueryException If an exception occurred while executing the query .
     */
    protected Set<String> getAll() throws QueryException {
        logger.debug("Querying all cubes.");
        return getAll("/repo_base/getAll.sparql", "http://dke.jku.at/inga/cubes#BaseCube");
    }

    /**
     * Returns the labels of all available cubes.
     *
     * @param lang The requested language.
     * @return List with found cube labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLang(String lang) throws QueryException {
        logger.debug("Querying all cube labels in language {}.", lang);
        return getLabelsByLang("/repo_cube/getLabelsByLang.sparql", lang);
    }

}
