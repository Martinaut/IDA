package at.jku.dke.ida.data.models;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and the dimension elements' label.
 * The similarity is expressed by a score between 0 and 1.
 */
public class DimensionSimilarity extends CubeSimilarity {

    private String dimension;

    /**
     * Instantiates a new instance of class {@linkplain DimensionSimilarity}.
     */
    DimensionSimilarity() {
        super();
    }

    /**
     * Instantiates a new instance of class {@linkplain DimensionSimilarity}.
     *
     * @param term      The term.
     * @param cube      The cube IRI.
     * @param element   The element IRI.
     * @param type      The type URI.
     * @param score     The score.
     * @param dimension The dimension.
     */
    public DimensionSimilarity(String term, String cube, String element, String type, double score, String dimension) {
        super(term, cube, element, type, score);
        this.dimension = dimension;
    }

    /**
     * Gets dimension.
     *
     * @return the dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * Sets dimension.
     *
     * @param dimension the dimension
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DimensionSimilarity.class.getSimpleName() + "[", "]")
                .add("term='" + getTerm() + "'")
                .add("cube='" + getCube() + "'")
                .add("dimension='" + dimension + "'")
                .add("element='" + getElement() + "'")
                .add("type='" + getType() + "'")
                .add("score=" + getScore())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DimensionSimilarity that = (DimensionSimilarity) o;
        return Objects.equals(dimension, that.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dimension);
    }
}
