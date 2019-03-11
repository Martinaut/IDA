package at.jku.dke.inga.shared.display;

import java.util.*;

/**
 * Displays the contents in a simple list.
 */
public class TwoListDisplay extends Display {

    private final Collection<? extends Displayable> dataLeft;
    private final Collection<? extends Displayable> dataRight;

    /**
     * Instantiates a new instance of class {@linkplain TwoListDisplay}.
     *
     * @param displayMessage The display message.
     * @param dataLeft       The data to display in the left list.
     * @param dataRight      The data to display in the right list.
     */
    public TwoListDisplay(String displayMessage, Collection<? extends Displayable> dataLeft, Collection<? extends Displayable> dataRight) {
        super(displayMessage);
        this.dataLeft = Collections.unmodifiableCollection(dataLeft);
        this.dataRight = Collections.unmodifiableCollection(dataRight);
    }

    /**
     * Instantiates a new instance of class {@linkplain Display}.
     *
     * @param displayMessageResourceName The resource name in the {@code DisplayMessages}-resource for the message to display.
     * @param dataLeft                   The data to display in the left list.
     * @param dataRight                  The data to display in the right list.
     */
    public TwoListDisplay(String displayMessageResourceName, Locale locale, Collection<? extends Displayable> dataLeft, Collection<? extends Displayable> dataRight) {
        super(displayMessageResourceName, locale);
        this.dataLeft = dataLeft;
        this.dataRight = dataRight;
    }

    /**
     * Gets the left list data.
     *
     * @return The data that should be displayed in the left list.
     */
    public Collection<? extends Displayable> getDataLeft() {
        return dataLeft;
    }

    /**
     * Gets the right list data.
     *
     * @return The data that should be displayed in the right list.
     */
    public Collection<? extends Displayable> getDataRight() {
        return dataRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", TwoListDisplay.class.getSimpleName() + "[", "]")
                .add("displayMessage='" + getDisplayMessage() + "'")
                .add("dataLeft=" + dataLeft)
                .add("dataRight=" + dataRight)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwoListDisplay)) return false;
        if (!super.equals(o)) return false;
        TwoListDisplay that = (TwoListDisplay) o;
        return Objects.equals(dataLeft, that.dataLeft) &&
                Objects.equals(dataRight, that.dataRight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataLeft, dataRight);
    }
}