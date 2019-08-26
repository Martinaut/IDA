package at.jku.dke.ida.rules.interfaces;

import at.jku.dke.ida.shared.display.Display;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.PatternService}.
 */
public interface PatternServiceModel extends ServiceModel {

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    String getUserInput();

}
