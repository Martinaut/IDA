package at.jku.dke.ida.data.models.similarity;

import at.jku.dke.ida.data.models.WordGroup;
import at.jku.dke.ida.shared.operations.PatternPart;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and the comparative elements' label.
 * The similarity is expressed by a score between 0 and 1.
 */
public class ComparativeSimilarity extends CubeSimilarity {

    private String part;

    /**
     * Instantiates a new instance of class {@linkplain ComparativeSimilarity}.
     */
    ComparativeSimilarity() {
        super();
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeSimilarity}.
     *
     * @param term    The term.
     * @param cube    The cube IRI.
     * @param element The element IRI.
     * @param type    The type URI.
     * @param score   The score.
     * @param part    The part.
     */
    public ComparativeSimilarity(WordGroup term, String cube, String element, String type, double score, String part) {
        super(term, cube, element, type, score);
        this.part = part;
    }

    /**
     * Gets pattern part.
     *
     * @return the pattern part
     */
    public PatternPart getPatternPart() {
        if (part == null) return null;
        if (part.equals("setOfInterest")) return PatternPart.SET_OF_INTEREST;
        if (part.equals("setOfComparison")) return PatternPart.SET_OF_COMPARISON;
        return null;
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
        return new StringJoiner(", ", ComparativeSimilarity.class.getSimpleName() + "[", "]")
                .add("term='" + getTerm() + "'")
                .add("cube='" + getCube() + "'")
                .add("part='" + part + "'")
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
        ComparativeSimilarity that = (ComparativeSimilarity) o;
        return Objects.equals(part, that.part);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), part);
    }
}
