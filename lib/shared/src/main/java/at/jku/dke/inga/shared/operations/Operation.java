package at.jku.dke.inga.shared.operations;

import at.jku.dke.inga.shared.ResourceBundleHelper;
import at.jku.dke.inga.shared.display.Displayable;

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
     * @param position     The position of operation in the list of operations.
     */
    public Operation(String eventName, String resourceName, int position) {
        this.eventName = eventName;
        this.displayName = ResourceBundleHelper.getResourceString("OperationNames", resourceName);
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
     * Gets the position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return position == operation.position &&
                Objects.equals(eventName, operation.eventName) &&
                Objects.equals(displayName, operation.displayName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventName, displayName, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Operation.class.getSimpleName() + "[", "]")
                .add("eventName='" + eventName + "'")
                .add("displayName='" + displayName + "'")
                .add("position=" + position)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Operation other) {
        return getPosition() - other.getPosition();
    }

    /**
     * Returns the identifier of the item.
     *
     * @return The unique identifier.
     */
    @Override
    public String getDisplayableId() {
        return eventName;
    }

    /**
     * Returns the title of the item.
     *
     * @return The title.
     */
    @Override
    public String getTitle() {
        return displayName;
    }

    /**
     * Returns the details of the item.
     * Details can be for example a description of the item.
     *
     * @return The details.
     */
    @Override
    public String getDetails() {
        return null;
    }
}
