package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Cube;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for querying and manipulating cubes.
 */
public interface CubeRepository extends CrudRepository<Cube, String> {
}
