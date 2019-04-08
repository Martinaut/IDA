package at.jku.dke.inga.scxml.session;

import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.scxml.events.DisplayEvent;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The model of a session context.
 * A context holds all relevant information for a specific session.
 */
public class SessionContextModel {

    private final String sessionId;
    private final Locale locale;
    private final DisplayListener listener;
    private final AnalysisSituationListener asListener;

    private AnalysisSituation analysisSituation;
    private Display displayData;

    private String userInput;
    private String operation;
    private Map<String, Object> additionalData;

    /**
     * Instantiates a new instance of class {@link SessionContextModel}.
     *
     * @param sessionId  The session id.
     * @param locale     The locale of the context.
     * @param listener   The listener for listening for available display data.
     * @param asListener The listener for listening for changes of the analysis situation.
     * @throws IllegalArgumentException If the {@code sessionId} or {@code locale} is {@code null} or empty or blank.
     */
    SessionContextModel(String sessionId, String locale, DisplayListener listener, AnalysisSituationListener asListener) {
        if (StringUtils.isBlank(sessionId)) throw new IllegalArgumentException("sessionId must not be null empty");
        if (StringUtils.isBlank(locale)) throw new IllegalArgumentException("locale must not be null empty");

        this.sessionId = sessionId;
        this.locale = new Locale(locale);
        this.listener = listener;
        this.asListener = asListener;

        this.analysisSituation = new NonComparativeAnalysisSituation();
        this.displayData = null;

        this.userInput = null;
        this.operation = EventNames.NAVIGATE_CUBE_SELECT;
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
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the operation.
     *
     * @param operation the operation
     */
    public void setOperation(String operation) {
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
    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    /**
     * Sets the analysis situation.
     *
     * @param analysisSituation the analysis situation
     */
    public void setAnalysisSituation(AnalysisSituation analysisSituation) {
        this.analysisSituation = analysisSituation;
        if (asListener != null)
            asListener.changed(new AnalysisSituationEvent(this, sessionId, analysisSituation, getLocale().getLanguage()));
    }

    /**
     * Gets the analysis situation listener.
     *
     * @return the analysis situation listener
     */
    public AnalysisSituationListener getAnalysisSituationListener() {
        return asListener;
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
     * Sets the display data and triggers an event if a listener is registered.
     *
     * @param displayData the display data
     */
    public void setDisplayData(Display displayData) {
        this.displayData = displayData;
        if (listener != null)
            listener.displayDataAvailable(new DisplayEvent(this, sessionId, displayData));
    }

    /**
     * Gets the display listener.
     *
     * @return the display listener
     */
    public DisplayListener getDisplayListener() {
        return listener;
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
        this.additionalData = additionalData;
    }
}
