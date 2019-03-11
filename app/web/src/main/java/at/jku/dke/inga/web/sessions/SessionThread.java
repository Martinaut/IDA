package at.jku.dke.inga.web.sessions;

import at.jku.dke.inga.scxml.DialogueStateMachineFactory;
import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.inga.shared.EventNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The thread running a state machine for a specific session.
 */
class SessionThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(SessionThread.class);

    private final SCXMLExecutor executor;

    /**
     * Instantiates a new instance of class {@linkplain SessionThread}.
     *
     * @param sessionId The session id.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     * @throws IllegalArgumentException           If the {@code sessionId} is {@code null} or empty or blank.
     */
    SessionThread(String sessionId) throws StateMachineInstantiationException {
        super("SessionThread_" + sessionId);
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be empty");
        this.executor = DialogueStateMachineFactory.create(sessionId);
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
     * @throws ModelException If an error occurred while triggering an event.
     * @see EventNames#USER_INPUT
     */
    public void triggerUserInputEvent() throws ModelException {
        executor.triggerEvent(new TriggerEvent(EventNames.USER_INPUT, TriggerEvent.SIGNAL_EVENT));
    }

    /**
     * Trigger the exit event.
     *
     * @throws ModelException If an error occurred while triggering an event.
     * @see EventNames#EXIT
     */
    public void triggerExitEvent() throws ModelException {
        executor.triggerEvent(new TriggerEvent(EventNames.EXIT, TriggerEvent.SIGNAL_EVENT));
        executor.detachInstance();
    }

    /**
     * Executes the state-chart.
     */
    @Override
    public void run() {
        LOGGER.info("Starting thread {}.", getName());
        try {
            executor.go();
        } catch (ModelException ex) {
            LOGGER.error("An error occurred in thread " + getName() + " while executing state chart.", ex);
        }
        LOGGER.info("Thread {} stopped.", getName());
    }
}
