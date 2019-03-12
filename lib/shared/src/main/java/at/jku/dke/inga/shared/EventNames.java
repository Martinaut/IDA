package at.jku.dke.inga.shared;

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
     * Triggers moving up to a measure.
     */
    public static final String NAVIGATE_MEASURE_MOVE_UP = "navigate.moveUpToMeasure";

    /**
     * Triggers moving down to a measure.
     */
    public static final String NAVIGATE_MEASURE_MOVE_DOWN = "navigate.moveDownToMeasure";

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
