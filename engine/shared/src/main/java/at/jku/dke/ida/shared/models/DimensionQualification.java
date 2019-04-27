package at.jku.dke.ida.shared.models.iris;

import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.models.generic.GenericDimensionQualification;

import java.util.Objects;

/**
 * Represents a dimension qualification.
 */
public class DimensionQualification extends GenericDimensionQualification<String> {

    /**
     * Instantiates a new instance of class {@linkplain GenericDimensionQualification}.
     */
    public DimensionQualification() {
        this.setGranularityLevel(IRIConstants.RESOURCE_TOP_LEVEL);
        this.setDiceLevel(IRIConstants.RESOURCE_TOP_LEVEL);
        this.setDiceNode(IRIConstants.RESOURCE_ALL_NODES);
    }

    /**
     * Sets the dice level.
     *
     * @param diceLevel the dice level
     */
    @Override
    public void setDiceLevel(String diceLevel) {
        super.setDiceLevel(Objects.requireNonNullElse(diceLevel, IRIConstants.RESOURCE_TOP_LEVEL));
    }

    /**
     * Sets the dice node.
     *
     * @param diceNode the dice node
     */
    @Override
    public void setDiceNode(String diceNode) {
        super.setDiceNode(Objects.requireNonNullElse(diceNode, IRIConstants.RESOURCE_ALL_NODES));
    }

    /**
     * Sets the granularity level.
     *
     * @param granularityLevel the granularity level
     */
    @Override
    public void setGranularityLevel(String granularityLevel) {
        super.setGranularityLevel(Objects.requireNonNullElse(granularityLevel, IRIConstants.RESOURCE_TOP_LEVEL));
    }

    /**
     * Returns whether the current dimension qualification is filled.
     * This means that all necessary fields are set.
     *
     * @return {@code true}, if all necessary fields of the dimension qualification are filled.
     */
    @Override
    public boolean isFilled() {
        if (!super.isFilled()) return false;
        return !getDimension().isBlank() &&
                !getDiceLevel().isBlank() &&
                !getDiceNode().isBlank() &&
                !getGranularityLevel().isBlank();
    }

}
