package at.jku.dke.inga.shared.models;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Base-class for all analysis situation types.
 */
public abstract class AnalysisSituation {

    private String name;
    private String description;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituation}.
     */
    protected AnalysisSituation() {
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituation.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .toString();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisSituation that = (AnalysisSituation) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
