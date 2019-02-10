package at.jku.dke.inga.data.models;

import javax.persistence.*;
import java.util.StringJoiner;

/**
 * The type Cube element.
 */
@Entity(name = "cube_elements")
public class CubeElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 255)
    private String cubeUri;

    @Column(nullable = false, length = 255)
    private String uri;

    @Column(nullable = false, length = 255)
    private String typeUri;

    /**
     * Instantiates a new instance of class {@linkplain CubeElement}.
     */
    CubeElement() {
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeElement}.
     *
     * @param cubeUri The cube this element belongs to.
     * @param uri     The URI of the element.
     * @param typeUri The URI of the element's type.
     */
    public CubeElement(String cubeUri, String uri, String typeUri) {
        this.uri = uri;
        this.typeUri = typeUri;
        this.cubeUri = cubeUri;
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeElement}.
     *
     * @param cube    The cube this element belongs to.
     * @param uri     The URI of the element.
     * @param typeUri The URI of the element's type.
     */
    public CubeElement(Cube cube, String uri, String typeUri) {
        this(cube.getUri(), uri, typeUri);
    }

    /**
     * Gets the ID.
     *
     * @return The unique id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
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
     * Gets the type uri.
     *
     * @return the type uri
     */
    public String getTypeUri() {
        return typeUri;
    }

    /**
     * Sets the type uri.
     *
     * @param typeUri the type uri
     */
    public void setTypeUri(String typeUri) {
        this.typeUri = typeUri;
    }

    /**
     * Gets the cube.
     *
     * @return the cube
     */
    public String getCubeUri() {
        return cubeUri;
    }

    /**
     * Sets the cube.
     *
     * @param cube the cube
     */
    public void setCubeUri(String cube) {
        this.cubeUri = cube;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", CubeElement.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cubeUri='" + cubeUri + "'")
                .add("uri='" + uri + "'")
                .add("typeUri='" + typeUri + "'")
                .toString();
    }
}
