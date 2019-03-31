package at.jku.dke.inga.shared.display;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Displays the contents in a simple list.
 */
public class ListDisplay extends Display {

    private final List<? extends Displayable> data;

    /**
     * Instantiates a new instance of class {@linkplain ListDisplay}.
     *
     * @param displayMessage The display message.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, List<? extends Displayable> data) {
        super(displayMessage);
        this.data = Collections.unmodifiableList(Objects.requireNonNullElseGet(data, ArrayList::new));
    }

    /**
     * Instantiates a new instance of class {@linkplain ListDisplay}.
     *
     * @param displayMessage The display message.
     * @param data           The data to display in a list.
     */
    public ListDisplay(String displayMessage, Iterable<? extends Displayable> data) {
        super(displayMessage);
        this.data = StreamSupport
                .stream(data == null ? Spliterators.emptySpliterator() : data.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale                     The locale for the resource name.
     * @param data                       The data to display in a list.
     */
    public ListDisplay(String displayMessageResourceName, Locale locale, List<? extends Displayable> data) {
        super(displayMessageResourceName, locale);
        this.data = Collections.unmodifiableList(Objects.requireNonNullElseGet(data, ArrayList::new));
    }

    /**
     * Returns the data to display.
     *
     * @return The data that should be displayed in a list.
     */
    public List<? extends Displayable> getData() {
        return data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ListDisplay.class.getSimpleName() + "[", "]")
                .add("displayMessage='" + getDisplayMessage() + "'")
                .add("data=" + data)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListDisplay)) return false;
        if (!super.equals(o)) return false;
        ListDisplay that = (ListDisplay) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), data);
    }
}
