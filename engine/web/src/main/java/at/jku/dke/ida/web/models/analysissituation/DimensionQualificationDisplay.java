package at.jku.dke.ida.web.models.analysissituation;

import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.models.generic.GenericDimensionQualification;

import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Used to display the dimension qualification.
 */
public class DimensionQualificationDisplay extends GenericDimensionQualification<Label> {

    private final Label lblTop;
    private final Label lblAll;

    /**
     * Instantiates a new instance of class {@linkplain DimensionQualificationDisplay}.
     *
     * @param lang The language.
     */
    public DimensionQualificationDisplay(String lang) {
        super();
        this.lblTop = new Label(IRIConstants.RESOURCE_TOP_LEVEL, lang,
                ResourceBundleHelper.getResourceString("web.ResourceNames", lang == null ? Locale.getDefault() : Locale.forLanguageTag(lang), "Top"));
        this.lblAll = new Label(IRIConstants.RESOURCE_ALL_NODES, lang,
                ResourceBundleHelper.getResourceString("web.ResourceNames", lang == null ? Locale.getDefault() : Locale.forLanguageTag(lang), "All"));

        this.setGranularityLevel(this.lblTop);
        this.setDiceLevel(this.lblTop);
        this.setDiceNode(this.lblAll);
        this.setSliceConditions(new TreeSet<>());
    }

    /**
     * Instantiates a new instance of class {@linkplain DimensionQualificationDisplay}.
     *
     * @param lang      The language.
     * @param dimension The URI of the dimension.
     */
    public DimensionQualificationDisplay(String lang, Label dimension) {
        super(dimension);
        this.lblTop = new Label(IRIConstants.RESOURCE_TOP_LEVEL, lang,
                ResourceBundleHelper.getResourceString("web.ResourceNames", lang == null ? Locale.getDefault() : Locale.forLanguageTag(lang), "Top"));
        this.lblAll = new Label(IRIConstants.RESOURCE_ALL_NODES, lang,
                ResourceBundleHelper.getResourceString("web.ResourceNames", lang == null ? Locale.getDefault() : Locale.forLanguageTag(lang), "All"));

        this.setGranularityLevel(this.lblTop);
        this.setDiceLevel(this.lblTop);
        this.setDiceNode(this.lblAll);
        this.setSliceConditions(new TreeSet<>());
    }

    /**
     * Sets the dice level.
     *
     * @param diceLevel the dice level
     */
    @Override
    public void setDiceLevel(Label diceLevel) {
        if (diceLevel != null && (lblTop.getUri().equals(diceLevel.getUri()) || lblTop.getUri().equals(diceLevel.getLabel())))
            super.setDiceLevel(lblTop);
        else
            super.setDiceLevel(Objects.requireNonNullElse(diceLevel, lblTop));
    }

    /**
     * Sets the dice node.
     *
     * @param diceNode the dice node
     */
    @Override
    public void setDiceNode(Label diceNode) {
        if (diceNode != null && (lblAll.getUri().equals(diceNode.getUri()) || lblAll.getUri().equals(diceNode.getLabel())))
            super.setDiceNode(lblAll);
        else
            super.setDiceNode(Objects.requireNonNullElse(diceNode, lblAll));
    }

    /**
     * Sets the granularity level.
     *
     * @param granularityLevel the granularity level
     */
    @Override
    public void setGranularityLevel(Label granularityLevel) {
        if (granularityLevel != null && (lblTop.getUri().equals(granularityLevel.getUri()) || lblTop.getUri().equals(granularityLevel.getLabel())))
            super.setGranularityLevel(lblTop);
        else
            super.setGranularityLevel(Objects.requireNonNullElse(granularityLevel, lblTop));
    }

    @Override
    public int compareTo(GenericDimensionQualification<Label> other) {
        return getDimension().getUri().compareTo(other.getDimension().getUri());
    }
}
