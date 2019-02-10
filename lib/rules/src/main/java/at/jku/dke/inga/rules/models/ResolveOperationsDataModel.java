package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.data.models.Cube;
import at.jku.dke.inga.data.models.CubeElement;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Data model used by the {@link at.jku.dke.inga.rules.services.OperationsResolver} service.
 */
public class ResolveOperationsDataModel extends DataModel<NonComparativeAnalysisSituation> {

    private final Cube cube;
    private final Collection<CubeElement> cubeElements;

    /**
     * Instantiates a new instance of class {@link ResolveOperationsDataModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    public ResolveOperationsDataModel(String currentState, NonComparativeAnalysisSituation analysisSituation, Locale locale) {
        super(currentState, analysisSituation, locale);

        this.cube = null;
        this.cubeElements = null;
    }

    /**
     * Instantiates a new instance of class {@link ResolveOperationsDataModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @param cube              The selected cube.
     * @param cubeElements      All elements of the selected cube.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    public ResolveOperationsDataModel(String currentState, NonComparativeAnalysisSituation analysisSituation, Locale locale, Cube cube, Collection<CubeElement> cubeElements) {
        super(currentState, analysisSituation, locale);
        this.cube = cube;
        this.cubeElements = Collections.unmodifiableCollection(cubeElements);
    }

    /**
     * Gets the selected cube.
     *
     * @return the cube
     */
    public Cube getCube() {
        return cube;
    }

    /**
     * Gets all elements of the selected cube.
     *
     * @return the cube elements
     */
    public Collection<CubeElement> getCubeElements() {
        return cubeElements;
    }

    // region --- Measures ---

    /**
     * Returns all measures contained in {@link #getCubeElements()} which are not already selected in the analysis situation.
     *
     * @return all measures
     */
    public Collection<CubeElement> getNotSelectedMeasures() {
        if (cubeElements == null) return Collections.emptyList();
        return cubeElements.stream()
                .filter(x -> x.getTypeUri().equals("http://purl.org/linked-data/cube#MeasureProperty"))
                .filter(x -> !getAnalysisSituation().getMeasures().contains(x.getUri()))
                .collect(Collectors.toList());
    }
    // endregion

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResolveOperationsDataModel)) return false;
        if (!super.equals(o)) return false;
        ResolveOperationsDataModel that = (ResolveOperationsDataModel) o;
        return Objects.equals(cube, that.cube) &&
                Objects.equals(cubeElements, that.cubeElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cube, cubeElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", ResolveOperationsDataModel.class.getSimpleName() + "[", "]")
                .add("currentState='" + getCurrentState() + "'")
                .add("analysisSituation=" + getAnalysisSituation())
                .add("locale=" + getLocale())
                .add("cube=" + cube)
                .add("cubeElements=" + cubeElements)
                .toString();
    }
}
