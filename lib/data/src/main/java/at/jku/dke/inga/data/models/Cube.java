package at.jku.dke.inga.data.models;

import javax.persistence.*;
import java.util.StringJoiner;

/**
 * Represents the database-table containing Cubes.
 */
@Entity(name = "cubes")
public class Cube {

    @Id
    @Column(length = 255)
    private String uri;

    /**
     * Instantiates a new instance of class {@linkplain Cube}.
     */
    Cube() {
    }

    /**
     * Instantiates a new instance of class {@linkplain Cube}.
     *
     * @param uri   The uri of the cube.
     */
    public Cube(String uri) {
        this.uri = uri;
    }

    /**
     * Gets the uri.
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the uri.
     *
     * @param uri the uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Cube.class.getSimpleName() + "[", "]")
                .add("uri='" + uri + "'")
                .toString();
    }
}