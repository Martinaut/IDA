package at.jku.dke.inga.web.sessions;

import at.jku.dke.inga.scxml.context.ContextManager;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.scxml.exceptions.SessionExpiredException;
import at.jku.dke.inga.scxml.exceptions.StateMachineExecutionException;
import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.model.ModelException;

import java.util.HashMap;
import java.util.Map;

/**
 * The session manager holds a thread-instance per session and provides methods for creating threads,
 * stopping them and triggering input events.
 */
public class SessionManager {

    // region --- STATIC ---
    private static SessionManager instance = new SessionManager();

    /**
     * Gets the session manager instance.
     *
     * @return the session manager
     */
    public static SessionManager getInstance() {
        return instance;
    }
    // endregion

    private final Map<String, SessionThread> sessions;

    /**
     * Instantiates a new instance of class {@linkplain SessionManager}.
     */
    private SessionManager() {
        this.sessions = new HashMap<>();
    }

    /**
     * Creates a new session and starts the state chart thread.
     *
     * @param sessionId The session identifier.
     * @param locale    The locale (must be de or en).
     * @param listener  The listener for listening for available display data.
     * @throws IllegalArgumentException           If the locale is invalid or if the {@code sessionId} is {@code null} or empty or blank or if the listener is {@code null}.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     */
    @SuppressWarnings("Duplicates")
    public void createSession(String sessionId, String locale, DisplayListener listener) throws StateMachineInstantiationException {
        // Validate Input
        if (StringUtils.isBlank(sessionId))
            throw new IllegalArgumentException("sessionId must not be empty");
        if (locale == null)
            throw new IllegalArgumentException("locale must not be null");
        if (listener == null)
            throw new IllegalArgumentException("listener must not be null");
        if (!locale.equals("en") && !locale.equals("de"))
            throw new IllegalArgumentException("locale must be 'en' or 'de'");

        synchronized (sessions) {
            // Does ID already exist?
            if (sessions.containsKey(sessionId))
                throw new StateMachineInstantiationException("There exists already a session with the specified session id " + sessionId, null);

            // Create Context model
            ContextManager.createNewContext(sessionId, locale, listener);

            // Create state chart
            SessionThread thread = new SessionThread(sessionId);
            sessions.put(sessionId, thread);
            thread.start();
        }
    }

    /**
     * Removes the state chart for the specified session id.
     *
     * @param sessionId The session identifier.
     * @throws IllegalArgumentException If {@code sessionId} is {@code null} or empty or blank.
     */
    public void stopSession(String sessionId) {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be empty");
        synchronized (sessions) {
            if (!sessions.containsKey(sessionId)) return;
            try {
                sessions.get(sessionId).triggerExitEvent();
            } catch (ModelException ignored) {
            }
            sessions.get(sessionId).interrupt();
            sessions.remove(sessionId);
        }
        ContextManager.deleteContext(sessionId);
    }

    /**
     * Triggers a user-input event.
     *
     * @param sessionId The session identifier.
     * @param userInput The user input.
     * @throws StateMachineExecutionException If an error occurred while executing a state machine.
     * @throws SessionExpiredException        If the state chart session does not exist or is already expired (or state chart has finished).
     * @see at.jku.dke.inga.shared.EventNames#USER_INPUT
     */
    public void triggerUserInput(String sessionId, String userInput) throws StateMachineExecutionException, SessionExpiredException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be empty");
        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");

        synchronized (sessions) {
            // Is sessionId present?
            if (!sessions.containsKey(sessionId)) {
                throw new SessionExpiredException("There exists no session with id " + sessionId);
            }

            // Get Thread
            SessionThread thread = sessions.get(sessionId);

            // Is state chart finished?
            if (!thread.isAlive() || thread.isInFinalState()) {
                stopSession(sessionId);
                throw new SessionExpiredException("Session with id " + sessionId + " already expired.");
            }

            // Trigger event
            try {
                ContextModel ctx = ContextManager.getContext(sessionId);
                if (ctx == null)
                    throw new SessionExpiredException("There exists no context with id " + sessionId);

                ctx.setUserInput(userInput);
                thread.triggerUserInputEvent();
            } catch (ModelException ex) {
                throw new StateMachineExecutionException("The triggered event left the engine in inconsistent state.", ex);
            }
        }
    }
}
