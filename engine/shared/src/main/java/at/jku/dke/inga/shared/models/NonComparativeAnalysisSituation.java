package at.jku.dke.inga.shared.models;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Non-comparative analysis situations represent multi-dimensional queries on cube instances.
 */
public class NonComparativeAnalysisSituation extends AnalysisSituation {

    private String cube; // 1. a cube instance of cube schema
    private Set<String> baseMeasureConditions; // 2. a possibly empty set of base measure conditions
    private Set<String> measures; // 3. a non-empty set of measures
    private Map<String, DimensionQualification> dimensionQualifications; // 4. a set of dimension qualifications
    private Set<String> filterConditions; // 5. a possibly empty set of filter conditions

    /**
     * Instantiates a new instance of class {@linkplain NonComparativeAnalysisSituation}.
     */
    public NonComparativeAnalysisSituation() {
        this.baseMeasureConditions = new HashSet<>();
        this.measures = new HashSet<>();
        this.dimensionQualifications = new HashMap<>();
        this.filterConditions = new HashSet<>();
    }

    /**
     * Gets the cube.
     *
     * @return the cube
     */
    public String getCube() {
        return cube;
    }

    /**
     * Sets the cube.
     *
     * @param cube the cube
     */
    public void setCube(String cube) {
        this.cube = cube;
    }

    // region --- BaseMeasureConditions ---

    /**
     * Gets the base measure conditions (may be empty) as unmodifiable set.
     *
     * @return the base measure conditions
     */
    public Set<String> getBaseMeasureConditions() {
        return Collections.unmodifiableSet(baseMeasureConditions);
    }

    /**
     * Sets the base measure conditions (may be empty).
     *
     * @param baseMeasureConditions the base measure conditions
     */
    public void setBaseMeasureConditions(Set<String> baseMeasureConditions) {
        this.baseMeasureConditions = Objects.requireNonNullElseGet(baseMeasureConditions, HashSet::new);
    }

    /**
     * Adds the specified base measure condition if it is not already present.
     *
     * @param cond The base measure condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified condition
     * @see Set#add(Object)
     */
    public boolean addBaseMeasureCondition(String cond) {
        return baseMeasureConditions.add(cond);
    }

    /**
     * Removes the specified base measure condition if it is present.
     *
     * @param cond The base measure condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeBaseMeasureCondition(String cond) {
        return baseMeasureConditions.remove(cond);
    }

    // endregion

    // region --- Measures ---

    /**
     * Gets the measures as unmodifiable set.
     *
     * @return the measures
     */
    public Set<String> getMeasures() {
        return measures;
    }

    /**
     * Sets the measures.
     *
     * @param measures the measures
     * @throws IllegalArgumentException If {@code measures} is empty or {@code null}.
     */
    public void setMeasures(Set<String> measures) {
        if (measures == null || measures.isEmpty()) throw new IllegalArgumentException("measures must not be empty");
        this.measures = measures;
    }

    /**
     * Adds the specified measure if it is not already present.
     *
     * @param measure The measure to be added to the set.
     * @return {@code true} if this set did not already contain the specified measure
     * @see Set#add(Object)
     */
    public boolean addMeasure(String measure) {
        return measures.add(measure);
    }

    /**
     * Removes the specified measure if it is present.
     *
     * @param measure The measure to be removed from the set, if present.
     * @return {@code true} if the set contained the specified measure
     * @see Set#remove(Object)
     */
    public boolean removeMeasure(String measure) {
        return measures.remove(measure);
    }
    // endregion

    // region --- Dimension Qualification ---

    /**
     * Gets the dimension qualifications as unmodifiable set.
     *
     * @return the dimension qualifications
     */
    public Set<DimensionQualification> getDimensionQualifications() {
        return Set.copyOf(dimensionQualifications.values());
    }

