package at.jku.dke.ida.rules.interfaces;

import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.operations.Operation;

import java.util.Map;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.ValueIntentService}.
 */
public interface ValueIntentServiceModel extends ServiceModel {

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
