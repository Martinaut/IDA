package at.jku.dke.inga.web.sessions;

import at.jku.dke.inga.scxml.DialogueStateMachineFactory;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.scxml.exceptions.SessionExpiredException;
import at.jku.dke.inga.scxml.exceptions.StateMachineExecutionException;
import at.jku.dke.inga.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.inga.scxml.context.ContextManager;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.shared.EventNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.SCInstance;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    private final Map<String, SCInstance> sessions;

    /**
     * Instantiates a new instance of class {@linkplain SessionManager}.
     */
    private SessionManager() {
        this.sessions = new HashMap<>();
    }

    /**
     * Creates a new session.
     *
     * @param locale The locale (must be de or en).
     * @return The session ID.
     * @throws IllegalArgumentException           If the locale is invalid.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     */
    public String createSession(String locale) throws StateMachineInstantiationException {
        // Validate Input
        if (locale == null)
            throw new IllegalArgumentException("locale must not be null");
        if (!locale.equals("en") && !locale.equals("de"))
            throw new IllegalArgumentException("locale must be 'en' or 'de'");

        // Compute Session ID
        String id;
        synchronized (sessions) {
            do {
                id = UUID.randomUUID().toString();
            } while (sessions.containsKey(id));

            // Create Context model
            ContextManager.createNewContext(id, locale);

            // Create state chart
            SCInstance instance = DialogueStateMachineFactory.create(id).detachInstance();
            sessions.put(id, instance);
        }
        return id;
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
            sessions.remove(sessionId);
        }
        ContextManager.deleteContext(sessionId);
    }

    public void triggerUserInput(String sessionId, String userInput, DisplayListener listener) throws StateMachineInstantiationException, StateMachineExecutionException, SessionExpiredException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be empty");
        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");

        synchronized (sessions) {
            // Is sessionId present?
            if (!sessions.containsKey(sessionId)) {
                throw new SessionExpiredException("There exists no session with id " + sessionId);
            }

            // Create state chart instance
            SCXMLExecutor executor = DialogueStateMachineFactory.attach(sessions.get(sessionId));

            // Is state chart finished?
            if (executor.getCurrentStatus().isFinal()) {
                sessions.remove(sessionId);
                throw new SessionExpiredException("Session with id " + sessionId + " already expired.");
            }

            // Trigger event
            try {
                ContextModel ctx = ContextManager.getContext(sessionId);
                if (ctx == null)
                    throw new SessionExpiredException("There exists no context with id " + sessionId);

                ctx.setListener(listener);
                ctx.setUserInput(userInput);

                executor.triggerEvent(new TriggerEvent(EventNames.USER_INPUT, TriggerEvent.SIGNAL_EVENT));
            } catch (ModelException ex) {
                throw new StateMachineExecutionException("The triggered event left the engine in inconsistent state.", ex);
            }
        }
    }
}
