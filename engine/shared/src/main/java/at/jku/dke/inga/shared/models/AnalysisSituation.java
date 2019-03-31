package at.jku.dke.inga.shared.models;

import java.util.StringJoiner;

/**
 * Base-class for all analysis situation types.
 */
public abstract class AnalysisSituation {

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituation}.
     */
    protected AnalysisSituation() {
    }

    /**
     * Returns whether the analysis situation is executable, i.e. all necessary fields are filled.
     *
     * @return {@code true} if the analysis situation is executable; {@code false} otherwise.
     */
    public abstract boolean isExecutable();

    /**
     * Returns whether the cube(s) is/are already defined.
     *
     * @return {@code true} if the cube(s) is/are defined; {@code false} otherwise.
     */
    public abstract boolean isCubeDefined();

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituation.class.getSimpleName() + "[", "]")
                .toString();
    }
}
