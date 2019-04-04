package at.jku.dke.inga.app;

import at.jku.dke.inga.app.ruleset.drools.InitialSentenceModel;
import at.jku.dke.inga.app.ruleset.drools.InitialSentenceService;
import at.jku.dke.inga.scxml.session.SessionContextModel;
import at.jku.dke.inga.web.controllers.InitialSentenceHandler;
import org.springframework.stereotype.Service;

/**
 * This class parses the sentence and tries to extract as much information as possible from the user input.
 */
@Service
public class NLPSentenceHandler implements InitialSentenceHandler {

    /**
     * Parses the initial sentence and sets the values of the analysis situation according to the sentence.
     *
     * @param sessionModel    The session model of the current session.
     * @param initialSentence The sentence to parse.
     */
    @Override
    public void parseSentence(SessionContextModel sessionModel, String initialSentence) {
        new InitialSentenceService().executeRules(new InitialSentenceModel(
                sessionModel.getLocale(),
                sessionModel.getAnalysisSituation(),
                sessionModel.getAdditionalData(),
                sessionModel.getSessionId(),
                initialSentence));
    }

}
