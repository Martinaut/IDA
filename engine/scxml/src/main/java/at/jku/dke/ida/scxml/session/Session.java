package at.jku.dke.ida.scxml.session;

import at.jku.dke.ida.scxml.StateMachineFactory;
import at.jku.dke.ida.scxml.events.AnalysisSituationListener;
import at.jku.dke.ida.scxml.events.DisplayListener;
import at.jku.dke.ida.scxml.events.QueryResultListener;
import at.jku.dke.ida.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.ida.shared.Event;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.Data;
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
     * @throws IllegalArgumentException           If the {@code sessionId} or {@code locale} is {@code null} or empty or blank.
     */
    Session(String sessionId, String locale, DisplayListener listener, AnalysisSituationListener asListener,
            QueryResultListener qrLsitener) throws StateMachineInstantiationException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");
        if (StringUtils.isBlank(locale)) throw new IllegalArgumentException("locale must not be null empty");

        this.sessionId = sessionId;
        this.executor = StateMachineFactory.create(sessionId);
        this.sessionContextModel = new SessionContextModel(sessionId, locale, listener, asListener, qrLsitener);
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
     * @see Event#USER_INPUT
     */
    public void triggerUserInputEvent(String userInput) throws ModelException {
        // if (userInput == null) throw new IllegalArgumentException("userInput must not be null");

        LOGGER.info("Triggering user-input event.");
        sessionContextModel.setUserInput(userInput);
        executor.triggerEvent(new TriggerEvent(Event.USER_INPUT.getEventName(), TriggerEvent.SIGNAL_EVENT));
    }

    /**
     * Trigger the revise query event.
     *
     * @throws ModelException           If an error occurred while triggering an event.
     * @throws IllegalArgumentException If {@code userInput} is {@code null}.
     * @see Event#REVISE_QUERY
     */
    public void triggerReviseQuery() throws ModelException {
        LOGGER.info("Triggering refine query event.");
        executor.triggerEvent(new TriggerEvent(Event.REVISE_QUERY.getEventName(), TriggerEvent.SIGNAL_EVENT));
    }

    /**
     * Sets a flag whether the cube is already selected.
     * <p>
     * This flag is only used once on initialization of the state machine. The default value is {@code false}.
     * Call this method before {@link #initiate()}.
     * Additionally this method sets {@link SessionContextModel#getOperation()} to {@code null} if {@code set} is {@code true};
     * to {@link Event#NAVIGATE_CUBE_SELECT} otherwise.
     *
     * @param set {@code true} if the cube is selected; {@code false} otherwise.
     */
    public void setCubeSetFlag(boolean set) {
        LOGGER.info("Setting cubeSelected flag to {}.", set);
        Data data = executor.getStateMachine().getDatamodel().getData().stream()
                .filter(x -> x.getId().equals("cubeSelected"))
                .findFirst()
                .orElse(null);
        if (data == null) {
            data = new Data();
            data.setId("cubeSelected");
            executor.getStateMachine().getDatamodel().addData(data);
        }
        data.setExpr(Boolean.toString(set));
        executor.getRootContext().set("cubeSelected", set);

        if (set)
            sessionContextModel.setOperation(null);
        else
            sessionContextModel.setOperation(Event.NAVIGATE_CUBE_SELECT);
    }

    /**
     * Trigger the exit event.
     *
     * @throws ModelException If an error occurred while triggering an event.
     * @see Event#EXIT
     */
    void triggerExitEvent() throws ModelException {
        LOGGER.info("Triggering exit event.");
        executor.triggerEvent(new TriggerEvent(Event.EXIT.getEventName(), TriggerEvent.SIGNAL_EVENT));
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
