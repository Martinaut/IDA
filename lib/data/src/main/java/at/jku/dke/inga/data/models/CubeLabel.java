package at.jku.dke.inga.data.models;

import at.jku.dke.inga.shared.display.Displayable;

import java.util.StringJoiner;

/**
 * Represents the database-table containing labels and descriptions of cubes.
 */
@Deprecated
public class CubeLabel implements Displayable {

    private String cubeUri;
    private String lang;
    private String label;
    private String description;

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     */
    CubeLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     *
     * @param cubeUri The cube this label belongs to.
     * @param lang    The language of this label.
     * @param label   The label of the cube.
     */
    public CubeLabel(String cubeUri, String lang, String label) {
        this.cubeUri = cubeUri;
        this.lang = lang;
        this.label = label;
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeLabel}.
     *
     * @param cubeUri     The cube this label belongs to.
     * @param lang        The language of this label and description.
     * @param label       The label of the cube.
     * @param description The description of the cube.
     */
    public CubeLabel(String cubeUri, String lang, String label, String description) {
        this(cubeUri, lang, label);
        this.description = description;
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
     * Sets the cube uri.
     *
     * @param cubeUri the cube uri
     */
    public void setCubeUri(String cubeUri) {
        this.cubeUri = cubeUri;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", CubeLabel.class.getSimpleName() + "[", "]")
                .add("cubeUri='" + cubeUri + "'")
                .add("lang='" + lang + "'")
                .add("label='" + label + "'")
                .add("description='" + description + "'")
                .toString();
    }

    /**
     * Returns the identifier of the item.
     *
     * @return The unique identifier.
     */
    @Override
    public String getDisplayableId() {
        return cubeUri;
    }

    /**
     * Returns the title of the item.
     *
     * @return The title.
     */
    @Override
    public String getTitle() {
        return label;
    }

    /**
     * Returns the details of the item.
     * Details can be for example a description of the item.
     *
     * @return The details.
     */
    @Override
    public String getDetails() {
        return description;
    }
}
