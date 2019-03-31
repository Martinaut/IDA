package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.data.repositories.AggregateMeasureRepository;
import at.jku.dke.inga.data.repositories.CubeRepository;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import com.google.common.graph.Graph;
import com.google.common.graph.ImmutableGraph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.ValueDisplayService}.
 */
public class ValueDisplayServiceModel extends DroolsServiceModel {

    private final String operation;
    private final CubeRepository cubeRepository;
    private final AggregateMeasureRepository aggregateMeasureRepository;
    private final GranularityLevelRepository granularityLevelRepository;

    /**
     * Instantiates a new Value display service model.
     *
     * @param currentState               The current state of the state machine.
     * @param analysisSituation          The analysis situation.
     * @param locale                     The display locale.
     * @param operation                  The operation the user wants to perform.
     * @param aggregateMeasureRepository The aggregate measure repository.
     * @param granularityLevelRepository The granularity level repository.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale}).
     */
    public ValueDisplayServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale,
                                    String operation, CubeRepository cubeRepository,
                                    AggregateMeasureRepository aggregateMeasureRepository, GranularityLevelRepository granularityLevelRepository) {
        super(currentState, analysisSituation, locale);

        if (operation == null) throw new IllegalArgumentException("operation must not be null");
        if (aggregateMeasureRepository == null)
            throw new IllegalArgumentException("aggregateMeasureRepository must not be null");
        if (granularityLevelRepository == null)
            throw new IllegalArgumentException("granularityLevelRepository must not be null");
        if (cubeRepository == null)
            throw new IllegalArgumentException("cubeRepository must not be null");

        this.operation = operation;
        this.cubeRepository = cubeRepository;
        this.aggregateMeasureRepository = aggregateMeasureRepository;
        this.granularityLevelRepository = granularityLevelRepository;
    }

    /**
     * Gets the operation the user wants to perform.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the cube repository.
     *
     * @return the cube repository
     */
    public CubeRepository getCubeRepository() {
        return cubeRepository;
    }

    /**
     * Gets the aggregate measure repository.
     *
     * @return the aggregate measure repository
     */
    public AggregateMeasureRepository getAggregateMeasureRepository() {
        return aggregateMeasureRepository;
    }

    /**
     * Gets the granularity level repository.
     *
     * @return the granularity level repository
     */
    public GranularityLevelRepository getGranularityLevelRepository() {
        return granularityLevelRepository;
    }
}
