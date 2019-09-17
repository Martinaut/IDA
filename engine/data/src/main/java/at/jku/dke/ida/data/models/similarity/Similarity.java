package at.jku.dke.ida.data.models.similarity;

import at.jku.dke.ida.data.models.WordGroup;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a similarity result between a term and and an element.
 * The similarity is expressed by a score between 0 and 1.
 */
public class Similarity<TElement> implements Comparable<Similarity<?>> {

    private WordGroup term;
    private TElement element;
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
     * @param element The element.
     * @param score   The score.
     */
    public Similarity(WordGroup term, TElement element, double score) {
        this.term = term;
        this.element = element;
        this.score = score;
    }

    /**
     * Gets the term.
     *
     * @return the term
     */
    public WordGroup getTerm() {
        return term;
    }

    /**
     * Sets the term.
     *
     * @param term the term
     */
    public void setTerm(WordGroup term) {
        this.term = term;
    }

    /**
     * Gets the element.
     *
     * @return the element
     */
    public TElement getElement() {
        return element;
    }

    /**
     * Sets the element IRI.
     *
     * @param element the element IRI
     */
    public void setElement(TElement element) {
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
                .add("element='" + element + "'")
                .add("score=" + score)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Similarity<?> that = (Similarity<?>) o;
        return Double.compare(that.score, score) == 0 &&
                Objects.equals(term, that.term) &&
                Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, element, score);
    }

    @Override
    public int compareTo(Similarity<?> other) {
        return Double.compare(other.score, score);
    }
}
