package at.jku.dke.ida.data.models.labels;

import java.util.StringJoiner;

/**
 * Represents an element with a label and description of a specific language in a cube.
 */
public class CubeLabel extends Label {

    private String cubeUri;
    private String typeUri;

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     */
    CubeLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     *
     * @param uri   The uri this label belongs to.
     * @param lang  The language of this label.
     * @param label The label of the element.
     */
    public CubeLabel(String uri, String lang, String label) {
        super(uri, lang, label);
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     *
     * @param uri         The uri this label belongs to.
     * @param lang        The language of this label and description.
     * @param label       The label of the element.
     * @param description The description of the element.
     */
    public CubeLabel(String uri, String lang, String label, String description) {
        super(uri, lang, label, description);
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     *
     * @param lang    The language of this label.
     * @param cubeUri The uri of the cube.
     * @param typeUri The uri if the type of the element.
     * @param uri     The uri this label belongs to.
     * @param label   The label of the element.
     */
    public CubeLabel(String lang, String cubeUri, String typeUri, String uri, String label) {
        super(uri, lang, label);
        this.cubeUri = cubeUri;
        this.typeUri = typeUri;
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     *
     * @param lang        The language of this label and description.
     * @param cubeUri     The uri of the cube.
     * @param typeUri The uri if the type of the element.
     * @param uri         The uri this label belongs to.
     * @param label       The label of the element.
     * @param description The description of the element.
     */
    public CubeLabel(String lang, String cubeUri, String typeUri, String uri, String label, String description) {
        super(uri, lang, label, description);
        this.cubeUri = cubeUri;
        this.typeUri = typeUri;
    }

    /**
     * Gets the cube uri.
     *
     * @return the cube uri
     */
    public String getCubeUri() {
        return cubeUri;
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
     * Sets the cube uri.
     *
     * @param cubeUri the cube uri
     */
    public void setCubeUri(String cubeUri) {
        this.cubeUri = cubeUri;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CubeLabel.class.getSimpleName() + "[", "]")
                .add("lang='" + getLang() + "'")
                .add("cubeUri='" + cubeUri + "'")
                .add("typeUri='" + typeUri + "'")
                .add("uri='" + getUri() + "'")
                .add("label='" + getLabel() + "'")
                .add("description='" + getDescription() + "'")
                .toString();
    }
}
