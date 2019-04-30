package at.jku.dke.ida.shared.display;

import java.util.Locale;

/**
 * A display for displaying a message.
 */
public class MessageDisplay extends Display {

    /**
     * Instantiates a new instance of class {@linkplain MessageDisplay}.
     *
     * @param displayMessage The message.
     */
    public MessageDisplay(String displayMessage) {
        super(displayMessage);
    }

    /**
     * Instantiates a new instance of class {@linkplain MessageDisplay}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale                     The locale for the resource name.
     */
    public MessageDisplay(String displayMessageResourceName, Locale locale) {
        super(displayMessageResourceName, locale);
    }

}
