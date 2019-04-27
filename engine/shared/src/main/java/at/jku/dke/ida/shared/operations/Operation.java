package at.jku.dke.ida.shared.operations;

import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.display.Displayable;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A class representing an operation to execute a user can opt for.
 */
public class Operation implements Comparable<Operation>, Displayable {

    private final String eventName;
    private final String displayName;
    private final int position;

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param eventName The event name and resource name in the <code>OperationNames</code> resource bundle.
     * @param position  The position of operation in the list of operations.
     */
    public Operation(String eventName, int position) {
        this.eventName = eventName;
        this.displayName = ResourceBundleHelper.getResourceString("OperationNames", eventName);
        this.position = position;
    }

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param eventName   The event name.
     * @param displayName The display name.
     * @param position    The position of operation in the list of operations.
     */
    public Operation(String eventName, String displayName, int position) {
        this.eventName = eventName;
        this.displayName = displayName;
        this.position = position;
    }

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param eventName The event name and resource name in the <code>OperationNames</code> resource bundle.
     * @param locale    The locale for the resource name.
     * @param position  The position of operation in the list of operations.
     */
    public Operation(String eventName, Locale locale, int position) {
        this.eventName = eventName;
        this.displayName = ResourceBundleHelper.getResourceString("OperationNames", locale, eventName);
        this.position = position;
    }

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param eventName    The event name.
     * @param resourceName The resource name in the <code>OperationNames</code> resource bundle.
     * @param locale       The locale for the resource name.
     * @param position     The position of operation in the list of operations.
     */
    public Operation(String eventName, String resourceName, Locale locale, int position) {
        this.eventName = eventName;
        this.displayName = ResourceBundleHelper.getResourceString("OperationNames", locale, resourceName);
        this.position = position;
    }

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the display position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return position == operation.position &&
                Objects.equals(eventName, operation.eventName) &&
                Objects.equals(displayName, operation.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, displayName, position);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Operation.class.getSimpleName() + "[", "]")
                .add("eventName='" + eventName + "'")
                .add("displayName='" + displayName + "'")
                .add("position=" + position)
                .toString();
    }

    @Override
    public int compareTo(Operation other) {
        return getPosition() - other.getPosition();
    }

    @Override
    public String getDisplayableId() {
        return eventName;
    }

    @Override
    public String getTitle() {
        return displayName;
    }

    @Override
    public String getDetails() {
        return null;
    }
}
