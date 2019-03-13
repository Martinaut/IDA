package at.jku.dke.inga.scxml.context;

import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.scxml.events.DisplayEvent;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;

import java.util.Locale;

/**
 * The model of a context.
 * A context holds all relevant information for a specific session.
 */
public class ContextModel {

    private final String sessionId;
    private final Locale locale;
    private final DisplayListener listener;
    private final AnalysisSituationListener asListener;

    private AnalysisSituation analysisSituation;
    private Display displayData;

    private String userInput;
    private Integer page;
    private String operation;

    /**
     * Instantiates a new instance of class {@link ContextModel}.
     *
     * @param sessionId  The session id.
     * @param locale     The locale of the context.
     * @param listener   The listener for listening for available display data.
     * @param asListener The listener for listening for changes of the analysis situation.
     */
    public ContextModel(String sessionId, String locale, DisplayListener listener, AnalysisSituationListener asListener) {
        this.sessionId = sessionId;
        this.locale = new Locale(locale);
        this.listener = listener;
        this.asListener = asListener;

        this.analysisSituation = new NonComparativeAnalysisSituation();
        this.displayData = null;

        this.userInput = null;
        this.page = null;
        this.operation = EventNames.NAVIGATE_CUBE_SELECT;

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
     * Gets the page.
     *
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Sets the page.
     *
     * @param page the page
     */
    public void setPage(Integer page) {
        this.page = page;
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
            asListener.changed(sessionId, new AnalysisSituationEvent(this, analysisSituation));
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
            listener.displayDataAvailable(sessionId, new DisplayEvent(this, displayData));
    }

    /**
     * Gets the analysis situation listener.
     *
     * @return the analysis situation listener
     */
    public AnalysisSituationListener getAnalysisSituationListener() {
        return asListener;
    }
}
