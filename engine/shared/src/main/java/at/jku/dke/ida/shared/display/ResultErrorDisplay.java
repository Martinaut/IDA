package at.jku.dke.ida.shared.display;

import java.util.Locale;

/**
 * A display for displaying an error message for an error that occurred while querying the result.
 */
public class ResultErrorDisplay extends ErrorDisplay {
    /**
     * Instantiates a new instance of class {@linkplain ErrorDisplay}.
     *
     * @param displayMessage The error message.
     */
    public ResultErrorDisplay(String displayMessage) {
        super(displayMessage);
    }

    /**
     * Instantiates a new instance of class {@linkplain ErrorDisplay}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the error message to display.
     * @param locale                     The locale for the resource name.
     */
    public ResultErrorDisplay(String displayMessageResourceName, Locale locale) {
        super(displayMessageResourceName, locale);
    }
}
