package at.jku.dke.ida.shared.session;

import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The model of a session context.
 * A context holds all relevant information for a specific session.
 */
public class SessionModel {

    private final String sessionId;
    private final Locale locale;

    private EngineAnalysisSituation analysisSituation;
    private Display displayData;
    private String queryResult;

    private String userInput;
    private Event operation;
    private Map<String, Object> additionalData;

    /**
     * Instantiates a new instance of class {@link SessionModel}.
     *
     * @param sessionId The session id.
     * @param locale    The locale of the context.
     * @throws IllegalArgumentException If the {@code sessionId} or {@code locale} is {@code null} or empty or blank.
     */
    public SessionModel(String sessionId, String locale) {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");
        if (StringUtils.isBlank(locale)) throw new IllegalArgumentException("locale must not be null empty");

        this.sessionId = sessionId;
        this.locale = new Locale(locale);

        this.analysisSituation = new NonComparativeAnalysisSituation();
        this.displayData = null;
        this.queryResult = null;

        this.userInput = null;
        this.operation = Event.NAVIGATE_CUBE_SELECT;
        this.additionalData = new HashMap<>();
    }

    /**
     * Gets session id.
     *
     * @return the session id
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * Sets the user input.
     *
     * @param userInput the user input
     */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    /**
     * Gets the operation.
     *
     * @return the operation
     */
    public Event getOperation() {
        return operation;
    }

    /**
     * Sets the operation.
     *
     * @param operation the operation
     */
    public void setOperation(Event operation) {
        this.operation = operation;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    public EngineAnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    /**
     * Sets the analysis situation.
     *
     * @param analysisSituation the analysis situation
     * @throws IllegalArgumentException analysisSituation must not be null
     */
    public void setAnalysisSituation(EngineAnalysisSituation analysisSituation) {
        if (analysisSituation == null) throw new IllegalArgumentException("analysisSituation must not be null");
        this.analysisSituation = analysisSituation;
    }

    /**
     * Gets the display data.
     *
     * @return the display data
     */
    public Display getDisplayData() {
        return displayData;
    }

    /**
     * Sets the display data.
     *
     * @param displayData the display data
     */
    public void setDisplayData(Display displayData) {
        this.displayData = displayData;
    }

    /**
     * Gets the query result.
     *
     * @return the query result
     */
    public String getQueryResult() {
        return queryResult;
    }

    /**
     * Sets the query result.
     *
     * @param queryResult the query result
     */
    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    /**
     * Gets additional data.
     *
     * @return the additional data
     */
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    /**
     * Sets additional data.
     *
     * @param additionalData the additional data
     */
    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = Objects.requireNonNullElseGet(additionalData, HashMap::new);
    }

}
