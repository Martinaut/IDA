package at.jku.dke.ida.shared.operations;

import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.display.Displayable;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A class representing an operation to execute a user can opt for.
 */
public class Operation implements Comparable<Operation>, Displayable {

    // region --- STATIC ---
    private static String BUNDLE_NAME = "shared.OperationNames";

    /**
     * Overrides the default bundle name ({@code shared.OperationNames}) for operation names.
     *
     * @param bundleName The bundle base name to use.
     */
    public static void setBundleName(String bundleName) {
        BUNDLE_NAME = bundleName;
    }
    // endregion

    private final Event event;
    private final String displayName;
    private final int position;

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param event    The event name and resource name in the <code>OperationNames</code> resource bundle.
     * @param position The position of operation in the list of operations.
     */
    public Operation(Event event, int position) {
        this.event = event;
        this.displayName = ResourceBundleHelper.getResourceString(BUNDLE_NAME, this.event.getEventName());
        this.position = position;
    }

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param event       The event name.
     * @param displayName The display name.
     * @param position    The position of operation in the list of operations.
     */
    public Operation(Event event, String displayName, int position) {
        this.event = event;
        this.displayName = displayName;
        this.position = position;
    }

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param event    The event name and resource name in the <code>OperationNames</code> resource bundle.
     * @param locale   The locale for the resource name.
     * @param position The position of operation in the list of operations.
     */
    public Operation(Event event, Locale locale, int position) {
        this.event = event;
        this.displayName = ResourceBundleHelper.getResourceString(BUNDLE_NAME, locale, this.event.getEventName());
        this.position = position;
    }

    /**
     * Instantiates a new instance of class {@link Operation}.
     *
     * @param event        The event name.
     * @param resourceName The resource name in the <code>OperationNames</code> resource bundle.
     * @param locale       The locale for the resource name.
     * @param position     The position of operation in the list of operations.
     */
    public Operation(Event event, String resourceName, Locale locale, int position) {
        this.event = event;
        this.displayName = ResourceBundleHelper.getResourceString(BUNDLE_NAME, locale, resourceName);
        this.position = position;
    }

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return event.getEventName();
    }

    /**
     * Gets the event.
     *
     * @return the event
     */
    public Event getEvent() {
        return event;
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
                Objects.equals(event, operation.event) &&
                Objects.equals(displayName, operation.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, displayName, position);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Operation.class.getSimpleName() + "[", "]")
                .add("event='" + event + "'")
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
        return getEventName();
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
