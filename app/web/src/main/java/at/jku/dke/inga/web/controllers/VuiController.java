package at.jku.dke.inga.web.controllers;

import at.jku.dke.inga.scxml.exceptions.SessionExpiredException;
import at.jku.dke.inga.scxml.exceptions.StateMachineExecutionException;
import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.web.models.DisplayResult;
import at.jku.dke.inga.web.sessions.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.atomic.AtomicReference;

@RestController
public class VuiController {

    /**
     * Starts a new session and returns the session ID.
     *
     * @param locale The locale the user requested.
     * @return The session id.
     * @throws ResponseStatusException If the state machine could not be instantiated.
     */
    @PostMapping(value = "/", produces = "text/plain")
    public String startSession(@RequestParam(value = "locale", required = false, defaultValue = "en") String locale) {
        try {
            return SessionManager.getInstance().createSession(locale);
        } catch (StateMachineInstantiationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "State Machine could not be instantiated", ex);
        }
    }

    /**
     * Stops the session and deletes the session data.
     *
     * @param sessionId The session identifier.
     */
    @DeleteMapping(value = "/")
    public void stopSession(@RequestHeader(value = "X-Inga-Session", defaultValue = "") String sessionId) {
        SessionManager.getInstance().stopSession(sessionId);
    }

    @PutMapping(value = "/", produces = "application/json")
    public DisplayResult execute(@RequestHeader(value = "X-Inga-Session") String sessionId,
                                 @RequestParam(value = "input") String text) {
        final AtomicReference<Display> display = new AtomicReference<>();
        DisplayListener listener = evt -> {
            display.set(evt.getDisplay());
            display.notify();
        };

        synchronized (display) {
            // Trigger event
            try {
                SessionManager.getInstance().triggerUserInput(sessionId, text, listener);
            } catch (StateMachineInstantiationException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "State Machine could not be instantiated", ex);
            } catch (StateMachineExecutionException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while executing state machine", ex);
            } catch (SessionExpiredException ex) {
                throw new ResponseStatusException(HttpStatus.GONE, "Session does not exist or expired", ex);
            }

            // Wait for display data
            try {
                display.wait();
            } catch (InterruptedException ex) {
                // TODO: verbessern
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unkown error occurred.", ex);
            }
        }
        // Return display data
        return new DisplayResult(display.get());
    }
}
