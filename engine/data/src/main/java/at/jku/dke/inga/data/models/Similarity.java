package at.jku.dke.inga.data.models;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and the elements' label.
 * The similarity is expressed by a score between 0 and 1.
 */
public class Similarity implements Comparable<Similarity> {

    private String term;
    private String cube;
    private String element;
    private String type;
    private double score;

    /**
     * Instantiates a new instance of class {@linkplain Similarity}.
     */
    Similarity() {
    }

    /**
     * Instantiates a new instance of class {@linkplain Similarity}.
     *
     * @param term    The term.
     * @param cube    The cube IRI.
     * @param element The element IRI.
     * @param type    The type URI.
     * @param score   The score.
     */
    public Similarity(String term, String cube, String element, String type, double score) {
        this.term = term;
        this.cube = cube;
        this.element = element;
        this.type = type;
        this.score = score;
    }

    /**
     * Gets the term.
     *
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Sets the term.
     *
     * @param term the term
     */
    public void setTerm(String term) {
        this.term = term;
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
     * Gets the element IRI.
     *
     * @return the element IRI
     */
    public String getElement() {
        return element;
    }

    /**
     * Sets the element IRI.
     *
     * @param element the element IRI
     */
    public void setElement(String element) {
        this.element = element;
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

    /**
     * Gets the score.
     *
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the score.
     *
     * @param score the score
     */
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Similarity.class.getSimpleName() + "[", "]")
                .add("term='" + term + "'")
                .add("cube='" + cube + "'")
                .add("element='" + element + "'")
                .add("type='" + type + "'")
                .add("score=" + score)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Similarity that = (Similarity) o;
        return Double.compare(that.score, score) == 0 &&
                Objects.equals(term, that.term) &&
                Objects.equals(cube, that.cube) &&
                Objects.equals(type, that.type) &&
                Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, cube, element, type, score);
    }

    @Override
    public int compareTo(Similarity other) {
        return Double.compare(score, other.score);
    }
}
