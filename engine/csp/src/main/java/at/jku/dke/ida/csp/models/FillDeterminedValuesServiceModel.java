package at.jku.dke.ida.csp.models;

import at.jku.dke.ida.csp.domain.AnalysisSituationEntity;
import at.jku.dke.ida.csp.service.FillDeterminedValuesService;
import at.jku.dke.ida.rules.models.AbstractServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Locale;

/**
 * Model used by {@link FillDeterminedValuesService}.
 */
public class FillDeterminedValuesServiceModel extends AbstractServiceModel {

    private final AnalysisSituationEntity determinedValues;

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public FillDeterminedValuesServiceModel(String currentState, SessionModel sessionModel, AnalysisSituationEntity determinedValues) {
        super(currentState, sessionModel);
        this.determinedValues = determinedValues;
    }

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public FillDeterminedValuesServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel, AnalysisSituationEntity determinedValues) {
        super(currentState, locale, analysisSituation, operation, sessionModel);
        this.determinedValues = determinedValues;
    }

    /**
     * Gets determined values.
     *
     * @return the determined values
     */
    public AnalysisSituationEntity getDeterminedValues() {
        return determinedValues;
    }
}
