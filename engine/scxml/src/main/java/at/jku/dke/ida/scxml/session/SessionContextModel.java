package at.jku.dke.ida.scxml.session;

import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.events.AnalysisSituationListener;
import at.jku.dke.ida.scxml.events.DisplayEvent;
import at.jku.dke.ida.scxml.events.DisplayListener;
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
        super(sessionId, locale);
        this.listener = listener;
        this.asListener = asListener;
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

    @Override
    public void setAnalysisSituation(EngineAnalysisSituation analysisSituation) {
        super.setAnalysisSituation(analysisSituation);
        if (asListener != null)
            asListener.changed(new AnalysisSituationEvent(this, getSessionId(), analysisSituation, getLocale().getLanguage()));
    }

    @Override
    public void setDisplayData(Display displayData) {
        super.setDisplayData(displayData);
        if (listener != null)
            listener.displayDataAvailable(new DisplayEvent(this, getSessionId(), displayData));
    }
}
