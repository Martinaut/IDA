package at.jku.dke.ida.app;

import at.jku.dke.ida.app.ruleset.InitialSentenceService;
import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.events.AnalysisSituationListener;
import at.jku.dke.ida.scxml.session.Session;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.web.controllers.InitialSentenceHandler;
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
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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
            Session session = SessionManager.getInstance().getSession(sessionModel.getSessionId());
            if (session != null) {
                session.setCubeSetFlag(true);
                if (listener != null)
                    listener.changed(new AnalysisSituationEvent(this, sessionModel.getSessionId(), sessionModel.getAnalysisSituation(), sessionModel.getLocale().getLanguage()));
            }
        }
    }
}