package at.jku.dke.inga.app.ruleset.csp.domain;

import at.jku.dke.inga.data.models.CubeSimilarity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class AnalysisSituationElement {

    private Set<CubeSimilarity> elements;

    public AnalysisSituationElement() {
        this.elements = new HashSet<>();
    }

    public AnalysisSituationElement(Set<CubeSimilarity> elements) {
        this.elements = elements;
    }

    public Set<CubeSimilarity> getElements() {
        return elements;
    }

    public void setElements(Set<CubeSimilarity> elements) {
        this.elements = elements;
    }

    public double getScore() {
        if (elements == null) return 1;
        double score = elements.stream()
                .mapToDouble(CubeSimilarity::getScore)
                .reduce(0, (left, right) -> left * right);
        return Double.compare(score, 0) == 0 ? 1 : score;
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
