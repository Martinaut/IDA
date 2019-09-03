package at.jku.dke.ida.data.models.labels;

import java.util.StringJoiner;

/**
 * Represents a comparative element with a label and description of a specific language.
 */
public class ComparativeLabel extends CubeLabel {

    private String part;

    /**
     * Instantiates a new instance of class {@linkplain ComparativeLabel}.
     */
    ComparativeLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeLabel}.
     *
     * @param lang  The language of this label.
     * @param part  The part.
     * @param uri   The uri this label belongs to.
     * @param label The label of the element.
     */
    public ComparativeLabel(String lang, String part, String uri, String label) {
        super(uri, lang, label);
        this.part = part;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeLabel}.
     *
     * @param lang        The language of this label and description.
     * @param part        The part.
     * @param uri         The uri this label belongs to.
     * @param label       The label of the element.
     * @param description The description of the element.
     */
    public ComparativeLabel(String lang, String part, String uri, String label, String description) {
        super(uri, lang, label, description);
        this.part = part;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeLabel}.
     *
     * @param lang    The language of this label.
     * @param cubeUri The uri of the cube.
     * @param typeUri The uri if the type of the element.
     * @param part    The part.
     * @param uri     The uri this label belongs to.
     * @param label   The label of the element.
     */
    public ComparativeLabel(String lang, String cubeUri, String typeUri, String part, String uri, String label) {
        super(lang, cubeUri, typeUri, uri, label);
        this.part = part;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeLabel}.
     *
     * @param lang        The language of this label and description.
     * @param cubeUri     The uri of the cube.
     * @param typeUri     The uri if the type of the element.
     * @param part        The part.
     * @param uri         The uri this label belongs to.
     * @param label       The label of the element.
     * @param description The description of the element.
     */
    public ComparativeLabel(String lang, String cubeUri, String typeUri, String part, String uri, String label, String description) {
        super(lang, cubeUri, typeUri, uri, label, description);
        this.part = part;
    }

    /**
     * Gets part.
     *
     * @return the part
     */
    public String getPart() {
        return part;
    }

    /**
     * Sets part.
     *
     * @param part the part
     */
    public void setPart(String part) {
        this.part = part;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ComparativeLabel.class.getSimpleName() + "[", "]")
                .add("lang='" + getLang() + "'")
                .add("cubeUri='" + getCubeUri() + "'")
                .add("typeUri='" + getTypeUri() + "'")
                .add("part='" + part + "'")
                .add("uri='" + getUri() + "'")
                .add("label='" + getLabel() + "'")
                .add("description='" + getDescription() + "'")
                .toString();
    }
}
