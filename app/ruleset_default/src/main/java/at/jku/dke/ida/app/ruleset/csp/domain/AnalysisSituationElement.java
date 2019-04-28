package at.jku.dke.ida.app.ruleset.csp.domain;

import at.jku.dke.ida.data.models.CubeSimilarity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * The element contains a set of analysis situation elements.
 * This elements are of type {@link CubeSimilarity} which contain the element and its similarity score.
 */
public class AnalysisSituationElement {

    private Set<CubeSimilarity> elements;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationElement}.
     */
    public AnalysisSituationElement() {
        this.elements = new HashSet<>();
    }

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationElement}.
     *
     * @param elements The set of elements.
     */
    public AnalysisSituationElement(Set<CubeSimilarity> elements) {
        this.elements = elements;
    }

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public Set<CubeSimilarity> getElements() {
        return elements;
    }

    /**
     * Sets the elements.
     *
     * @param elements the elements
     */
    public void setElements(Set<CubeSimilarity> elements) {
        this.elements = elements;
    }

    /**
     * Gets the score of the element.
     * <p>
     * For this, the scores of all elements are multiplied.
     * If elements is {@code null} or empty, 0 will be returned.
     *
     * @return the score
     */
    public double getScore() {
        if (elements == null || elements.isEmpty()) return 0;
        return elements.stream()
                .mapToDouble(CubeSimilarity::getScore)
                .reduce(1, (left, right) -> left * right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisSituationElement that = (AnalysisSituationElement) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituationElement.class.getSimpleName() + "[", "]")
                .add("elements=" + elements)
                .toString();
    }
}
