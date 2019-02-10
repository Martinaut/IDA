package at.jku.dke.inga.shared.display;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * Displays the contents in a simple list.
 */
public class TwoListDisplay extends Display {

    private final Collection<Displayable> dataLeft;
    private final Collection<Displayable> dataRight;

    /**
     * Instantiates a new instance of class {@linkplain TwoListDisplay}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param dataLeft       The data to display in the left list.
     * @param dataRight      The data to display in the right list.
     */
    public TwoListDisplay(String displayMessage, Collection<Displayable> dataLeft, Collection<Displayable> dataRight) {
        super(displayMessage);
        this.dataLeft = Collections.unmodifiableCollection(dataLeft);
        this.dataRight = Collections.unmodifiableCollection(dataRight);
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param dataLeft       The data to display in the left list.
     * @param dataRight      The data to display in the right list.
     */
    public TwoListDisplay(String displayMessage, Locale locale, Collection<Displayable> dataLeft, Collection<Displayable> dataRight) {
        super(displayMessage, locale);
        this.dataLeft = dataLeft;
        this.dataRight = dataRight;
    }

    /**
     * Gets the left list data.
     *
     * @return The data that should be displayed in the left list.
     */
    public Collection<Displayable> getDataLeft() {
        return dataLeft;
    }

    /**
     * Gets the right list data.
     *
     * @return The data that should be displayed in the right list.
     */
    public Collection<Displayable> getDataRight() {
        return dataRight;
    }
}
