package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.models.labels.*;
import at.jku.dke.ida.data.models.similarity.*;
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
     * Converts a binding set to a cube label.
     * The binding set must contain: cube, element, label and description.
     *
     * @param lang       The language.
     * @param type       The type IRI.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    public static CubeLabel convertToCubeLabel(String lang, String type, BindingSet bindingSet) {
        return new CubeLabel(
                lang,
                bindingSet.hasBinding("cube") ? bindingSet.getValue("cube").stringValue() : null,
                type,
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a dimension label.
     * The binding set must contain: cube, dimension, dimensionLabel, element, label and description.
     *
     * @param lang       The language.
     * @param type       The type IRI.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    public static DimensionLabel convertToDimensionLabel(String lang, String type, BindingSet bindingSet) {
        return new DimensionLabel(
                lang,
                bindingSet.hasBinding("cube") ? bindingSet.getValue("cube").stringValue() : null,
                type,
                bindingSet.getValue("dimension").stringValue(),
                bindingSet.getValue("dimensionLabel").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a dimension level label.
     * The binding set must contain: cube, dimension, dimensionLabel, level, levelLabel element, label and description.
     *
     * @param lang       The language.
     * @param type       The type IRI.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    static DimensionLevelLabel convertToLevelLabel(String lang, String type, BindingSet bindingSet) {
        return new DimensionLevelLabel(
                lang,
                bindingSet.hasBinding("cube") ? bindingSet.getValue("cube").stringValue() : null,
                type,
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
     * Converts a binding set to a comparative label.
     * The binding set must contain: cube, part, element, label and description.
     *
     * @param lang       The language.
     * @param type       The type IRI.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    public static ComparativeLabel convertToComparativeLabel(String lang, String type, BindingSet bindingSet) {
        return new ComparativeLabel(
                lang,
                bindingSet.hasBinding("cube") ? bindingSet.getValue("cube").stringValue() : null,
                type,
                bindingSet.hasBinding("part") ? bindingSet.getValue("part").stringValue() : null,
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a comparative measure label.
     * The binding set must contain: cube, part, element, label, measure and description.
     *
     * @param lang       The language.
     * @param type       The type IRI.
     * @param bindingSet The binding set.
     * @return Converted Dimension Label
     */
    public static ComparativeMeasureLabel convertToComparativeMeasureLabel(String lang, String type, BindingSet bindingSet) {
        return new ComparativeMeasureLabel(
                lang,
                bindingSet.hasBinding("cube") ? bindingSet.getValue("cube").stringValue() : null,
                type,
                bindingSet.hasBinding("part") ? bindingSet.getValue("part").stringValue() : null,
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("label").stringValue(),
                bindingSet.hasBinding("description") ? bindingSet.getValue("description").stringValue() : null,
                bindingSet.hasBinding("measure") ? bindingSet.getValue("measure").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a dimension similarity.
     * The binding set must contain: cube, element, type and score.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Dimension Similarity
     */
    static CubeSimilarity convertToSimilarity(String term, BindingSet bindingSet) {
        return new CubeSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue()
        );
    }

    /**
     * Converts a binding set to a dimension similarity.
     * The binding set must contain: cube, element, type, score and dimension.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Dimension Similarity
     */
    static DimensionSimilarity convertToDimSimilarity(String term, BindingSet bindingSet) {
        return new DimensionSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue(),
                bindingSet.getValue("dimension").stringValue()
        );
    }

    /**
     * Converts a binding set to a level similarity.
     * The binding set must contain: cube, element, type, score, level and dimension.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Level Similarity
     */
    static LevelSimilarity convertToLevelSimilarity(String term, BindingSet bindingSet) {
        return new LevelSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue(),
                bindingSet.getValue("dimension").stringValue(),
                bindingSet.getValue("level").stringValue()
        );
    }

    /**
     * Converts a binding set to a comparative similarity.
     * The binding set must contain: cube, element, type, score and part.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Comparative Similarity
     */
    static ComparativeSimilarity convertToComparativeSimilarity(String term, BindingSet bindingSet) {
        return new ComparativeSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue(),
                bindingSet.hasBinding("part") ? bindingSet.getValue("part").stringValue() : null
        );
    }

    /**
     * Converts a binding set to a comparative measure similarity.
     * The binding set must contain: cube, element, type, score, measure and part.
     *
     * @param term       The term.
     * @param bindingSet The binding set.
     * @return Converted Comparative Measure Similarity
     */
    static ComparativeMeasureSimilarity convertToComparativeMeasureSimilarity(String term, BindingSet bindingSet) {
        return new ComparativeMeasureSimilarity(
                term,
                bindingSet.getValue("cube").stringValue(),
                bindingSet.getValue("element").stringValue(),
                bindingSet.getValue("type").stringValue(),
                ((Literal) bindingSet.getValue("score")).doubleValue(),
                bindingSet.hasBinding("part") ? bindingSet.getValue("part").stringValue() : null,
                bindingSet.hasBinding("measure") ? bindingSet.getValue("measure").stringValue() : null
        );
    }
}
