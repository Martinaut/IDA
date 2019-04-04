package at.jku.dke.inga.web.controllers;

import at.jku.dke.inga.scxml.session.SessionContextModel;

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
    void parseSentence(SessionContextModel sessionModel, String initialSentence);

}
