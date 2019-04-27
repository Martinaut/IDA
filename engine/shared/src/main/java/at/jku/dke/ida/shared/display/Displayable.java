package at.jku.dke.ida.shared.display;

/**
 * An interface for all classes that may be displayed in the UI.
 */
public interface Displayable {

    /**
     * Returns the identifier of the item.
     *
     * @return The unique identifier.
     */
    String getDisplayableId();

    /**
     * Returns the title of the item.
     *
     * @return The title.
     */
    String getTitle();

    /**
     * Returns the details of the item.
     * Details can be for example a description of the item.
     *
     * @return The details.
     */
    String getDetails();

}
