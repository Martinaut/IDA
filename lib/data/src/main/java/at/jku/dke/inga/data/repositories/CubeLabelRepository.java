package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.CubeLabel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for querying and manipulating cube labels.
 */
public interface CubeLabelRepository extends CrudRepository<CubeLabel, Integer> {
    /**
     * Returns all labels for the specified cube.
     *
     * @param cubeUri The URI of the cube.
     * @return List with found labels of the cube.
     */
    List<CubeLabel> findByCubeUri(String cubeUri);

    /**
     * Returns the label for the specified cube and language.
     *
     * @param cubeUri The URI of the cube.
     * @param lang    The requested language.
     * @return The label (and description) of the cube in the requested language.
     */
    Optional<CubeLabel> findByCubeUriAndLang(String cubeUri, String lang);

    /**
     * Returns all labels for the specified language.
     *
     * @param lang The requested language.
     * @return List with found labels of the language
     */
    List<CubeLabel> findByLang(String lang);
}
