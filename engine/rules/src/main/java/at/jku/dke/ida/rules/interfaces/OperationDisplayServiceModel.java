package at.jku.dke.ida.rules.interfaces;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.OperationDisplayService}.
 */
public interface OperationDisplayServiceModel extends ServiceModel {

    /**
     * Returns whether there are measures in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are measures available for selection; {@code false} otherwise.
     */
    boolean areNotSelectedMeasuresAvailable();

    /**
     * Returns whether there are filter conditions conditions in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are filter conditions available for selection; {@code false} otherwise.
     */
    boolean areNotSelectedFilterConditionsAvailable();  // TODO (with relationships?)

    /**
     * Returns whether there are base measure conditions in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are base measure conditions available for selection; {@code false} otherwise.
     */
    boolean areNotSelectedBaseMeasureConditionsAvailable(); // TODO (with relationships?)

    // TODO: Dice Node (with relationships?)
    // TODO: Slice Conditions (with relationships?)
    // TODO: Granularity Levels
}
