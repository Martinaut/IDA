package at.jku.dke.inga.web.models;

import at.jku.dke.inga.shared.display.Display;

/**
 * Represents a result that is sent to the user.
 */
public class DisplayResult {

    private String type;
    private Display display;

    /**
     * Instantiates a new instance of class {@linkplain DisplayResult}.
     *
     * @param display The display data.
     */
    public DisplayResult(Display display) {
        this.type = display.getClass().getSimpleName();
        this.display = display;
    }

    /**
     * Gets the type of the display data.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the display data.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the display data.
     *
     * @return the display
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Sets the display data.
     *
     * @param display the display
     */
    public void setDisplay(Display display) {
        this.display = display;
    }

}
