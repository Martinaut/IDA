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
     * @param score   The score.
     */
    public Similarity(String term, String cube, String element, double score) {
        this.term = term;
        this.cube = cube;
        this.element = element;
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
                Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, cube, element, score);
    }

    @Override
    public int compareTo(Similarity other) {
        return Double.compare(score, other.score);
    }
}
