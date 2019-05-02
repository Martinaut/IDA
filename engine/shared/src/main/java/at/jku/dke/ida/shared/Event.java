package at.jku.dke.ida.shared;

import java.util.Locale;
import java.util.Objects;

/**
 * This enum contains all possible events.
 */
public enum Event {

    /**
     * Used for transitions with the target state "Finished".
     */
    EXIT("exit"),

    /**
     * Used to abort the changing state and go back to executable state.
     */
    ABORT("abort"),

    /**
     * Used when the user gave an input.
     */
    USER_INPUT("userInput"),

    /**
     * Used to request more information within a step.
     */
    MORE_INFORMATION("moreInformation"),

    /**
     * Used to specify that the user-input cannot be resolved.
     */
    INVALID_INPUT("invalidInput"),

    /**
     * Used for transitions from "Determining*" to the next state.
     */
    DETERMINED("determined"),

    // region --- QUERY ---
    /**
     * Used for transitions from "Executable" to "Executed".
     */
    EXECUTE_QUERY("executeQuery"),

    /**
     * Used for transitions from "Executed" to "Executable".
     */
    REVISE_QUERY("reviseQuery"),
    // endregion

    // region --- NAVIGATE ---
    /**
     * Used to trigger a navigation-step. This event has multiple sub-events.
     */
    NAVIGATE("navigate"),

    /**
     * Triggers cube selection.
     */
    NAVIGATE_CUBE_SELECT("navigate.selectCube"),

    // region --- MEASURE ---
    /**
     * Triggers adding a measure.
     */
    NAVIGATE_MEASURE_ADD("navigate.addMeasure"),

    /**
     * Triggers refocusing a measure.
     */
    NAVIGATE_MEASURE_REFOCUS("navigate.refocusMeasure"),

    /**
     * Triggers dropping a measure.
     */
    NAVIGATE_MEASURE_DROP("navigate.dropMeasure"),
    // endregion
    // region --- GRANULARITY LEVEL ---
    /**
     * Triggers a drill down operation.
     */
    NAVIGATE_GL_DRILL_DOWN("navigate.drillDown"),

    /**
     * Triggers a roll-up operation
     */
    NAVIGATE_GL_ROLL_UP("navigate.rollUp"),
    // endregion
    // region --- DICE NODE ---
    /**
     * Triggers adding a dice node.
     */
    NAVIGATE_DICE_NODE_MOVE_UP("navigate.moveUpDiceNode"),

    /**
     * Triggers removing a dice node.
     */
    NAVIGATE_DICE_NODE_MOVE_DOWN("navigate.moveDownDiceNode"),
    // endregion
    // region --- SLICE CONDITION ---
    /**
     * Triggers adding a slice condition.
     */
    NAVIGATE_SLICE_CONDITION_NARROW("navigate.narrowSliceCondition"),

    /**
     * Triggers removing a slice condition.
     */
    NAVIGATE_SLICE_CONDITION_BROADEN("navigate.broadenSliceCondition"),

    /**
     * Triggers replacing a slice condition.
     */
    NAVIGATE_SLICE_CONDITION_REFOCUS("navigate.refocusSliceCondition"),
    // endregion
    // region --- FILTER ---
    /**
     * Triggers adding a filter.
     */
    NAVIGATE_FILTER_NARROW("navigate.narrowFilter"),

    /**
     * Triggers removing a filter.
     */
    NAVIGATE_FILTER_BROADEN("navigate.broadenFilter"),

    /**
     * Triggers replacing a filter.
     */
    NAVIGATE_FILTER_REFOCUS("navigate.refocusFilter"),
    // endregion
    // region --- BASE MEASURE CONDITION ---
    /**
     * Triggers adding a base measure condition.
     */
    NAVIGATE_BMC_NARROW("navigate.narrowBaseMeasureCondition"),

    /**
     * Triggers removing a base measure condition.
     */
    NAVIGATE_BMC_BROADEN("navigate.broadenBaseMeasureCondition"),

    /**
     * Triggers replacing a base measure condition.
     */
    NAVIGATE_BMC_REFOCUS("navigate.refocusBaseMeasureCondition");
    // endregion
    // endregion

    private String eventName;

    Event(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Get the synonyms of the event.
     *
     * @param locale The locale.
     * @return The synonyms.
     */
    public String[] getSynonyms(Locale locale) {
        locale = Objects.requireNonNullElse(locale, Locale.getDefault());
        String keywords = ResourceBundleHelper.getResourceString(BUNDLE_NAME, locale, this.getEventName());
        return keywords == null || keywords.isBlank() ? new String[0] : keywords.split(",");
    }

    // region --- STATIC ---
    private static String BUNDLE_NAME = "shared.Synonyms";

    /**
     * Overrides the default bundle name ({@code shared.Synonyms}) for operation synonyms.
     *
     * @param bundleName The bundle base name to use.
     */
    public static void setBundleName(String bundleName) {
        BUNDLE_NAME = bundleName;
    }
    // endregion
}