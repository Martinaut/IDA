package at.jku.dke.ida.shared;

/**
 * This class contains constants for all event names.
 */
public final class EventNames {

    /**
     * Used for transitions with the target state "Finished".
     */
    public static final String EXIT = "exit";

    /**
     * Used when the user gave an input.
     */
    public static final String USER_INPUT = "userInput";

    // region --- NAVIGATE ---
    /**
     * Used to trigger a navigation-step. This event can have multiple sub-events.
     */
    public static final String NAVIGATE = "navigate";

    /**
     * Triggers cube selection.
     */
    public static final String NAVIGATE_CUBE_SELECT = "navigate.selectCube";

    // region --- MEASURE ---
    /**
     * Triggers adding a measure.
     */
    public static final String NAVIGATE_MEASURE_ADD = "navigate.addMeasure";

    /**
     * Triggers refocusing a measure.
     */
    public static final String NAVIGATE_MEASURE_REFOCUS = "navigate.refocusMeasure";

    /**
     * Triggers dropping a measure.
     */
    public static final String NAVIGATE_MEASURE_DROP = "navigate.dropMeasure";
    // endregion
    // region --- GRANULARITY LEVEL ---
    /**
     * Triggers a drill down operation.
     */
    public static final String NAVIGATE_GL_DRILL_DOWN = "navigate.drillDown";

    /**
     * Triggers a roll-up operation
     */
    public static final String NAVIGATE_GL_ROLL_UP = "navigate.rollUp";
    // endregion
    // region --- DICE NODE ---
    /**
     * Triggers adding a dice node.
     */
    public static final String NAVIGATE_DICE_NODE_MOVE_UP = "navigate.moveUpDiceNode";

    /**
     * Triggers removing a dice node.
     */
    public static final String NAVIGATE_DICE_NODE_MOVE_DOWN = "navigate.moveDownDiceNode";
    // endregion
    // region --- SLICE CONDITION ---

    /**
     * Triggers adding a slice condition.
     */
    public static final String NAVIGATE_SLICE_CONDITION_NARROW = "navigate.narrowSliceCondition";

    /**
     * Triggers removing a slice condition.
     */
    public static final String NAVIGATE_SLICE_CONDITION_BROADEN = "navigate.broadenSliceCondition";

    /**
     * Triggers replacing a slice condition.
     */
    public static final String NAVIGATE_SLICE_CONDITION_REFOCUS = "navigate.refocusSliceCondition";
    // endregion
    // region --- FILTER ---
    /**
     * Triggers adding a filter.
     */
    public static final String NAVIGATE_FILTER_NARROW = "navigate.narrowFilter";

    /**
     * Triggers removing a filter.
     */
    public static final String NAVIGATE_FILTER_BROADEN = "navigate.broadenFilter";

    /**
     * Triggers replacing a filter.
     */
    public static final String NAVIGATE_FILTER_REFOCUS = "navigate.refocusFilter";
    // endregion
    // region --- BASE MEASURE CONDITION ---
    /**
     * Triggers adding a base measure condition.
     */
    public static final String NAVIGATE_BMC_NARROW = "navigate.narrowBaseMeasureCondition";

    /**
     * Triggers removing a base measure condition.
     */
    public static final String NAVIGATE_BMC_BROADEN = "navigate.broadenBaseMeasureCondition";

    /**
     * Triggers replacing a base measure condition.
     */
    public static final String NAVIGATE_BMC_REFOCUS = "navigate.refocusBaseMeasureCondition";
    // endregion
    // endregion

    // region --- QUERY ---
    /**
     * Used for transitions from "Executable" to "Executed".
     */
    public static final String EXECUTE_QUERY = "executeQuery";

    /**
     * Used for transitions from "Executed" to "Executable".
     */
    public static final String REVISE_QUERY = "reviseQuery";
    // endregion

    /**
     * Used to request more information within a step.
     */
    public static final String MORE_INFORMATION = "moreInformation";

    /**
     * Used to specify that the user-input cannot be resolved.
     */
    public static final String INVALID_INPUT = "invalidInput";

    /**
     * Used for transitions from "Determining*" to the next state.
     */
    public static final String DETERMINED = "determined";

    /**
     * Prevents generating a instance of this class.
     */
    private EventNames() {
    }
}
