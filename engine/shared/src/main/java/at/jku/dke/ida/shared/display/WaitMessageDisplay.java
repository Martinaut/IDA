package at.jku.dke.ida.shared.display;

import java.util.Locale;

/**
 * A display for displaying a message and displaying a waiting spinner.
 */
public class WaitMessageDisplay extends MessageDisplay {
    /**
     * Instantiates a new instance of class {@linkplain MessageDisplay}.
     *
     * @param displayMessage The message.
     */
    public WaitMessageDisplay(String displayMessage) {
        super(displayMessage);
    }

    /**
     * Instantiates a new instance of class {@linkplain MessageDisplay}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale                     The locale for the resource name.
     */
    public WaitMessageDisplay(String displayMessageResourceName, Locale locale) {
        super(displayMessageResourceName, locale);
    }
}
