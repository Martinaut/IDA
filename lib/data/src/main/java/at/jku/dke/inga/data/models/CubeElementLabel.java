package at.jku.dke.inga.data.models;

import at.jku.dke.inga.shared.display.Displayable;

import javax.persistence.*;
import java.util.StringJoiner;

/**
 * Represents the database-table containing labels and descriptions of cube elements.
 */
@Entity(name = "cube_element_labels")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"cubeElementUri", "lang"})})
public class CubeElementLabel implements Displayable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 255)
    private String cubeElementUri;

    @Column(nullable = false, length = 5)
    private String lang;

    @Column(nullable = false)
    private String label;

    @Column
    private String description;

    /**
     * Instantiates a new instance of class {@linkplain CubeElementLabel}.
     */
    CubeElementLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeElementLabel}.
     *
     * @param cubeElementUri The cube element this label belongs to.
     * @param lang           The language of this label.
     * @param label          The label of the cube element.
     */
    public CubeElementLabel(String cubeElementUri, String lang, String label) {
        this.cubeElementUri = cubeElementUri;
        this.lang = lang;
        this.label = label;
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeElementLabel}.
     *
     * @param cubeElementUri The cube element this label belongs to.
     * @param lang           The language of this label and description.
     * @param label          The label of the cube element.
     * @param description    The description of the cube element.
     */
    public CubeElementLabel(String cubeElementUri, String lang, String label, String description) {
        this(cubeElementUri, lang, label);
        this.description = description;
    }

    /**
     * Gets the id.
     *
     * @return the id
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
     * Gets the cube element uri.
     *
     * @return the cube element uri
     */
    public String getCubeElementUri() {
        return cubeElementUri;
    }

    /**
     * Sets the cube element uri.
     *
     * @param cubeElementUri the cube element uri
     */
    public void setCubeElementUri(String cubeElementUri) {
        this.cubeElementUri = cubeElementUri;
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
        return new StringJoiner(", ", CubeElementLabel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cubeElementUri='" + cubeElementUri + "'")
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
        return cubeElementUri;
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
