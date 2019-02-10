package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.CubeElement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for querying and manipulating cube elements.
 */
public interface CubeElementRepository extends CrudRepository<CubeElement, Integer> {
    /**
     * Returns all elements for the specified cube.
     *
     * @param cubeUri The URI of the cube.
     * @return List with found elements of the cube.
     */
    List<CubeElement> findByCubeUri(String cubeUri);

    /**
     * Returns all elements for the specified cube with the specified type.
     *
     * @param cubeUri The URI of the cube.
     * @param typeUri The URI of the requested type.
     * @return List of cube elements of the specified type for the specified cube.
     */
    List<CubeElement> findByCubeUriAndTypeUri(String cubeUri, String typeUri);
}
