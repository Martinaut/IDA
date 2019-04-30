package at.jku.dke.ida.rules.interfaces;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.OperationDisplayService}.
 */
public interface OperationDisplayServiceModel extends ServiceModel {

    // region --- MEASURE ---

    /**
     * Returns whether there are measures in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are measures available for selection; {@code false} otherwise.
     */
    boolean isNotSelectedMeasureAvailable();
    // endregion

    // region --- GRANULARITY LEVEL ---

    /**
     * Returns whether at least one drill down relationship is available in at least one dimension.
     *
     * @return {@code true} if at least one drill down relationship is available based on the current state; {@code false} otherwise.
     */
    boolean isDrillDownRelationshipAvailable();

    /**
     * Returns whether at least one roll up relationship is available in at least one dimension.
     *
     * @return {@code true} if at least one roll up relationship is available based on the current state; {@code false} otherwise.
     */
    boolean isRollUpRelationshipAvailable();
    // endregion

    // region --- DICE NODE ---

    /**
     * Returns whether in at least one dimension there is a sub-level relationship available for the current dice level.
     *
     * @return {@code true} if a sub-level for the current dice level is available; {@code false} otherwise.
     */
    boolean isSubLevelRelationshipForDiceLevelAvailable();

    /**
     * Returns whether in at least one dimension there is a parent-level relationship available for the current dice level.
     *
     * @return {@code true} if a parent-level for the current dice level is available; {@code false} otherwise.
     */
    boolean isParentLevelRelationshipForDiceLevelAvailable();
    // endregion

    // region --- SLICE CONDITION ---

    /**
     * Returns whether in at least one dimension there is an imply relationship available for a slice condition.
     *
     * @return {@code true} if a imply relationship is available; {@code false} otherwise.
     */
    boolean isImplyRelationshipForSliceConditionAvailable();

    /**
     * Returns whether in at least one dimension there is a subusme relationship available for a slice condition.
     *
     * @return {@code true} if a subsume relationship available; {@code false} otherwise.
     */
    boolean isSubsumeRelationshipForSliceConditionAvailable();

    /**
     * Returns whether there exists at least one dimension with at least on selected slice condition and at least one
     * not selected slice condition.
     *
     * @return {@code true} if a replacement of slice conditions is possible; {@code false} otherwise.
     */
    boolean isDimensionWithSelectedAndNotSelectedSliceConditionsAvailable();
    // endregion

    // region --- BASE MEASURE CONDITION ---
    /**
     * Returns whether there are base measure conditions in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are base measure conditions available for selection; {@code false} otherwise.
     */
    boolean isNotSelectedBaseMeasureConditionAvailable();
    // endregion

    // region --- FILTER ---
    /**
     * Returns whether there are filter conditions conditions in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are filter conditions available for selection; {@code false} otherwise.
     */
    boolean isNotSelectedFilterConditionAvailable();
    // endregion
}
