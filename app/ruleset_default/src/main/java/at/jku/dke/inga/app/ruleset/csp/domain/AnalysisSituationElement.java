package at.jku.dke.inga.app.ruleset.csp.domain;

import at.jku.dke.inga.data.models.Similarity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class AnalysisSituationElement {

    private Set<Similarity> elements;

    public AnalysisSituationElement() {
        this.elements = new HashSet<>();
    }

    public AnalysisSituationElement(Set<Similarity> elements) {
        this.elements = elements;
    }

    public Set<Similarity> getElements() {
        return elements;
    }

    public void setElements(Set<Similarity> elements) {
        this.elements = elements;
    }

    public double getScore() {
        if (elements == null) return 0;
        return elements.stream()
                .mapToDouble(Similarity::getScore)
                .reduce(0, (left, right) -> left * right);
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
