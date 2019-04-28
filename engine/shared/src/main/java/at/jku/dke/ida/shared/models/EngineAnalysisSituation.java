package at.jku.dke.ida.shared.models;

/**
 * Interface for analysis situations used in the state-chart and drools rules.
 */
public interface EngineAnalysisSituation {

    /**
     * Returns whether the analysis situation is executable, i.e. all necessary fields are filled.
     *
     * @return {@code true} if the analysis situation is executable; {@code false} otherwise.
     */
    boolean isExecutable();

    /**
     * Returns whether the cube(s) is/are already defined.
     *
     * @return {@code true} if the cube(s) is/are defined; {@code false} otherwise.
     */
    boolean isCubeDefined();

}
