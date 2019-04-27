package at.jku.dke.ida.data.models;

import java.util.StringJoiner;

/**
 * Represents an element with a label and description of a specific language in a dimension.
 */
public class DimensionLabel extends Label {

    private String dimensionUri;
    private String dimensionLabel;

    /**
     * Instantiates a new instance of class {@linkplain DimensionLabel}.
     */
    DimensionLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain Label}.
     *
     * @param lang           The language of this label.
     * @param dimensionUri   The uri of the dimension.
     * @param dimensionLabel The label of the dimension.
     * @param uri            The uri this label belongs to.
     * @param label          The label of the element.
     */
    public DimensionLabel(String lang, String dimensionUri, String dimensionLabel, String uri, String label) {
        super(uri, lang, label);
        this.dimensionUri = dimensionUri;
        this.dimensionLabel = dimensionLabel;
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
     */
    public DimensionLabel(String lang, String dimensionUri, String dimensionLabel, String uri, String label, String description) {
        super(uri, lang, label, description);
        this.dimensionUri = dimensionUri;
        this.dimensionLabel = dimensionLabel;
    }

    /**
     * Gets the dimension uri.
     *
     * @return the dimension uri
     */
    public String getDimensionUri() {
        return dimensionUri;
    }

    /**
     * Sets the dimension uri.
     *
     * @param dimensionUri the dimension uri
     */
    public void setDimensionUri(String dimensionUri) {
        this.dimensionUri = dimensionUri;
    }

    /**
     * Gets the dimension label.
     *
     * @return the dimension label
     */
    public String getDimensionLabel() {
        return dimensionLabel;
    }

    /**
     * Sets the dimension label.
     *
     * @param dimensionLabel the dimension label
     */
    public void setDimensionLabel(String dimensionLabel) {
        this.dimensionLabel = dimensionLabel;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DimensionLabel.class.getSimpleName() + "[", "]")
                .add("lang='" + getLang() + "'")
                .add("dimensionUri='" + dimensionUri + "'")
                .add("dimensionLabel='" + dimensionLabel + "'")
                .add("uri='" + getUri() + "'")
                .add("label='" + getLabel() + "'")
                .add("description='" + getDescription() + "'")
                .toString();
    }

    @Override
    public String getTitle() {
        return dimensionLabel + ": " + super.getTitle();
    }
}
