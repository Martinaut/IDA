package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.helpers.Keyword;
import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.models.DimensionLevelLabel;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.SimpleDisplayable;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.session.SessionModel;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains some helper-methods used in interceptors.
 */
final class InterceptionHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private InterceptionHelper() {
    }

    // region --- Operations ---

    /**
     * Calculates the string similarity for the operations.
     *
     * @param currentState       The current state.
     * @param sessionModel       The session model.
     * @param locale             The locale.
     * @param possibleOperations The list with possible operations.
     * @return List with operation similarities
     */
    static Set<Similarity<Displayable>> computeOperationStringSimilarities(String currentState, SessionModel sessionModel, Locale locale, Collection<Operation> possibleOperations) {
        return computeOperationStringSimilarities(currentState, sessionModel, locale, possibleOperations, true);
    }

    /**
     * Calculates the string similarity for the operations.
     *
     * @param currentState       The current state.
     * @param sessionModel       The session model.
     * @param locale             The locale.
     * @param possibleOperations The list with possible operations.
     * @param withNumbered       Flag, whether to add numbered operations to the possible operations
     * @return List with operation similarities
     */
    static Set<Similarity<Displayable>> computeOperationStringSimilarities(String currentState, SessionModel sessionModel, Locale locale, Collection<Operation> possibleOperations, boolean withNumbered) {
        // Add numbered operations
        Collection<Operation> coll = new ArrayList<>(possibleOperations);
        if (withNumbered)
            coll.addAll(createNumberedOperations(locale, possibleOperations));

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                currentState,
                sessionModel,
                coll.stream().map(x -> (Displayable) x).collect(Collectors.toList())
        );

        return new StringSimilarityService().executeRules(model);
    }

    /**
     * Creates a list of numbered operations.
     * <p>
     * The list creates operations with labels
     * <ul>
     * <li>Option one, Option two, ... and first option, second option, ...
     * <li>first option, second option, ...
     * <li>first, second, ...
     *
     * @param locale             The locale.
     * @param possibleOperations The list with possible operations.
     * @return List with possible operations as numbered labels
     */
    private static Collection<Operation> createNumberedOperations(Locale locale, Collection<Operation> possibleOperations) {
        final String optionText = Keyword.OPTION.getKeywordText(locale);
        List<Operation> numbered = new ArrayList<>();

        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        for (Operation op : possibleOperations) {
            numbered.add(new Operation(op.getEvent(), optionText + ' ' + nf.format(i++), op.getPosition()));
        }

        i = 1;
        for (Operation op : possibleOperations) {
            numbered.add(new Operation(op.getEvent(), nf.format(i++, "%spellout-ordinal") + ' ' + optionText, op.getPosition()));
        }

        i = 1;
        for (Operation op : possibleOperations) {
            numbered.add(new Operation(op.getEvent(), nf.format(i++, "%spellout-ordinal"), op.getPosition()));
        }

        i = 1;
        for (Operation op : possibleOperations) {
            numbered.add(new Operation(op.getEvent(), optionText + ' ' + (i++), op.getPosition()));
        }

        return numbered;
    }
    // endregion

    // region --- Value ---

    /**
     * Calculates the string similarity for the values.
     *
     * @param currentState   The current state.
     * @param sessionModel   The session model.
     * @param locale         The locale.
     * @param possibleValues The list with possible values.
     * @return List with value similarities
     */
    static Set<Similarity<Displayable>> computeValueStringSimilarities(String currentState, SessionModel sessionModel,
                                                                       Locale locale, Collection<Displayable> possibleValues) {
        // Add numbered operations
        possibleValues.addAll(createNumberedValues(locale, possibleValues));

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                currentState,
                sessionModel,
                possibleValues
        );

        return new StringSimilarityService().executeRules(model);
    }

    /**
     * Calculates the string similarity for the values.
     *
     * @param currentState The current state.
     * @param sessionModel The session model.
     * @param locale       The locale.
     * @param leftValues   The list with possible values on the left side.
     * @param rightValues  The list with possible values on the right side.
     * @return List with value similarities
     */
    static Set<Similarity<Displayable>> computeValueStringSimilarities(String currentState, SessionModel sessionModel, Locale locale,
                                                                       Collection<Displayable> leftValues, Collection<Displayable> rightValues) {
        Collection<Displayable> possibleValues = new HashSet<>();
        possibleValues.addAll(leftValues);
        possibleValues.addAll(rightValues);

        // Add numbered operations
        possibleValues.addAll(createNumberedValues(locale, leftValues, Keyword.LEFT));
        possibleValues.addAll(createNumberedValues(locale, rightValues, Keyword.RIGHT));

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                currentState,
                sessionModel,
                possibleValues
        );

        return new StringSimilarityService().executeRules(model);
    }

    /**
     * Creates a list of numbered operations.
     * <p>
     * <ul>
     * <li>Option one, Option two, ... and first option, second option, ...
     * <li>first option, second option, ...
     * <li>first, second, ...
     *
     * @param locale         The locale.
     * @param possibleValues The list with possible values.
     * @return List with possible values as numbered labels
     */
    private static Collection<Displayable> createNumberedValues(Locale locale, Collection<Displayable> possibleValues) {
        return createNumberedValues(locale, possibleValues, null);
    }

    /**
     * Creates a list of numbered operations.
     * <p>
     * <ul>
     * <li>Option one, Option two, ... and first option, second option, ...
     * <li>first option, second option, ...
     * <li>first, second, ...
     *
     * @param locale         The locale.
     * @param possibleValues The list with possible values.
     * @return List with possible values as numbered labels
     */
    private static Collection<Displayable> createNumberedValues(Locale locale, Collection<Displayable> possibleValues, Keyword prefixResourceName) {
        String optionText = Keyword.OPTION.getKeywordText(locale);
        String prefix = "";
        if (prefixResourceName != null) {
            prefix = prefixResourceName.getKeywordText(locale);
            optionText = prefix + ' ' + optionText;
            prefix = ' ' + prefix;
        }

        List<Displayable> numbered = new ArrayList<>();

        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        for (Displayable op : possibleValues) {
            numbered.add(clone(op, optionText + ' ' + nf.format(i++)));
        }

        i = 1;
        for (Displayable op : possibleValues) {
            numbered.add(clone(op, nf.format(i++, "%spellout-ordinal") + ' ' + optionText));
        }

        i = 1;
        for (Displayable op : possibleValues) {
            numbered.add(clone(op, nf.format(i++, "%spellout-ordinal") + prefix));
        }

        i = 1;
        for (Displayable op : possibleValues) {
            numbered.add(clone(op, optionText + ' ' + (i++)));
        }

        return numbered;
    }
    // endregion

    private static Displayable clone(Displayable value, String label) {
        if (value instanceof DimensionLevelLabel) {
            DimensionLevelLabel lbl = (DimensionLevelLabel) value;
            return new DimensionLevelLabel(lbl.getLang(), lbl.getDimensionUri(), lbl.getDimensionLabel(), lbl.getLevelUri(), lbl.getLevelLabel(), lbl.getUri(), label, lbl.getDescription());
        }
        if (value instanceof DimensionLabel) {
            DimensionLabel lbl = (DimensionLabel) value;
            return new DimensionLabel(lbl.getLang(), lbl.getDimensionUri(), lbl.getDimensionLabel(), lbl.getUri(), label, lbl.getDescription());
        }
        if (value instanceof Label) {
            Label lbl = (Label) value;
            return new Label(lbl.getUri(), lbl.getLang(), label, lbl.getDescription());
        }
        return new SimpleDisplayable(value.getDisplayableId(), label, value.getDetails());
    }
}