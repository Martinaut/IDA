package at.jku.dke.inga.app;

import at.jku.dke.inga.app.ruleset.InitialSentenceService;
import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.scxml.session.SessionManager;
import at.jku.dke.inga.shared.session.SessionModel;
import at.jku.dke.inga.web.controllers.InitialSentenceHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class parses the sentence and tries to extract as much information as possible from the user input.
 */
@Service
public class NLPSentenceHandler implements InitialSentenceHandler {

    private final AnalysisSituationListener listener;

    /**
     * Instantiates a new instance of class {@linkplain NLPSentenceHandler}.
     *
     * @param listener The analysis situation listener.
     */
    public NLPSentenceHandler(Optional<AnalysisSituationListener> listener) {
        this.listener = listener.orElse(evt -> {
        });
    }

    /**
     * Parses the initial sentence and sets the values of the analysis situation according to the sentence.
     *
     * @param sessionModel    The session model of the current session.
     * @param initialSentence The sentence to parse.
     */
    @Override
    public void parseSentence(SessionModel sessionModel, String initialSentence) {
        InitialSentenceService.fillAnalysisSituation(sessionModel, initialSentence);
        if (sessionModel.getAnalysisSituation().isCubeDefined()) {
            SessionManager.getInstance().getSession(sessionModel.getSessionId()).setCubeSetFlag(true);
            if (listener != null)
                listener.changed(new AnalysisSituationEvent(this, sessionModel.getSessionId(), sessionModel.getAnalysisSituation(), sessionModel.getLocale().getLanguage()));
        }
    }

}
