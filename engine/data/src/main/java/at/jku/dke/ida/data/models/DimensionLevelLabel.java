package at.jku.dke.ida.data.models;

import java.util.StringJoiner;

/**
 * Represents an element with a label and description of a specific language in a dimension for a level.
 */
public class DimensionLevelLabel extends DimensionLabel {

    private String levelUri;
    private String levelLabel;

    /**
     * Instantiates a new instance of class {@linkplain DimensionLevelLabel}.
     */
    DimensionLevelLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     *
     * @param lang           The language of this label.
     * @param dimensionUri   The uri of the dimension.
     * @param dimensionLabel The label of the dimension.
     * @param uri            The uri this label belongs to.
     * @param label          The label of the element.
     * @param levelLabel     The label of the level.
     * @param levelUri       The uri of the level.
     */
    public DimensionLevelLabel(String lang, String dimensionUri, String dimensionLabel, String levelUri, String levelLabel, String uri, String label) {
        super(lang, dimensionUri, dimensionLabel, uri, label);
        this.levelUri = levelUri;
        this.levelLabel = levelLabel;
    }

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     *
     * @param lang           The language of this label and description.
     * @param dimensionUri   The uri of the dimension.
     * @param dimensionLabel The label of the dimension.
     * @param uri            The uri this label belongs to.
     * @param label          The label of the element.
     * @param description    The description of the element.
     * @param levelLabel     The label of the level.
     * @param levelUri       The uri of the level.
     */
    public DimensionLevelLabel(String lang, String dimensionUri, String dimensionLabel, String levelUri, String levelLabel, String uri, String label, String description) {
        super(lang, dimensionUri, dimensionLabel, uri, label, description);
        this.levelUri = levelUri;
        this.levelLabel = levelLabel;
    }

    /**
     * Gets the dimension uri.
     *
     * @return the dimension uri
     */
    public String getLevelUri() {
        return levelUri;
    }

    /**
     * Sets the dimension uri.
     *
     * @param levelUri the dimension uri
     */
    public void setLevelUri(String levelUri) {
        this.levelUri = levelUri;
    }

    /**
     * Gets the dimension label.
     *
     * @return the dimension label
     */
    public String getLevelLabel() {
        return levelLabel;
    }

    /**
     * Sets the dimension label.
     *
     * @param levelLabel the dimension label
     */
    public void setLevelLabel(String levelLabel) {
        this.levelLabel = levelLabel;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DimensionLevelLabel.class.getSimpleName() + "[", "]")
                .add("lang='" + getLang() + "'")
                .add("dimensionUri='" + getDimensionUri() + "'")
                .add("dimensionLabel='" + getDimensionLabel() + "'")
                .add("levelUri='" + levelUri + "'")
                .add("levelLabel='" + levelLabel + "'")
                .add("uri='" + getUri() + "'")
                .add("label='" + getLabel() + "'")
                .add("description='" + getDescription() + "'")
                .toString();
    }

    @Override
    public String getTitle() {
        return levelLabel + " - " + super.getTitle();
    }
}
