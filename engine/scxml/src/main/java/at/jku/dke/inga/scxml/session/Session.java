package at.jku.dke.inga.scxml.session;

import at.jku.dke.inga.scxml.StateMachineFactory;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.inga.shared.EventNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a specific session.
 */
public class Session {

    private static final Logger LOGGER = LogManager.getLogger(Session.class);

    private final String sessionId;
    private final SCXMLExecutor executor;
    private final SessionContextModel sessionContextModel;

    /**
     * Instantiates a new instance of class {@linkplain Session}.
     *
     * @param sessionId The session id.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     * @throws IllegalArgumentException           If the {@code sessionId} is {@code null} or empty or blank.
     */
    Session(String sessionId, String locale, DisplayListener listener, AnalysisSituationListener asListener) throws StateMachineInstantiationException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");
        this.sessionId = sessionId;
        this.executor = StateMachineFactory.create(sessionId);
        this.sessionContextModel = new SessionContextModel(sessionId, locale, listener, asListener);
    }

    /**
     * Gets the session id.
     *
     * @return the session id
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Gets the session context model.
     *
     * @return the session context model
     */
    public SessionContextModel getSessionContextModel() {
        return sessionContextModel;
    }

    /**
     * Returns whether the state chart is in its final state.
     *
     * @return {@code true} if in final state; {@code false} otherwise.
     */
    public boolean isInFinalState() {
        return executor.getCurrentStatus().isFinal();
    }

    /**
     * Trigger the user input event.
     *
     * @param userInput The user input.
     * @throws ModelException           If an error occurred while triggering an event.
     * @throws IllegalArgumentException If {@code userInput} is {@code null}.
     * @see EventNames#USER_INPUT
     */
    public void triggerUserInputEvent(String userInput) throws ModelException {
        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");

        LOGGER.info("Triggering user-input event.");
        sessionContextModel.setUserInput(userInput);
        executor.triggerEvent(new TriggerEvent(EventNames.USER_INPUT, TriggerEvent.SIGNAL_EVENT));
    }

    /**
     * Trigger the exit event.
     *
     * @throws ModelException If an error occurred while triggering an event.
     * @see EventNames#EXIT
     */
    void triggerExitEvent() throws ModelException {
        LOGGER.info("Triggering exit event.");
        executor.triggerEvent(new TriggerEvent(EventNames.EXIT, TriggerEvent.SIGNAL_EVENT));
        executor.detachInstance();
    }

    /**
     * Initiates the state machine.
     * This method can only be called once.
     *
     * @throws IllegalStateException If the state machine is already initiated.
     */
    void initiate() {
        if (executor.isRunning()) throw new IllegalStateException("State Machine already initiated.");

        LOGGER.info("Initiating state machine for session {}.", sessionId);

        try {
            executor.go();
        } catch (ModelException ex) {
            LOGGER.error("An error occurred in session " + sessionId + " while initiating the state chart.", ex);
        }
    }
}
