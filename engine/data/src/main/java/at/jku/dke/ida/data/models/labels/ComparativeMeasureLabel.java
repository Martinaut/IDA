package at.jku.dke.ida.data.models.labels;

import java.util.StringJoiner;

/**
 * Represents a comparative element with a label and description of a specific language.
 */
public class ComparativeMeasureLabel extends ComparativeLabel {

    private String measure;

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureLabel}.
     */
    ComparativeMeasureLabel() {
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureLabel}.
     *
     * @param lang    The language of this label.
     * @param part    The part.
     * @param uri     The uri this label belongs to.
     * @param label   The label of the element.
     * @param measure The measure IRI.
     */
    public ComparativeMeasureLabel(String lang, String part, String uri, String label, String measure) {
        super(uri, part, lang, label);
        this.measure = measure;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureLabel}.
     *
     * @param lang        The language of this label and description.
     * @param part        The part.
     * @param uri         The uri this label belongs to.
     * @param label       The label of the element.
     * @param description The description of the element.
     * @param measure     The measure IRI.
     */
    public ComparativeMeasureLabel(String lang, String part, String uri, String label, String description, String measure) {
        super(uri, part, lang, label, description);
        this.measure = measure;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureLabel}.
     *
     * @param lang    The language of this label.
     * @param cubeUri The uri of the cube.
     * @param typeUri The uri if the type of the element.
     * @param part    The part.
     * @param uri     The uri this label belongs to.
     * @param label   The label of the element.
     * @param measure The measure IRI.
     */
    public ComparativeMeasureLabel(String lang, String cubeUri, String typeUri, String part, String uri, String label, String measure) {
        super(lang, cubeUri, typeUri, part, uri, label);
        this.measure = measure;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureLabel}.
     *
     * @param lang        The language of this label and description.
     * @param cubeUri     The uri of the cube.
     * @param typeUri     The uri if the type of the element.
     * @param part        The part.
     * @param uri         The uri this label belongs to.
     * @param label       The label of the element.
     * @param description The description of the element.
     * @param measure     The measure IRI.
     */
    public ComparativeMeasureLabel(String lang, String cubeUri, String typeUri, String part, String uri, String label, String description, String measure) {
        super(lang, cubeUri, typeUri, part, uri, label, description);
        this.measure = measure;
    }

    /**
     * Gets measure.
     *
     * @return the measure
     */
    public String getMeasure() {
        return measure;
    }

    /**
     * Sets measure.
     *
     * @param measure the measure
     */
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ComparativeMeasureLabel.class.getSimpleName() + "[", "]")
                .add("lang='" + getLang() + "'")
                .add("cubeUri='" + getCubeUri() + "'")
                .add("typeUri='" + getTypeUri() + "'")
                .add("part='" + getPart() + "'")
                .add("uri='" + getUri() + "'")
                .add("label='" + getLabel() + "'")
                .add("description='" + getDescription() + "'")
                .add("measure='" + measure + "'")
                .toString();
    }
}
