package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.SwitchAnalysisSituationServiceModel;
import at.jku.dke.ida.shared.session.SessionModel;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.SwitchAnalysisSituationService}.
 */
public class DefaultSwitchAnalysisSituationServiceModel extends AbstractServiceModel implements SwitchAnalysisSituationServiceModel {
    /**
     * Instantiates a new instance of class {@link DefaultSwitchAnalysisSituationServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultSwitchAnalysisSituationServiceModel(String currentState, SessionModel sessionModel) {
        super(currentState, sessionModel);
    }
}