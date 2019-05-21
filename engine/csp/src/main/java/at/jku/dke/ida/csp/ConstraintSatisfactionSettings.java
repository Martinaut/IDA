package at.jku.dke.ida.csp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Contains configuration for constraint satisfaction problem building.
 */
@Component
@Primary
@ConfigurationProperties(prefix = "csp")
public class ConstraintSatisfactionSettings {

    private boolean useLevels;
    private boolean useLevelPredicates;
    private boolean useBaseMeasurePredicates;
    private boolean useAggregateMeasurePredicates;

    /**
     * Instantiates a new instance of class {@linkplain ConstraintSatisfactionSettings}.
     */
    public ConstraintSatisfactionSettings() {
        this.useLevels = true;
        this.useLevelPredicates = true;
        this.useBaseMeasurePredicates = false;
        this.useAggregateMeasurePredicates = false;
    }

    /**
     * Returns whether to add levels to the planning solution.
     *
     * @return {@code true}, if levels should be added; {@code false} otherwise.
     */
    public boolean isUseLevels() {
        return useLevels;
    }

    /**
     * Sets whether to add levels to the planning solution.
     *
     * @param useLevels {@code true}, if levels should be added; {@code false} otherwise.
     */
    public void setUseLevels(boolean useLevels) {
        this.useLevels = useLevels;
    }

    /**
     * Returns whether to add level predicates to the planning solution.
     *
     * @return {@code true}, if level predicates should be added; {@code false} otherwise.
     */
    public boolean isUseLevelPredicates() {
        return useLevelPredicates;
    }

    /**
     * Sets whether to add level predicates to the planning solution.
     *
     * @param useLevelPredicates {@code true}, if level predicates should be added; {@code false} otherwise.
     */
    public void setUseLevelPredicates(boolean useLevelPredicates) {
        this.useLevelPredicates = useLevelPredicates;
    }

    /**
     * Returns whether to add base measure predicates to the planning solution.
     *
     * @return {@code true}, if base measure predicates should be added; {@code false} otherwise.
     */
    public boolean isUseBaseMeasurePredicates() {
        return useBaseMeasurePredicates;
    }

    /**
     * Sets whether to add base measure predicates to the planning solution.
     *
     * @param useBaseMeasurePredicates {@code true}, if base measure predicates should be added; {@code false} otherwise.
     */
    public void setUseBaseMeasurePredicates(boolean useBaseMeasurePredicates) {
        this.useBaseMeasurePredicates = useBaseMeasurePredicates;
    }

    /**
     * Returns whether to add aggregate measure predicates to the planning solution.
     *
     * @return {@code true}, if aggregate measure predicates should be added; {@code false} otherwise.
     */
    public boolean isUseAggregateMeasurePredicates() {
        return useAggregateMeasurePredicates;
    }

    /**
     * Sets whether to add aggregate measure predicates to the planning solution.
     *
     * @param useAggregateMeasurePredicates {@code true}, if aggregate measure predicates should be added; {@code false} otherwise.
     */
    public void setUseAggregateMeasurePredicates(boolean useAggregateMeasurePredicates) {
        this.useAggregateMeasurePredicates = useAggregateMeasurePredicates;
    }
}
