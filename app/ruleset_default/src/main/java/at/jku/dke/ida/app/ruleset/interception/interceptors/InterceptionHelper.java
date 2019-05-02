package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.Similarity;
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
        // Add numbered operations
        Collection<Operation> coll = new ArrayList<>(possibleOperations);
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
        final String optionText = ResourceBundleHelper.getResourceString("ruleset.Keywords", locale, "Option");
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

        return numbered;
    }

    /**
     * Calculates the string similarity for the values.
     *
     * @param currentState   The current state.
     * @param sessionModel   The session model.
     * @param locale         The locale.
     * @param possibleValues The list with possible values.
     * @return List with value similarities
     */
    static Set<Similarity<Displayable>> computeValueStringSimilarities(String currentState, SessionModel sessionModel, Locale locale, Collection<Displayable> possibleValues) {
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
        final String optionText = ResourceBundleHelper.getResourceString("ruleset.Keywords", locale, "Option");
        List<Displayable> numbered = new ArrayList<>();

        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        for (Displayable op : possibleValues) {
            numbered.add(new SimpleDisplayable(op.getDisplayableId(), optionText + ' ' + nf.format(i++), op.getDetails()));
        }

        i = 1;
        for (Displayable op : possibleValues) {
            numbered.add(new SimpleDisplayable(op.getDisplayableId(), nf.format(i++, "%spellout-ordinal") + ' ' + optionText, op.getDetails()));
        }

        i = 1;
        for (Displayable op : possibleValues) {
            numbered.add(new SimpleDisplayable(op.getDisplayableId(), nf.format(i++, "%spellout-ordinal"), op.getDetails()));
        }

        return numbered;
    }
}
