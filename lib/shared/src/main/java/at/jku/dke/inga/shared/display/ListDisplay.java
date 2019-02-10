package at.jku.dke.inga.shared.display;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Displays the contents in a simple list.
 */
public class ListDisplay extends Display {

    private final Collection<? extends Displayable> data;

    /**
     * Instantiates a new instance of class {@linkplain ListDisplay}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, Collection<? extends Displayable> data) {
        super(displayMessage);
        this.data = Collections.unmodifiableCollection(data);
    }

    /**
     * Instantiates a new instance of class {@linkplain ListDisplay}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, Iterable<? extends Displayable> data) {
        super(displayMessage);
        this.data = StreamSupport.stream(data.spliterator(), false).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessage The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale         The locale for the resource name.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, Locale locale, Collection<? extends Displayable> data) {
        super(displayMessage, locale);
        this.data = data;
    }

    /**
     * Returns the data to display.
     *
     * @return The data that should be displayed in a list.
     */
    public Collection<? extends Displayable> getData() {
        return data;
    }
}
