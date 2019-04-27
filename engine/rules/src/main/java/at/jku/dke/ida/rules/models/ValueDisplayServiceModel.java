package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.data.repositories.*;
import at.jku.dke.ida.shared.models.AnalysisSituation;

import java.util.*;

/**
 * Model required by {@link at.jku.dke.ida.rules.services.ValueDisplayService}.
 */
public class ValueDisplayServiceModel extends DroolsServiceModel {

    private final SimpleRepository simpleRepository;
    private final CubeRepository cubeRepository;
    private final AggregateMeasureRepository aggregateMeasureRepository;
    private final GranularityLevelRepository granularityLevelRepository;
    private final LevelPredicateRepository levelPredicateRepository;
    private final BaseMeasurePredicateRepository baseMeasurePredicateRepository;
    private final AggregateMeasurePredicateRepository aggregateMeasurePredicateRepository;
    private final LevelMemberRepository levelMemberRepository;

    /**
     * Instantiates a new instance of class {@link ValueDisplayServiceModel}.
     *
     * @param currentState                        The current state of the state machine.
     * @param locale                              The display locale.
     * @param analysisSituation                   The analysis situation.
     * @param operation                           The operation the user wants to perform.
     * @param additionalData                      Additional data.
     * @param simpleRepository                    The simple repository.
     * @param cubeRepository                      The cube repository.
     * @param aggregateMeasureRepository          The aggregate measure repository.
     * @param granularityLevelRepository          The granularity level repository.
     * @param levelPredicateRepository            The level predicate repository.
     * @param baseMeasurePredicateRepository      The base measure predicate repository.
     * @param aggregateMeasurePredicateRepository The aggregate measure predicate repository.
     * @param levelMemberRepository               The level member repository.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public ValueDisplayServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData,
                                    SimpleRepository simpleRepository, CubeRepository cubeRepository, AggregateMeasureRepository aggregateMeasureRepository,
                                    GranularityLevelRepository granularityLevelRepository, LevelPredicateRepository levelPredicateRepository,
                                    BaseMeasurePredicateRepository baseMeasurePredicateRepository, AggregateMeasurePredicateRepository aggregateMeasurePredicateRepository,
                                    LevelMemberRepository levelMemberRepository) {
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
        if (baseMeasurePredicateRepository == null)
            throw new IllegalArgumentException("baseMeasurePredicateRepository must not be null");
        if (aggregateMeasurePredicateRepository == null)
            throw new IllegalArgumentException("aggregateMeasurePredicateRepository must not be null");
        if (levelMemberRepository == null)
            throw new IllegalArgumentException("levelMemberRepository must not be null");

        this.simpleRepository = simpleRepository;
        this.cubeRepository = cubeRepository;
        this.aggregateMeasureRepository = aggregateMeasureRepository;
        this.granularityLevelRepository = granularityLevelRepository;
        this.levelPredicateRepository = levelPredicateRepository;
        this.baseMeasurePredicateRepository = baseMeasurePredicateRepository;
        this.aggregateMeasurePredicateRepository = aggregateMeasurePredicateRepository;
        this.levelMemberRepository = levelMemberRepository;
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

    /**
     * Gets the base measure predicate repository.
     *
     * @return the base measure predicate repository
     */
    public BaseMeasurePredicateRepository getBaseMeasurePredicateRepository() {
        return baseMeasurePredicateRepository;
    }

    /**
     * Gets the aggregate measure predicate repository.
     *
     * @return the aggregate measure predicate repository
     */
    public AggregateMeasurePredicateRepository getAggregateMeasurePredicateRepository() {
        return aggregateMeasurePredicateRepository;
    }

    /**
     * Gets the level member repository.
     *
     * @return the level member repository
     */
    public LevelMemberRepository getLevelMemberRepository() {
        return levelMemberRepository;
    }
}
