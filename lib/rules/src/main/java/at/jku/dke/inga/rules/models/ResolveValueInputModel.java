package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Locale;

public class ResolveValueInputModel extends DataModel<AnalysisSituation> {

    private final String userInput;

    /**
     * Instantiates a new instance of class {@link ResolveValueInputModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param analysisSituation  The analysis situation.
     * @param locale             The display locale.
     * @param userInput          The user input.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (or empty).
     */
    public ResolveValueInputModel(String currentState, AnalysisSituation analysisSituation, Locale locale,
                                  String userInput) {
        super(currentState, analysisSituation, locale);

        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");
        this.userInput = userInput;
    }

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    public String getUserInput() {
        return userInput;
    }
}
