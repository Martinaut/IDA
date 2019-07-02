package at.jku.dke.ida.shared.models.generic;

import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.models.DimensionQualification;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Non-comparative analysis situations represent multi-dimensional queries on cube instances.
 *
 * @param <TValue>   The type of the schema elements.
 * @param <TDimQual> The type of the dimension qualifications.
 */
public class GenericNonComparativeAnalysisSituation<TValue extends Comparable<? super TValue>, TDimQual extends GenericDimensionQualification<TValue>> extends AnalysisSituation {

    private TValue cube; // 1. a cube instance of cube schema
    private Set<TValue> baseMeasureConditions; // 2. a possibly empty set of base measure conditions
    private Set<TValue> measures; // 3. a non-empty set of measures
    private Map<TValue, TDimQual> dimensionQualifications; // 4. a set of dimension qualifications
    private Set<TValue> filterConditions; // 5. a possibly empty set of filter conditions

    /**
     * Instantiates a new instance of class {@linkplain GenericNonComparativeAnalysisSituation}.
     */
    public GenericNonComparativeAnalysisSituation() {
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
    public TValue getCube() {
        return cube;
    }

    /**
     * Sets the cube.
     *
     * @param cube the cube
     */
    public void setCube(TValue cube) {
        this.cube = cube;
    }

    // region --- BaseMeasureConditions ---

    /**
     * Gets the base measure conditions (may be empty) as unmodifiable set.
     *
     * @return the base measure conditions
     */
    public Set<TValue> getBaseMeasureConditions() {
        return Collections.unmodifiableSet(baseMeasureConditions);
    }

    /**
     * Sets the base measure conditions (may be empty).
     *
     * @param baseMeasureConditions the base measure conditions
     */
    public void setBaseMeasureConditions(Set<TValue> baseMeasureConditions) {
        this.baseMeasureConditions = Objects.requireNonNullElseGet(baseMeasureConditions, HashSet::new);
    }

    /**
     * Adds the specified base measure condition if it is not already present.
     *
     * @param cond The base measure condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified condition
     * @see Set#add(Object)
     */
    public boolean addBaseMeasureCondition(TValue cond) {
        return baseMeasureConditions.add(cond);
    }

    /**
     * Removes the specified base measure condition if it is present.
     *
     * @param cond The base measure condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeBaseMeasureCondition(TValue cond) {
        return baseMeasureConditions.remove(cond);
    }

    // endregion

    // region --- Measures ---

    /**
     * Gets the measures as unmodifiable set.
     *
     * @return the measures
     */
    public Set<TValue> getMeasures() {
        return measures;
    }

    /**
     * Sets the measures.
     *
     * @param measures the measures
     */
    public void setMeasures(Set<TValue> measures) {
        this.measures = Objects.requireNonNullElseGet(measures, HashSet::new);
    }

    /**
     * Adds the specified measure if it is not already present.
     *
     * @param measure The measure to be added to the set.
     * @return {@code true} if this set did not already contain the specified measure
     * @see Set#add(Object)
     */
    public boolean addMeasure(TValue measure) {
        return measures.add(measure);
    }

    /**
     * Removes the specified measure if it is present.
     *
     * @param measure The measure to be removed from the set, if present.
     * @return {@code true} if the set contained the specified measure
     * @see Set#remove(Object)
     */
    public boolean removeMeasure(TValue measure) {
        return measures.remove(measure);
    }
    // endregion

    // region --- Dimension Qualification ---

    /**
     * Gets the dimension qualifications as unmodifiable set.
     *
     * @return the dimension qualifications
     */
    public Set<TDimQual> getDimensionQualifications() {
        return Set.copyOf(dimensionQualifications.values());
    }

    /**
     * Sets the dimension qualifications.
     *
     * @param dimensionQualifications the dimension qualifications
     * @throws IllegalArgumentException If {@code dimensionQualifications} contains duplicate {@link DimensionQualification#getDimension()} values.
     */
    public void setDimensionQualifications(Set<TDimQual> dimensionQualifications) {
        if (dimensionQualifications == null ||
                dimensionQualifications.stream().map(TDimQual::getDimension).distinct().count() != dimensionQualifications.size())
            throw new IllegalArgumentException("There are duplicate dimension values in dimensionQualifications.");

        this.dimensionQualifications = dimensionQualifications.stream()
                .collect(Collectors.toMap(TDimQual::getDimension, Function.identity()));
    }

    /**
     * Returns the dimension qualification for the specified dimension URI.
     * Use this method if you want to modify a property of a dimension qualification.
     *
     * @param dimensionUri The dimension URI.
     * @return The dimension qualification instance, if present.
     */
    public TDimQual getDimensionQualification(TValue dimensionUri) {
        return this.dimensionQualifications.get(dimensionUri);
    }

    /**
     * Adds the specified dimension qualification if it is not already present.
     *
     * @param qualification The dimension qualification to be added to the set.
     * //@throws IllegalArgumentException If the {@code qualification} is {@code null} or if there exists already an
     * //                                 entry with the same {@link DimensionQualification#getDimension()} value.
     */
    public void addDimensionQualification(TDimQual qualification) {
        if (qualification == null || dimensionQualifications.containsKey(qualification.getDimension()))
            return;//throw new IllegalArgumentException("qualification must not be null and the dimension has to be unique.");
        dimensionQualifications.put(qualification.getDimension(), qualification);
    }

    /**
     * Removes the specified dimension qualification if it is present.
     *
     * @param qualification The dimension qualification to be removed from the set, if present.
     */
    public void removeDimensionQualification(TDimQual qualification) {
        dimensionQualifications.remove(qualification.getDimension());
    }

    // endregion

    // region --- FilterConditions ---

    /**
     * Gets the filter conditions (may be empty) as unmodifiable set.
     *
     * @return the filter conditions
     */
    public Set<TValue> getFilterConditions() {
        return Collections.unmodifiableSet(filterConditions);
    }

    /**
     * Sets the filter conditions (may be empty).
     *
     * @param filterConditions the filter conditions
     */
    public void setFilterConditions(Set<TValue> filterConditions) {
        this.filterConditions = Objects.requireNonNullElseGet(filterConditions, HashSet::new);
    }

    /**
     * Adds the specified filter condition if it is not already present.
     *
     * @param cond The filter condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified condition
     * @see Set#add(Object)
     */
    public boolean addFilterCondition(TValue cond) {
        return filterConditions.add(cond);
    }

    /**
     * Removes the specified filter condition if it is present.
     *
     * @param cond The filter condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeFilterCondition(TValue cond) {
        return filterConditions.remove(cond);
    }
    // endregion

    @Override
    public boolean isExecutable() {
        return isCubeDefined() &&
                !measures.isEmpty() &&
                dimensionQualifications.values().stream().allMatch(TDimQual::isFilled);
    }

    @Override
    public boolean isCubeDefined() {
        return cube != null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericNonComparativeAnalysisSituation.class.getSimpleName() + "[", "]")
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
        GenericNonComparativeAnalysisSituation that = (GenericNonComparativeAnalysisSituation) o;
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
