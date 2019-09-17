package at.jku.dke.ida.data.models.similarity;

import at.jku.dke.ida.data.models.WordGroup;
import at.jku.dke.ida.shared.operations.PatternPart;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and the comparative measure elements' label.
 * The similarity is expressed by a score between 0 and 1.
 */
public class ComparativeMeasureSimilarity extends ComparativeSimilarity {

    private String measure;

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureSimilarity}.
     */
    ComparativeMeasureSimilarity() {
        super();
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeMeasureSimilarity}.
     *
     * @param term    The term.
     * @param cube    The cube IRI.
     * @param element The element IRI.
     * @param type    The type URI.
     * @param score   The score.
     * @param part    The part.
     * @param measure The measure IRI.
     */
    public ComparativeMeasureSimilarity(WordGroup term, String cube, String element, String type, double score, String part, String measure) {
        super(term, cube, element, type, score, part);
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
        return new StringJoiner(", ", ComparativeMeasureSimilarity.class.getSimpleName() + "[", "]")
                .add("term='" + getTerm() + "'")
                .add("cube='" + getCube() + "'")
                .add("part='" + getPart() + "'")
                .add("element='" + getElement() + "'")
                .add("type='" + getType() + "'")
                .add("score=" + getScore())
                .add("measure=" + measure)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComparativeMeasureSimilarity that = (ComparativeMeasureSimilarity) o;
        return Objects.equals(measure, that.measure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), measure);
    }
}
