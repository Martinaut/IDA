package at.jku.dke.inga.web.models;

/**
 * A message containing the user input.
 */
public class InputMessage {

    private String userInput;

    /**
     * Instantiates a new instance of class {@linkplain InputMessage}.
     */
    public InputMessage() {
    }

    /**
     * Instantiates a new instance of class {@linkplain InputMessage}.
     *
     * @param userInput The user input.
     */
    public InputMessage(String userInput) {
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

    /**
     * Sets the user input.
     *
     * @param userInput the user input
     */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
