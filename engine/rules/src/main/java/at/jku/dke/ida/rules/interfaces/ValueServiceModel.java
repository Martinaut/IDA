package at.jku.dke.ida.rules.interfaces;

import at.jku.dke.ida.shared.display.Display;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.ValueService}.
 */
public interface ValueServiceModel extends ServiceModel {

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    String getUserInput();

    /**
     * Gets the display data.
     *
     * @return the display data
     */
    Display getDisplayData();

}
