package at.jku.dke.ida.app.ruleset.helpers;

import at.jku.dke.ida.csp.domain.AnalysisSituationElement;
import at.jku.dke.ida.csp.domain.AnalysisSituationEntity;
import at.jku.dke.ida.data.models.similarity.*;
import at.jku.dke.ida.shared.operations.PatternPart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contains helpers that are used in the CSP-rules files.
 */
public final class CSPRuleHelpers {
    /**
     * Prevents creation of instances of this class.
     */
    private CSPRuleHelpers() {
    }

    /**
     * BigDecimal with value "-1"
     */
    public static final BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);

    /**
     * Returns whether all elements are in the specified cube.
     * <p>
     * If {@code elem} is {@code null} or empty, {@code true} will be returned.
     *
     * @param cube The cube.
     * @param elem The cube elements to check.
     * @return {@code true} if all elements are in the specified cube; {@code false} otherwise.
     */
    public static boolean allInCube(String cube, AnalysisSituationElement elem) {
        if (elem == null) return true;
        if (elem.getElements().isEmpty()) return true;

        return elem.getElements().stream()
                .allMatch(x -> x.getCube().equals(cube));
    }

    /**
     * Returns whether there is only at maximum one element per dimension.
     * <p>
     * If {@code elem} is {@code null}, empty or contains no {@link DimensionSimilarity} objects, {@code true} will be returned.
     * If {@code elem} contains elements which are no instances of {@link DimensionSimilarity}, {@code false} will be returned.
     *
     * @param elem The elements.
     * @return {@code true} if there is maximum one element per dimension; {@code false} otherwise.
     */
    public static boolean maximumOneElementPerDimension(AnalysisSituationElement elem) {
        if (elem == null) return true;
        if (elem.getElements().isEmpty()) return true;
        if (elem.getElements().stream().noneMatch(x -> x instanceof DimensionSimilarity)) return true;
        if (elem.getElements().stream().anyMatch(x -> !(x instanceof DimensionSimilarity))) return false;

        return elem.getElements().stream()
                .map(x -> ((DimensionSimilarity) x).getDimension())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream()
                .allMatch(x -> x <= 1);
    }

    /**
     * Returns whether one cube element is used in more than one place.
     *
     * @param as The analysis situation.
     * @return {@code true} if there exists at least one element that is used more than one time; {@code false} otherwise.
     */
    public static boolean containsDuplicates(AnalysisSituationEntity as) {
        if (as == null) return false;

        var grouped = as.getAllSimilarities().stream().collect(Collectors.groupingBy(CubeSimilarity::getElement));
        for (String elem : grouped.keySet()) {
            List<CubeSimilarity> sims = grouped.get(elem);
            if (sims.size() > 1) {
                if (sims.stream().allMatch(x -> x instanceof ComparativeSimilarity)) {
                    if (sims.stream()
                            .map(x -> (ComparativeSimilarity) x)
                            .collect(Collectors.groupingBy(ComparativeSimilarity::getPatternPart, Collectors.counting()))
                            .values().stream()
                            .anyMatch(x -> x > 1))
                        return true;
                } else
                    return true;
            }
        }

        return false;
    }

    /**
     * Returns whether a term is used for more than one element.
     * <p>
     * This function only compares the strings. For example, if there are the terms "amount" and "total amount", these
     * therms are not considered equal and therefore not multiple assignment is detected.
     *
     * @param as The analysis situation.
     * @return {@code true} if a term is used multiple times; {@code false} otherwise.
     */
    public static boolean multipleAssignmentsPerTerm(AnalysisSituationEntity as) {
        if (as == null) return false;

        Map<Integer, List<CubeSimilarity>> similarities = new HashMap<>();
        for (CubeSimilarity sim : as.getAllSimilarities()) {
            for (int idx : sim.getTerm().getIndices()) {
                similarities.computeIfAbsent(idx, k -> new ArrayList<>()).add(sim);
            }
        }

        for (List<CubeSimilarity> sims : similarities.values()) {
            if (sims.size() == 1) continue;
            if (sims.stream().allMatch(x -> x instanceof ComparativeSimilarity)) {
                var mapped = sims.stream().map(x -> (ComparativeSimilarity) x).collect(Collectors.toList());
                if (!(mapped.stream().anyMatch(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST) &&
                        mapped.stream().anyMatch(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON) &&
                        mapped.stream().map(CubeSimilarity::getType).distinct().count() == 1))
                    return true;
            } else
                return true;
        }


//        var similarities = as.getAllSimilarities().stream()
//                .collect(Collectors.groupingBy(x -> x.getTerm().getIndices()));
//        for (List<CubeSimilarity> sims : similarities.values()) {
//            if (sims.size() == 1) continue;
//            if (sims.stream().map(Similarity::getElement).distinct().count() > 1)
//                return true;
//        }
        return false;

//        return as.getAllSimilarities().stream()
//                .map(CubeSimilarity::getTerm)
//                .flatMap(x -> x.getIndices().stream())
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                .values().stream()
//                .anyMatch(x -> x > 1);

//        return as.getAllSimilarities().stream()
//                .map(CubeSimilarity::getTerm)
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                .values().stream()
//                .anyMatch(x -> x > 1);
    }

    /**
     * Returns whether all elements are in the appropriate cube
     * (all elements of same pattern part have to be in the same cube).
     * <p>
     * If {@code elem} is {@code null}, {@code true} will be returned.
     * Only {@link ComparativeSimilarity} elements are considered.
     *
     * @param elem The entity to check.
     * @return {@code true} if all elements are in the appropriate cube; {@code false} otherwise.
     */
    public static boolean allInAppropriateCube(AnalysisSituationEntity elem) {
        if (elem == null) return true;

        return elem.getAllSimilarities().stream()
                .filter(x -> x instanceof ComparativeSimilarity)
                .map(x -> (ComparativeSimilarity) x)
                .collect(Collectors.groupingBy(ComparativeSimilarity::getPatternPart))
                .values().stream()
                .allMatch(x -> x.stream().map(CubeSimilarity::getCube).distinct().count() != 1);
    }

    /**
     * Returns whether for each comparative measure exactly one element for each parts exist.
     * <p>
     * If {@code scores} is {@code null}, {@code true} will be returned.
     *
     * @param scores The list of comparative measures.
     * @return {@code true} if for each measure an element for both parts exist in the list; {@code false} otherwise.
     */
    public static boolean scoreInBothParts(AnalysisSituationElement scores) {
        if (scores == null) return true;

        var grouped = scores.getElements().stream()
                .filter(x -> x instanceof ComparativeMeasureSimilarity)
                .map(x -> (ComparativeMeasureSimilarity) x)
                .collect(Collectors.groupingBy(Similarity::getElement))
                .values();

        for (var elem : grouped) {
            boolean result = elem.size() == 2 &&
                    elem.stream().anyMatch(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST) &&
                    elem.stream().anyMatch(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON);
            if (!result) break;
        }
        return true;
    }

    /**
     * Returns whether for each join condition predicate an element for both parts exist.
     * <p>
     * If {@code joinConditions} is {@code null}, {@code true} will be returned.
     *
     * @param joinConditions The list of join condition predicates.
     * @return {@code true} if for each predicate an element for both parts exist in the list; {@code false} otherwise.
     */
    public static boolean predicateInBothParts(AnalysisSituationElement joinConditions) {
        if (joinConditions == null) return true;

        var grouped = joinConditions.getElements().stream()
                .filter(x -> x instanceof ComparativeSimilarity)
                .map(x -> (ComparativeSimilarity) x)
                .collect(Collectors.groupingBy(Similarity::getElement))
                .values();

        for (var elem : grouped) {
            boolean result = elem.stream().anyMatch(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST) &&
                    elem.stream().anyMatch(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON);
            if (!result) break;
        }
        return true;
    }
}
