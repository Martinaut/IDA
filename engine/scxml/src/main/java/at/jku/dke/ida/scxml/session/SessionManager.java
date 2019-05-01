package at.jku.dke.ida.scxml.session;

import at.jku.dke.ida.scxml.events.AnalysisSituationListener;
import at.jku.dke.ida.scxml.events.DisplayListener;
import at.jku.dke.ida.scxml.events.QueryResultListener;
import at.jku.dke.ida.scxml.exceptions.SessionExpiredException;
import at.jku.dke.ida.scxml.exceptions.StateMachineExecutionException;
import at.jku.dke.ida.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The session manager holds an object per session and provides methods for creating sessions,
 * deleting them and triggering input events.
 */
public final class SessionManager {

    // region --- STATIC ---
    private static Logger LOGGER = LogManager.getLogger(SessionManager.class);
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

    private final Map<String, Session> sessions;

    /**
     * Instantiates a new instance of class {@linkplain SessionManager}.
     */
    private SessionManager() {
        this.sessions = new HashMap<>();
    }

    /**
     * Creates a new session and initiates the state machine.
     *
     * @param sessionId The session id.
     * @param locale    The locale.
     * @throws IllegalArgumentException           If the {@code locale} or {@code sessionId} is {@code null} or empty.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     */
    public void createSession(String sessionId, String locale) throws StateMachineInstantiationException {
        // Validate Input
        if (StringUtils.isBlank(sessionId))
            throw new IllegalArgumentException("sessionId must not be null or empty");
        if (StringUtils.isBlank(locale))
            throw new IllegalArgumentException("locale must not be null or empty");

        LOGGER.info("Creating new session for session id {} with locale {}.", sessionId, locale);
        synchronized (sessions) {
            // Does ID already exist?
            if (sessions.containsKey(sessionId)) {
                LOGGER.warn("Duplicate session id found. Cannot create session.");
                throw new StateMachineInstantiationException("There exists already a session with the specified session id " + sessionId, null);
            }

            // Create Session
            Session session = new Session(
                    sessionId,
                    locale,
                    BeanUtil.getOptionalBean(DisplayListener.class),
                    BeanUtil.getOptionalBean(AnalysisSituationListener.class),
                    BeanUtil.getOptionalBean(QueryResultListener.class));
            sessions.put(sessionId, session);
        }
    }

    /**
     * Initiates the state machine of the session.
     * If the session id was not found, nothing will happen.
     *
     * @param sessionId The session id.
     * @throws IllegalArgumentException If the {@code sessionId} is {@code null} or empty.
     */
    public void initiateStateMachine(String sessionId) {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");

        synchronized (sessions) {
            if (!sessions.containsKey(sessionId)) return;

            LOGGER.info("Initiating session {} state machine.", sessionId);
            sessions.get(sessionId).initiate();
        }
    }

    /**
     * Deletes the session.
     * If the session id was not found, nothing will happen.
     *
     * @param sessionId The session id.
     * @throws IllegalArgumentException If the {@code sessionId} is {@code null} or empty.
     */
    public void deleteSession(String sessionId) {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");

        synchronized (sessions) {
            if (!sessions.containsKey(sessionId)) return;

            LOGGER.info("Deleting session {}.", sessionId);
            try {
                sessions.get(sessionId).triggerExitEvent();
            } catch (ModelException ex) {
                LOGGER.warn("Could not trigger exit event for session " + sessionId, ex);
            }
            sessions.remove(sessionId);
        }
    }

    /**
     * Returns the session with the specified id.
     *
     * @param sessionId The session id.
     * @return The session or {@code null} if the session id does not exist.
     * @throws IllegalArgumentException If the {@code sessionId} is {@code null} or empty.
     */
    public Session getSession(String sessionId) {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");
        synchronized (sessions) {
            if (!sessions.containsKey(sessionId)) return null;
            return sessions.get(sessionId);
        }
    }

    /**
     * Triggers a user-input event.
     *
     * @param sessionId The session id.
     * @param userInput The user input.
     * @throws StateMachineExecutionException If an error occurred while executing a state machine.
     * @throws SessionExpiredException        If the state chart session does not exist or is already expired (or state chart has finished).
     * @see Event#USER_INPUT
     */
    public void triggerUserInput(String sessionId, String userInput) throws StateMachineExecutionException, SessionExpiredException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");
        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");

        LOGGER.info("Triggering user input for session {}.", sessionId);

        // Get session
        Session session = getSession(sessionId);
        if (session == null) {
            LOGGER.warn("Cannot trigger user input: Session {} does not exist.", sessionId);
            throw new SessionExpiredException("There exists no session with id " + sessionId);
        }

        // Is already finished?
        if (session.isInFinalState()) {
            LOGGER.warn("Cannot trigger user input: Session {} already expired.", sessionId);
            deleteSession(sessionId);
            throw new SessionExpiredException("Session with id " + sessionId + " already expired.");
        }

        // Trigger event
        try {
            session.triggerUserInputEvent(userInput);
        } catch (ModelException ex) {
            LOGGER.error("An error occurred while triggering the user input event for session {}.", sessionId);
            throw new StateMachineExecutionException("The triggered event left the engine in inconsistent state.", ex);
        }
    }

    /**
     * Triggers a revise query event.
     *
     * @param sessionId The session id.
     * @throws StateMachineExecutionException If an error occurred while executing a state machine.
     * @throws SessionExpiredException        If the state chart session does not exist or is already expired (or state chart has finished).
     * @see Event#REVISE_QUERY
     */
    public void triggerReviseQuery(String sessionId) throws StateMachineExecutionException, SessionExpiredException {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");

        LOGGER.info("Triggering revise query for session {}.", sessionId);

        // Get session
        Session session = getSession(sessionId);
        if (session == null) {
            LOGGER.warn("Cannot trigger revise query: Session {} does not exist.", sessionId);
            throw new SessionExpiredException("There exists no session with id " + sessionId);
        }

        // Is already finished?
        if (session.isInFinalState()) {
            LOGGER.warn("Cannot trigger revise query: Session {} already expired.", sessionId);
            deleteSession(sessionId);
            throw new SessionExpiredException("Session with id " + sessionId + " already expired.");
        }

        // Trigger event
        try {
            session.triggerReviseQuery();
        } catch (ModelException ex) {
            LOGGER.error("An error occurred while triggering the revise query event for session {}.", sessionId);
            throw new StateMachineExecutionException("The triggered event left the engine in inconsistent state.", ex);
        }
    }
}
