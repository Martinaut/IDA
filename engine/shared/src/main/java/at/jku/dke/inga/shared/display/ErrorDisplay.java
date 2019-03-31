package at.jku.dke.inga.shared.display;

import java.util.Locale;

/**
 * A display for displaying an error message.
 */
public class ErrorDisplay extends Display {

    /**
     * Instantiates a new instance of class {@linkplain ErrorDisplay}.
     *
     * @param displayMessage The error message.
     */
    public ErrorDisplay(String displayMessage) {
        super(displayMessage);
    }

    /**
     * Instantiates a new instance of class {@linkplain ErrorDisplay}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the error message to display.
     * @param locale                     The locale for the resource name.
     */
    public ErrorDisplay(String displayMessageResourceName, Locale locale) {
        super(displayMessageResourceName, locale);
    }

}
