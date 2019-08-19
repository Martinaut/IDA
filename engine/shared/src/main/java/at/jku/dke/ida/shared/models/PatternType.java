package at.jku.dke.ida.shared.models;

/**
 * Represents the possible comparison patterns.
 */
public enum PatternType {
    /**
     * Sets have same grouping properties and same measures.
     * They have common selection criteria, but SC has additional selection criteria.
     */
    SetToBase,

    /**
     * SI and SC share all elements except the slice and dice conditions.
     */
    HomogeneousIndependentSet,

    /**
     * Different Facts, independently defined
     */
    HeterogeneousIndependentSet
}
