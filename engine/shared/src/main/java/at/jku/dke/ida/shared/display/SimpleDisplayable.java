package at.jku.dke.ida.shared.display;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A simple class only with setters implementing {@linkplain Displayable}.
 */
public class SimpleDisplayable implements Displayable {

    private final String displayableId;
    private final String title;
    private final String details;

    /**
     * Instantiates a new instance of class {@linkplain SimpleDisplayable}.
     *
     * @param displayableId the displayable id
     * @param title         the title
     * @param details       the details
     */
    public SimpleDisplayable(String displayableId, String title, String details) {
        this.displayableId = displayableId;
        this.title = title;
        this.details = details;
    }

    /**
     * Returns the identifier of the item.
     *
     * @return The unique identifier.
     */
    @Override
    public String getDisplayableId() {
        return displayableId;
    }

    /**
     * Returns the title of the item.
     *
     * @return The title.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Returns the details of the item.
     * Details can be for example a description of the item.
     *
     * @return The details.
     */
    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleDisplayable that = (SimpleDisplayable) o;
        return Objects.equals(displayableId, that.displayableId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayableId, title, details);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SimpleDisplayable.class.getSimpleName() + "[", "]")
                .add("displayableId='" + displayableId + "'")
                .add("title='" + title + "'")
                .add("details='" + details + "'")
                .toString();
    }
}
