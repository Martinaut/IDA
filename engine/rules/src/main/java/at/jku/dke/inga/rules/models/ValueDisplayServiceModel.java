package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.data.repositories.*;
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

    private final SimpleRepository simpleRepository;
    private final CubeRepository cubeRepository;
    private final AggregateMeasureRepository aggregateMeasureRepository;
    private final GranularityLevelRepository granularityLevelRepository;
    private final LevelPredicateRepository levelPredicateRepository;

    /**
     * Instantiates a new instance of class {@link ValueDisplayServiceModel}.
     *
     * @param currentState               The current state of the state machine.
     * @param locale                     The display locale.
     * @param analysisSituation          The analysis situation.
     * @param operation                  The operation the user wants to perform.
     * @param additionalData             Additional data.
     * @param simpleRepository The simple repository.
     * @param aggregateMeasureRepository The aggregate measure repository.
     * @param granularityLevelRepository The granularity level repository.
     * @param levelPredicateRepository   The level predicate repository.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public ValueDisplayServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData,
                                    SimpleRepository simpleRepository, CubeRepository cubeRepository, AggregateMeasureRepository aggregateMeasureRepository,
                                    GranularityLevelRepository granularityLevelRepository, LevelPredicateRepository levelPredicateRepository) {
        super(currentState, locale, analysisSituation, operation, additionalData);

        if (operation == null)
            throw new IllegalArgumentException("operation must not be null");
        if (simpleRepository == null)
            throw new IllegalArgumentException("simpleRepository must not be null");
        if (aggregateMeasureRepository == null)
            throw new IllegalArgumentException("aggregateMeasureRepository must not be null");
        if (granularityLevelRepository == null)
            throw new IllegalArgumentException("granularityLevelRepository must not be null");
        if (cubeRepository == null)
            throw new IllegalArgumentException("cubeRepository must not be null");
        if (levelPredicateRepository == null)
            throw new IllegalArgumentException("levelPredicateRepository must not be null");

        this.simpleRepository = simpleRepository;
        this.cubeRepository = cubeRepository;
        this.aggregateMeasureRepository = aggregateMeasureRepository;
        this.granularityLevelRepository = granularityLevelRepository;
        this.levelPredicateRepository = levelPredicateRepository;
    }

    /**
     * Gets the simple repository.
     *
     * @return the simple repository
     */
    public SimpleRepository getSimpleRepository() {
        return simpleRepository;
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

    /**
     * Gets the level predicate repository.
     *
     * @return the level predicate repository
     */
    public LevelPredicateRepository getLevelPredicateRepository() {
        return levelPredicateRepository;
    }
}
