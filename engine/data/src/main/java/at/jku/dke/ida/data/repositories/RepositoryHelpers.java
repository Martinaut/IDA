package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.models.DimensionLevelLabel;
import org.eclipse.rdf4j.query.BindingSet;

/**
 * This class contains some helper methods.
 */
final class RepositoryHelpers {
    /**
     * Prevents creation of instances of this class.
     */
    private RepositoryHelpers() {
    }

    /**
     * Converts a binding set to a dimension label.
     * The binding set must contain: dimension, dimensionLabel, element, label and description.
     *
     * @param lang       The language.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    static DimensionLabel convert(String lang, BindingSet bindingSet) {
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
    static DimensionLevelLabel convertToLevelLabel(String lang, BindingSet bindingSet) {
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
}