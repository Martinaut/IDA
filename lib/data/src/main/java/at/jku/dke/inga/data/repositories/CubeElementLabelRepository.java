package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.CubeElementLabel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for querying and manipulating cube element labels.
 */
public interface CubeElementLabelRepository extends CrudRepository<CubeElementLabel, Integer> {
    /**
     * Returns all labels for the specified cube element.
     *
     * @param cubeElementUri The URI of the cube element.
     * @return List with found labels of the cube element.
     */
    List<CubeElementLabel> findByCubeElementUri(String cubeElementUri);

    /**
     * Returns the label for the specified cube element and language.
     *
     * @param cubeElementUri The URI of the cube element.
     * @param lang           The requested language.
     * @return The label (and description) of the cube element in the requested language.
     */
    Optional<CubeElementLabel> findByCubeElementUriAndLang(String cubeElementUri, String lang);

    /**
     * Returns all labels for the specified language.
     *
     * @param lang The requested language.
     * @return List with found labels of the language
     */
    List<CubeElementLabel> findByLang(String lang);
}
