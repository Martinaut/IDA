package at.jku.dke.ida.data.models.labels;

import at.jku.dke.ida.shared.display.Displayable;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents an element with a label and description of a specific language.
 */
public class Label implements Displayable, Comparable<Label> {

    private String uri;
    private String lang;
    private String label;
    private String description;

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     */
    Label() {
    }

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     *
     * @param uri   The uri this label belongs to.
     * @param lang  The language of this label.
     * @param label The label of the element.
     */
    public Label(String uri, String lang, String label) {
        this.uri = uri;
        this.lang = lang;
        this.label = label;
    }

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     *
     * @param uri         The uri this label belongs to.
     * @param lang        The language of this label and description.
     * @param label       The label of the element.
     * @param description The description of the element.
     */
    public Label(String uri, String lang, String label, String description) {
        this(uri, lang, label);
        this.description = description;
    }

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     *
     * @param label The label of the element.
     */
    public Label(String label) {
        this.label = label;
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
     * Gets the language.
     *
     * @return the language
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the language.
     *
     * @param lang the language
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Label.class.getSimpleName() + "[", "]")
                .add("uri='" + uri + "'")
                .add("lang='" + lang + "'")
                .add("label='" + label + "'")
                .add("description='" + description + "'")
                .toString();
    }

    @Override
    public String getDisplayableId() {
        return uri;
    }

    @Override
    public String getTitle() {
        return label;
    }

    @Override
    public String getDetails() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label1 = (Label) o;
        return Objects.equals(uri, label1.uri) &&
                Objects.equals(lang, label1.lang) &&
                Objects.equals(label, label1.label) &&
                Objects.equals(description, label1.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, lang, label, description);
    }

    @Override
    public int compareTo(Label other) {
        return label.compareTo(other.label);
    }
}
