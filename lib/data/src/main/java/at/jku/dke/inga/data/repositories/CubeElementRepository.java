package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.CubeElement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
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

    /**
     * Returns all elements for the specified cube with the specified type except those specified in the uri-list.
     *
     * @param cubeUri The URI of the cube.
     * @param typeUri The URI of the requested type.
     * @param uris    The elements to exclude.
     * @return List of cube elements of the specified type for the specified cube.
     */
    List<CubeElement> findByCubeUriAndTypeUriAndUriNotIn(String cubeUri, String typeUri, Collection<String> uris);

    /**
     * Returns all elements for the specified cube with the specified type except those specified in the uri-list.
     *
     * @param cubeUri The URI of the cube.
     * @param typeUri The URI of the requested type.
     * @param uris    The elements to exclude.
     * @return List of cube elements of the specified type for the specified cube.
     */
    @Query("SELECT ce.uri FROM cube_elements ce WHERE ce.cubeUri = :cubeUri AND ce.typeUri = :typeUri AND ce.uri NOT IN :uris")
    List<String> findByCubeUriAndTypeUriAndUriNotInAndSelectUri(@Param("cubeUri") String cubeUri, @Param("typeUri") String typeUri, @Param("uris") Collection<String> uris);
}