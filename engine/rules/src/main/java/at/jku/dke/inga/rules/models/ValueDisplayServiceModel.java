package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.data.repositories.AggregateMeasureRepository;
import at.jku.dke.inga.data.repositories.CubeRepository;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import com.google.common.graph.Graph;
import com.google.common.graph.ImmutableGraph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.ValueDisplayService}.
 */
public class ValueDisplayServiceModel extends DroolsServiceModel {

    private final CubeRepository cubeRepository;
    private final AggregateMeasureRepository aggregateMeasureRepository;
    private final GranularityLevelRepository granularityLevelRepository;

    /**
     * Instantiates a new instance of class {@link ValueDisplayServiceModel}.
     *
     * @param currentState               The current state of the state machine.
     * @param locale                     The display locale.
     * @param analysisSituation          The analysis situation.
     * @param operation                  The operation the user wants to perform.
     * @param additionalData             Additional data.
     * @param aggregateMeasureRepository The aggregate measure repository.
     * @param granularityLevelRepository The granularity level repository.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public ValueDisplayServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData,
                                    CubeRepository cubeRepository, AggregateMeasureRepository aggregateMeasureRepository, GranularityLevelRepository granularityLevelRepository) {
        super(currentState, locale, analysisSituation, operation, additionalData);

        if (operation == null)
            throw new IllegalArgumentException("operation must not be null");
        if (aggregateMeasureRepository == null)
            throw new IllegalArgumentException("aggregateMeasureRepository must not be null");
        if (granularityLevelRepository == null)
            throw new IllegalArgumentException("granularityLevelRepository must not be null");
        if (cubeRepository == null)
            throw new IllegalArgumentException("cubeRepository must not be null");

        this.cubeRepository = cubeRepository;
        this.aggregateMeasureRepository = aggregateMeasureRepository;
        this.granularityLevelRepository = granularityLevelRepository;
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
