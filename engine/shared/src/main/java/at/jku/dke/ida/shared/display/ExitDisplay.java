package at.jku.dke.ida.shared.display;

import java.util.Locale;

/**
 * A notification for the client that the connection will be closed.
 */
public class ExitDisplay extends Display {
    /**
     * Instantiates a new instance of class {@linkplain ExitDisplay}.
     */
    public ExitDisplay() {
        super("Goodbye.");
    }

    /**
     * Instantiates a new instance of class {@linkplain ExitDisplay}.
     *
     * @param locale                     The locale for the resource name.
     */
    protected ExitDisplay(Locale locale) {
        super("goodbye", locale);
    }
}
