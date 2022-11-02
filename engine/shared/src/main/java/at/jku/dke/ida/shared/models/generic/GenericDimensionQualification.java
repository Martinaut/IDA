package at.jku.dke.ida.shared.models.generic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Represents a dimension qualification.
 * It describes the selection of dimension nodes that have to be analyzed.
 *
 * @param <TValue> The type of the schema elements.
 */
public class GenericDimensionQualification<TValue extends Comparable<? super TValue>> implements Comparable<GenericDimensionQualification<TValue>> {

    private TValue dimension; // 1. a dimension instance of dimension schema
    private TValue diceLevel; // 2. a dice level variable
    private TValue diceNode; // 3. a dice node variable
    private Set<TValue> sliceConditions; // 4. a possibly empty set of slice conditions
    private TValue granularityLevel; // 5 . a granularity level variable

    /**
     * Instantiates a new instance of class {@linkplain GenericDimensionQualification}.
     */
    public GenericDimensionQualification() {
        this.dimension = null;
        this.granularityLevel = null;
        this.diceLevel = null;
        this.diceNode = null;
        this.sliceConditions = new HashSet<>();
    }

    /**
     * Instantiates a new instance of class {@linkplain GenericDimensionQualification}.
     *
     * @param dimension The URI of the dimension.
     */
    public GenericDimensionQualification(TValue dimension) {
        this();
        this.dimension = dimension;
    }

    /**
     * Gets the dimension.
     *
     * @return the dimension
     */
    public TValue getDimension() {
        return dimension;
    }

    /**
     * Sets the dimension.
     *
     * @param dimension the dimension
     */
    public void setDimension(TValue dimension) {
        this.dimension = dimension;
    }

    /**
     * Gets the dice level.
     *
     * @return the dice level
     */
    public TValue getDiceLevel() {
        return diceLevel;
    }

    /**
     * Sets the dice level.
     *
     * @param diceLevel the dice level
     */
    protected void setDiceLevel(TValue diceLevel) {
        this.diceLevel = diceLevel;
    }

    /**
     * Gets the dice node.
     *
     * @return the dice node
     */
    public TValue getDiceNode() {
        return diceNode;
    }

    /**
     * Sets the dice node.
     *
     * @param diceNode the dice node
     */
    protected void setDiceNode(TValue diceNode) {
        this.diceNode = diceNode;
    }

    /**
     * Sets the dice level and node.
     *
     * @param diceLevel The dice level.
     * @param diceNode  The dice node.
     */
    public void setDice(TValue diceLevel, TValue diceNode) {
        this.setDiceLevel(diceLevel);
        this.setDiceNode(diceNode);
    }

    /**
     * Gets a unmodifiable copy of the slice conditions (may be empty).
     *
     * @return the slice conditions
     */
    public Set<TValue> getSliceConditions() {
        return Collections.unmodifiableSet(sliceConditions);
    }

    /**
     * Sets the slice conditions.
     *
     * @param sliceConditions the slice conditions
     */
    public void setSliceConditions(Set<TValue> sliceConditions) {
        this.sliceConditions = Objects.requireNonNullElseGet(sliceConditions, HashSet::new);
    }

    /**
     * Adds the specified slice condition if it is not already present.
     *
     * @param cond The slice condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified element
     * @see Set#add(Object)
     */
    public boolean addSliceCondition(TValue cond) {
        return sliceConditions.add(cond);
    }

    /**
     * Removes the specified slice condition if it is present.
     *
     * @param cond The slice condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeSliceCondition(TValue cond) {
        return sliceConditions.remove(cond);
    }

    /**
     * Gets the granularity level.
     *
     * @return the granularity level
     */
    public TValue getGranularityLevel() {
        return granularityLevel;
    }

    /**
     * Sets the granularity level.
     *
     * @param granularityLevel the granularity level
     */
    public void setGranularityLevel(TValue granularityLevel) {
        this.granularityLevel = granularityLevel;
    }

    /**
     * Returns whether the current dimension qualification is filled.
     * This means that all necessary fields are set.
     *
     * @return {@code true}, if all necessary fields of the dimension qualification are filled.
     */
    @JsonIgnore
    public boolean isFilled() {
        return dimension != null &&
                diceLevel != null &&
                diceNode != null &&
                granularityLevel != null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericDimensionQualification.class.getSimpleName() + "[", "]")
                .add("dimension='" + dimension + "'")
                .add("diceLevel='" + diceLevel + "'")
                .add("diceNode='" + diceNode + "'")
                .add("sliceConditions=" + sliceConditions)
                .add("granularityLevel='" + granularityLevel + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericDimensionQualification that = (GenericDimensionQualification) o;
        return Objects.equals(dimension, that.dimension) &&
                Objects.equals(diceLevel, that.diceLevel) &&
                Objects.equals(diceNode, that.diceNode) &&
                Objects.equals(sliceConditions, that.sliceConditions) &&
                Objects.equals(granularityLevel, that.granularityLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, diceLevel, diceNode, sliceConditions, granularityLevel);
    }

    @Override
    public int compareTo(GenericDimensionQualification<TValue> other) {
        return dimension.compareTo(other.dimension);
    }
}