    /**
     * Sets the dimension qualifications.
     *
     * @param dimensionQualifications the dimension qualifications
     * @throws IllegalArgumentException If {@code dimensionQualifications} contains duplicate {@link DimensionQualification#getDimension()} values.
     */
    public void setDimensionQualifications(Set<DimensionQualification> dimensionQualifications) {
        if (dimensionQualifications == null ||
                dimensionQualifications.stream().map(DimensionQualification::getDimension).distinct().count() != dimensionQualifications.size())
            throw new IllegalArgumentException("There are duplicate dimension values in dimensionQualifications.");

        this.dimensionQualifications = dimensionQualifications.stream()
                .collect(Collectors.toMap(DimensionQualification::getDimension, Function.identity()));
    }

    /**
     * Returns the dimension qualification for the specified dimension URI.
     * Use this method if you want to modify a property of a dimension qualification.
     *
     * @param dimensionUri The dimension URI.
     * @return The dimension qualification instance, if present.
     */
    public DimensionQualification getDimensionQualification(String dimensionUri) {
        return this.dimensionQualifications.get(dimensionUri);
    }

    /**
     * Adds the specified dimension qualification if it is not already present.
     *
     * @param qualification The dimension qualification to be added to the set.
     * @throws IllegalArgumentException If the {@code qualification} is {@code null} or if there exists already an
     *                                  entry with the same {@link DimensionQualification#getDimension()} value.
     */
    public void addDimensionQualification(DimensionQualification qualification) {
        if (qualification == null || dimensionQualifications.containsKey(qualification.getDimension()))
            throw new IllegalArgumentException("qualification must not be null and the dimension has to be unique.");
        dimensionQualifications.put(qualification.getDimension(), qualification);
    }

    /**
     * Removes the specified dimension qualification if it is present.
     *
     * @param qualification The dimension qualification to be removed from the set, if present.
     */
    public void removeDimensionQualification(DimensionQualification qualification) {
        dimensionQualifications.remove(qualification.getDimension());
    }

    // endregion

    // region --- FilterConditions ---

    /**
     * Gets the filter conditions (may be empty) as unmodifiable set.
     *
     * @return the filter conditions
     */
    public Set<String> getFilterConditions() {
        return Collections.unmodifiableSet(filterConditions);
    }

    /**
     * Sets the filter conditions (may be empty).
     *
     * @param filterConditions the filter conditions
     */
    public void setFilterConditions(Set<String> filterConditions) {
        this.filterConditions = Objects.requireNonNullElseGet(filterConditions, HashSet::new);
    }

    /**
     * Adds the specified filter condition if it is not already present.
     *
     * @param cond The filter condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified condition
     * @see Set#add(Object)
     */
    public boolean addFilterCondition(String cond) {
        return filterConditions.add(cond);
    }

    /**
     * Removes the specified filter condition if it is present.
     *
     * @param cond The filter condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeFilterCondition(String cond) {
        return filterConditions.remove(cond);
    }
    // endregion

    @Override
    public boolean isExecutable() {
        return StringUtils.isNotBlank(cube) &&
                !measures.isEmpty() &&
                dimensionQualifications.values().stream().allMatch(DimensionQualification::isFilled);
    }

    @Override
    public boolean isCubeDefined() {
        return StringUtils.isNotBlank(cube);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NonComparativeAnalysisSituation.class.getSimpleName() + "[", "]")
                .add("cube='" + cube + "'")
                .add("baseMeasureConditions=" + baseMeasureConditions)
                .add("measures=" + measures)
                .add("dimensionQualifications=" + dimensionQualifications)
                .add("filterConditions=" + filterConditions)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NonComparativeAnalysisSituation that = (NonComparativeAnalysisSituation) o;
        return Objects.equals(cube, that.cube) &&
                Objects.equals(baseMeasureConditions, that.baseMeasureConditions) &&
                Objects.equals(measures, that.measures) &&
                Objects.equals(dimensionQualifications, that.dimensionQualifications) &&
                Objects.equals(filterConditions, that.filterConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cube, baseMeasureConditions, measures, dimensionQualifications, filterConditions);
    }
}
