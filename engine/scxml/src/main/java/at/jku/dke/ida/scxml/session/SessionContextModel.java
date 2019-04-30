package at.jku.dke.ida.scxml.session;

import at.jku.dke.ida.scxml.events.*;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

/**
 * The model of a session context.
 * A context holds all relevant information for a specific session.
 */
public class SessionContextModel extends SessionModel {

    private final DisplayListener listener;
    private final AnalysisSituationListener asListener;
    private final QueryResultListener qrListener;

    /**
     * Instantiates a new instance of class {@link SessionContextModel}.
     *
     * @param sessionId  The session id.
     * @param locale     The locale of the context.
     * @param listener   The listener for listening for available display data.
     * @param asListener The listener for listening for changes of the analysis situation.
     * @param qrListener The listener for listening for changes of the query result.
     * @throws IllegalArgumentException If the {@code sessionId} or {@code locale} is {@code null} or empty or blank.
     */
    SessionContextModel(String sessionId, String locale, DisplayListener listener, AnalysisSituationListener asListener,
                        QueryResultListener qrListener) {
        super(sessionId, locale);
        this.listener = listener;
        this.asListener = asListener;
        this.qrListener = qrListener;
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
     * Gets the display listener.
     *
     * @return the display listener
     */
    public DisplayListener getDisplayListener() {
        return listener;
    }

    /**
     * Gets the query result listener.
     *
     * @return the query result listener
     */
    public AnalysisSituationListener getQueryResultListener() {
        return asListener;
    }

    /**
     * Sets the analysis situation and triggers an event if a listener is registered.
     *
     * @param analysisSituation the analysis situation
     * @throws IllegalArgumentException analysisSituation must not be null
     */
    @Override
    public void setAnalysisSituation(EngineAnalysisSituation analysisSituation) {
        super.setAnalysisSituation(analysisSituation);
        if (asListener != null)
            asListener.changed(new AnalysisSituationEvent(this, getSessionId(), analysisSituation, getLocale().getLanguage()));
    }

    /**
     * Sets the display data and triggers an event if a listener is registered.
     *
     * @param displayData the display data
     */
    @Override
    public void setDisplayData(Display displayData) {
        super.setDisplayData(displayData);
        if (listener != null)
            listener.displayDataAvailable(new DisplayEvent(this, getSessionId(), displayData));
    }

    /**
     * Sets the query result and triggers an event if a listener is registered.
     *
     * @param queryResult the query result
     */
    @Override
    public void setQueryResult(String queryResult) {
        super.setQueryResult(queryResult);
        if (qrListener != null)
            qrListener.changed(new QueryResultEvent(this, getSessionId(), queryResult));
    }
}
