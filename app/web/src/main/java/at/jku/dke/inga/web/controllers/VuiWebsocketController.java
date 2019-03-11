package at.jku.dke.inga.web.controllers;

import at.jku.dke.inga.scxml.exceptions.SessionExpiredException;
import at.jku.dke.inga.scxml.exceptions.StateMachineExecutionException;
import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.inga.shared.display.ErrorDisplay;
import at.jku.dke.inga.web.listener.DisplayDataEventListener;
import at.jku.dke.inga.web.models.InputMessage;
import at.jku.dke.inga.web.models.StartDialogueMessage;
import at.jku.dke.inga.web.sessions.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * The controller with websocket endpoints.
 */
@Controller
public class VuiWebsocketController {

    private static final Logger LOGGER = LogManager.getLogger(VuiWebsocketController.class);

    private final DisplayDataEventListener eventListener;

    /**
     * Instantiates a new instance of class {@linkplain VuiWebsocketController}.
     *
     * @param eventListener The data listener.
     */
    @Autowired
    public VuiWebsocketController(DisplayDataEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Starts a new dialogue state machine.
     *
     * @param message        The message.
     * @param headerAccessor The header accessor used to get the session id.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     */
    @MessageMapping("/start")
    public void start(@Payload StartDialogueMessage message, SimpMessageHeaderAccessor headerAccessor) throws StateMachineInstantiationException {
        SessionManager.getInstance().createSession(headerAccessor.getSessionId(), message.getLocale(), eventListener);
    }

    /**
     * Triggers a user-input event on the state machine of the current session.
     *
     * @param message        The message with the user input.
     * @param headerAccessor The header accessor used to get the session id.
     * @throws StateMachineExecutionException If an error occurred while executing a state machine.
     * @throws SessionExpiredException        If the state chart session does not exist or is already expired (or state chart has finished).
     * @see at.jku.dke.inga.shared.EventNames#USER_INPUT
     */
    @MessageMapping("/input")
    public void userInput(@Payload InputMessage message, SimpMessageHeaderAccessor headerAccessor) throws StateMachineExecutionException, SessionExpiredException {
        SessionManager.getInstance().triggerUserInput(headerAccessor.getSessionId(), message.getUserInput());
    }


    // region --- Exception Handlers ---

    /**
     * Handles {@link StateMachineInstantiationException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(StateMachineInstantiationException ex) {
        LOGGER.error("An error occurred while instantiating a state machine.", ex);
        return new ErrorDisplay("The State Machine could not be instantiated: " + ex.getMessage());
    }

    /**
     * Handles {@link StateMachineExecutionException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(StateMachineExecutionException ex) {
        LOGGER.error("An error occurred while executing a state machine.", ex);
        return new ErrorDisplay("An error occurred while executing a state machine: " + ex.getMessage());
    }

    /**
     * Handles {@link SessionExpiredException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(SessionExpiredException ex) {
        LOGGER.error("The state chart session does not exist or is already expired (or state chart has finished).", ex);
        return new ErrorDisplay("The state chart session does not exist or is already expired (or state chart has finished): " + ex.getMessage());
    }
    // endregion
}
