package at.jku.dke.inga.web.controllers;

import at.jku.dke.inga.shared.session.SessionModel;

/**
 * The method of this interface gets called when a new session is started to parse the initial user input.
 */
public interface InitialSentenceHandler {

    /**
     * Parses the initial sentence and sets the values of the analysis situation according to the sentence.
     *
     * @param sessionModel    The session model of the current session.
     * @param initialSentence The sentence to parse.
     */
    void parseSentence(SessionModel sessionModel, String initialSentence);

}
