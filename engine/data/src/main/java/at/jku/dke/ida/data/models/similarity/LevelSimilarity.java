package at.jku.dke.ida.data.models.similarity;

import at.jku.dke.ida.data.models.WordGroup;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and the level elements' label.
 * The similarity is expressed by a score between 0 and 1.
 */
public class LevelSimilarity extends CubeSimilarity {

    private String dimension;
    private String level;

    /**
     * Instantiates a new instance of class {@linkplain LevelSimilarity}.
     */
    LevelSimilarity() {
        super();
    }

    /**
     * Instantiates a new instance of class {@linkplain LevelSimilarity}.
     *
     * @param term      The term.
     * @param cube      The cube IRI.
     * @param element   The element IRI.
     * @param type      The type URI.
     * @param score     The score.
     * @param dimension The dimension IRI.
     * @param level     The level IRI.
     */
    public LevelSimilarity(WordGroup term, String cube, String element, String type, double score, String dimension, String level) {
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

    /**
     * Gets level.
     *
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LevelSimilarity.class.getSimpleName() + "[", "]")
                .add("term='" + getTerm() + "'")
                .add("cube='" + getCube() + "'")
                .add("dimension='" + dimension + "'")
                .add("level='" + level + "'")
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
        LevelSimilarity that = (LevelSimilarity) o;
        return Objects.equals(dimension, that.dimension) &&
                Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dimension, level);
    }
}
