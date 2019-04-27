package at.jku.dke.ida.shared.models;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Represents a dimension qualification.
 * It describes the selection of dimension nodes that have to be analyzed.
 */
public class DimensionQualification implements Comparable<DimensionQualification> {

    private String dimension; // 1. a dimension instance of dimension schema
    private String diceLevel; // 2. a dice level variable
    private String diceNode; // 3. a dice node variable
    private Set<String> sliceConditions; // 4. a possibly empty set of slice conditions
    private String granularityLevel; // 5 . a granularity level variable

    /**
     * Instantiates a new instance of class {@linkplain DimensionQualification}.
     */
    public DimensionQualification() {
        this.dimension = null;
        this.sliceConditions = new HashSet<>();
        this.granularityLevel = "top"; // TODO: is this the right value for top, or does it have an URI?
        this.diceLevel = "top";  // TODO: is this the right value for top, or does it have an URI?
        this.diceNode = "all";  // TODO: is this the right value for all, or does it have an URI?
    }

    /**
     * Instantiates a new instance of class {@linkplain DimensionQualification}.
     *
     * @param dimension The URI of the dimension.
     */
    public DimensionQualification(String dimension) {
        this();
        this.dimension = dimension;
    }

    /**
     * Gets the dimension.
     *
     * @return the dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * Sets the dimension.
     *
     * @param dimension the dimension
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    /**
     * Gets the dice level.
     *
     * @return the dice level
     */
    public String getDiceLevel() {
        return diceLevel;
    }

    /**
     * Sets the dice level.
     *
     * @param diceLevel the dice level
     */
    public void setDiceLevel(String diceLevel) {
        this.diceLevel = diceLevel;
    }

    /**
     * Gets the dice node.
     *
     * @return the dice node
     */
    public String getDiceNode() {
        return diceNode;
    }

    /**
     * Sets the dice node.
     *
     * @param diceNode the dice node
     */
    public void setDiceNode(String diceNode) {
        this.diceNode = diceNode;
    }

    /**
     * Gets a unmodifiable copy of the slice conditions (may be empty).
     *
     * @return the slice conditions
     */
    public Set<String> getSliceConditions() {
        return Collections.unmodifiableSet(sliceConditions);
    }

    /**
     * Sets the slice conditions.
     *
     * @param sliceConditions the slice conditions
     */
    public void setSliceConditions(Set<String> sliceConditions) {
        this.sliceConditions = Objects.requireNonNullElseGet(sliceConditions, HashSet::new);
    }

    /**
     * Adds the specified slice condition if it is not already present.
     *
     * @param cond The slice condition to be added to the set.
     * @return {@code true} if this set did not already contain the specified element
     * @see Set#add(Object)
     */
    public boolean addSliceCondition(String cond) {
        return sliceConditions.add(cond);
    }

    /**
     * Removes the specified slice condition if it is present.
     *
     * @param cond The slice condition to be removed from the set, if present.
     * @return {@code true} if the set contained the specified condition
     * @see Set#remove(Object)
     */
    public boolean removeSliceCondition(String cond) {
        return sliceConditions.remove(cond);
    }

    /**
     * Gets the granularity level.
     *
     * @return the granularity level
     */
    public String getGranularityLevel() {
        return granularityLevel;
    }

    /**
     * Sets the granularity level.
     *
     * @param granularityLevel the granularity level
     */
    public void setGranularityLevel(String granularityLevel) {
        this.granularityLevel = granularityLevel;
    }

    /**
     * Returns whether the current dimension qualification is filled.
     * This means that all necessary fields are set.
     *
     * @return {@code true}, if all necessary fields of the dimension qualification are filled.
     */
    public boolean isFilled() {
        return StringUtils.isNotBlank(dimension) &&
                StringUtils.isNotBlank(diceLevel) &&
                StringUtils.isNotBlank(diceNode) &&
                StringUtils.isNotBlank(granularityLevel);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DimensionQualification.class.getSimpleName() + "[", "]")
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
        DimensionQualification that = (DimensionQualification) o;
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
    public int compareTo(DimensionQualification other) {
        return StringUtils.compare(dimension, other.dimension);
    }
}
