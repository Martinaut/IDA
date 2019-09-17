package at.jku.dke.ida.data.models.similarity;

import at.jku.dke.ida.data.models.WordGroup;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and the elements' label.
 * The similarity is expressed by a score between 0 and 1.
 */
public class CubeSimilarity extends Similarity<String> {

    private String cube;
    private String type;

    /**
     * Instantiates a new instance of class {@linkplain CubeSimilarity}.
     */
    CubeSimilarity() {
    }

    /**
     * Instantiates a new instance of class {@linkplain CubeSimilarity}.
     *
     * @param term    The term.
     * @param cube    The cube IRI.
     * @param element The element IRI.
     * @param type    The type URI.
     * @param score   The score.
     */
    public CubeSimilarity(WordGroup term, String cube, String element, String type, double score) {
        super(term, element, score);
        this.cube = cube;
        this.type = type;
    }

    /**
     * Gets the cube IRI.
     *
     * @return the cube IRI
     */
    public String getCube() {
        return cube;
    }

    /**
     * Sets the cube IRI.
     *
     * @param cube the cube IRI
     */
    public void setCube(String cube) {
        this.cube = cube;
    }

    /**
     * Gets the type of the element.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the element.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CubeSimilarity.class.getSimpleName() + "[", "]")
                .add("term='" + getTerm() + "'")
                .add("cube='" + cube + "'")
                .add("element='" + getElement() + "'")
                .add("type='" + type + "'")
                .add("score=" + getScore())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CubeSimilarity that = (CubeSimilarity) o;
        return Objects.equals(cube, that.cube) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cube, type);
    }
}
