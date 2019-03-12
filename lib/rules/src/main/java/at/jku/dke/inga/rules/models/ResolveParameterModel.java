package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.display.Displayable;
import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Locale;
import java.util.Map;

public class ResolveParameterModel extends DataModel<AnalysisSituation> {

    private final String userInput;
    private final Map<Integer, Displayable> possibleValues;

    /**
     * Instantiates a new instance of class {@link ResolveParameterModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param analysisSituation  The analysis situation.
     * @param locale             The display locale.
     * @param userInput          The user input.
     * @param possibleValues Possible operations (the key of the map has to be the display number).
     * @throws IllegalArgumentException If any of the parameters is {@code null} (or empty).
     */
    public ResolveParameterModel(String currentState, AnalysisSituation analysisSituation, Locale locale,
                                 String userInput, Map<Integer, Displayable> possibleValues) {
        super(currentState, analysisSituation, locale);

        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");
        if (possibleValues == null) throw new IllegalArgumentException("possibleValues must not be null");
        this.userInput = userInput;
        this.possibleValues = possibleValues;
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
     * Returns the possible operations.
     * The key of the map has to be the display number.
     *
     * @return The possible operations.
     */
    public Map<Integer, Displayable> getPossibleValues() {
        return possibleValues;
    }
}
