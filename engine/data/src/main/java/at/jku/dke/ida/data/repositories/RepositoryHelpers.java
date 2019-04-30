package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.models.*;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;

/**
 * This class contains some helper methods.
 */
public final class RepositoryHelpers {
    /**
     * Prevents creation of instances of this class.
     */
    private RepositoryHelpers() {
    }

    /**
     * Converts a binding set to a dimension label.
     * The binding set must contain: element, label and description.
     *
     * @param lang       The language.
     * @param bindingSet The binding set.
     * @return Converted Label
     */
    public static Label convert(String lang, BindingSet bindingSet) {
        return new Label(
                bindingSet.getValue("element").stringValue(),
                lang,
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a dimension label.
     * The binding set must contain: dimension, dimensionLabel, element, label and description.
     *
     * @param lang       The language.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    public static DimensionLabel convertToDimensionLabel(String lang, BindingSet bindingSet) {
        return new DimensionLabel(
                lang,
                bindingSet.getValue("dimension").stringValue(),
                bindingSet.getValue("dimensionLabel").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a dimension level label.
     * The binding set must contain: dimension, dimensionLabel, level, levelLabel element, label and description.
     *
     * @param lang       The language.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    public static DimensionLevelLabel convertToLevelLabel(String lang, BindingSet bindingSet) {
        return new DimensionLevelLabel(
                lang,
                bindingSet.getValue("dimension").stringValue(),
                bindingSet.getValue("dimensionLabel").stringValue(),
                bindingSet.getValue("level").stringValue(),
                bindingSet.getValue("levelLabel").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a dimension label.
     * The binding set must contain: cube, element, type and score.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Dimension Similarity
     */
    public static CubeSimilarity convertToSimilarity(String term, BindingSet bindingSet) {
        return new CubeSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue()
        );
    }

    /**
     * Converts a binding set to a dimension label.
     * The binding set must contain: cube, element, type, score and dimension.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Dimension Similarity
     */
    public static DimensionSimilarity convertToDimSimilarity(String term, BindingSet bindingSet) {
        return new DimensionSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue(),
                bindingSet.getValue("dimension").stringValue()
        );
    }
}
