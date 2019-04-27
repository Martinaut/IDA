package at.jku.dke.ida.shared.display;

import java.util.*;

/**
 * Displays the contents in two lists (e.g. used for replace operations).
 */
public class TwoListDisplay extends Display {

    private final List<? extends Displayable> dataLeft;
    private final List<? extends Displayable> dataRight;

    /**
     * Instantiates a new instance of class {@linkplain TwoListDisplay}.
     *
     * @param displayMessage The display message.
     * @param dataLeft       The data to display in the left list.
     * @param dataRight      The data to display in the right list.
     */
    public TwoListDisplay(String displayMessage, List<? extends Displayable> dataLeft, List<? extends Displayable> dataRight) {
        super(displayMessage);
        this.dataLeft = Collections.unmodifiableList(Objects.requireNonNullElseGet(dataLeft, ArrayList::new));
        this.dataRight = Collections.unmodifiableList(Objects.requireNonNullElseGet(dataRight, ArrayList::new));
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param locale                     The locale for the resource name.
     * @param dataLeft                   The data to display in the left list.
     * @param dataRight                  The data to display in the right list.
     */
    public TwoListDisplay(String displayMessageResourceName, Locale locale, List<? extends Displayable> dataLeft, List<? extends Displayable> dataRight) {
        super(displayMessageResourceName, locale);
        this.dataLeft = Collections.unmodifiableList(Objects.requireNonNullElseGet(dataLeft, ArrayList::new));
        this.dataRight = Collections.unmodifiableList(Objects.requireNonNullElseGet(dataRight, ArrayList::new));
    }

    /**
     * Gets the left list data.
     *
     * @return The data that should be displayed in the left list.
     */
    public List<? extends Displayable> getDataLeft() {
        return dataLeft;
    }

    /**
     * Gets the right list data.
     *
     * @return The data that should be displayed in the right list.
     */
    public List<? extends Displayable> getDataRight() {
        return dataRight;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TwoListDisplay.class.getSimpleName() + "[", "]")
                .add("displayMessage='" + getDisplayMessage() + "'")
                .add("dataLeft=" + dataLeft)
                .add("dataRight=" + dataRight)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwoListDisplay)) return false;
        if (!super.equals(o)) return false;
        TwoListDisplay that = (TwoListDisplay) o;
        return Objects.equals(dataLeft, that.dataLeft) &&
                Objects.equals(dataRight, that.dataRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataLeft, dataRight);
    }
}