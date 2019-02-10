package at.jku.dke.inga.shared.display;

import at.jku.dke.inga.shared.ResourceBundleHelper;

import java.util.Locale;

/**
 * Base-class for all display data.
 */
public abstract class Display {

    private final String displayMessage;

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     */
    protected Display(String displayMessage) {
        this.displayMessage = ResourceBundleHelper.getResourceString("DisplayMessages", displayMessage);
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale         The locale for the resource name.
     */
    protected Display(String displayMessage, Locale locale) {
        this.displayMessage = ResourceBundleHelper.getResourceString("DisplayMessages", locale, displayMessage);
    }

    /**
     * Gets display message.
     *
     * @return the display message
     */
    public String getDisplayMessage() {
        return displayMessage;
    }

}
