package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.data.repositories.*;
import at.jku.dke.ida.rules.interfaces.ValueDisplayServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.*;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.ValueDisplayService}.
 */
public class DefaultValueDisplayServiceModel extends AbstractServiceModel implements ValueDisplayServiceModel {

    private final SimpleRepository simpleRepository;
    private final CubeRepository cubeRepository;
    private final AggregateMeasureRepository aggregateMeasureRepository;
    private final LevelRepository granularityLevelRepository;
    private final LevelPredicateRepository levelPredicateRepository;
    private final BaseMeasurePredicateRepository baseMeasurePredicateRepository;
    private final AggregateMeasurePredicateRepository aggregateMeasurePredicateRepository;
    private final LevelMemberRepository levelMemberRepository;
    private final JoinConditionPredicateRepository joinConditionPredicateRepository;
    private final ComparativeMeasureRepository comparativeMeasureRepository;
    private final ComparativeMeasurePredicateRepository comparativeMeasurePredicateRepository;
    private boolean skipDisplay;

    /**
     * Instantiates a new instance of class {@link DefaultValueDisplayServiceModel}.
     *
     * @param currentState                        The current state of the state machine.
     * @param sessionModel                        The session model.
     * @param simpleRepository                    The simple repository.
     * @param cubeRepository                      The cube repository.
     * @param aggregateMeasureRepository          The aggregate measure repository.
     * @param granularityLevelRepository          The granularity level repository.
     * @param levelPredicateRepository            The level predicate repository.
     * @param baseMeasurePredicateRepository      The base measure predicate repository.
     * @param aggregateMeasurePredicateRepository The aggregate measure predicate repository.
     * @param levelMemberRepository               The level member repository.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultValueDisplayServiceModel(String currentState, SessionModel sessionModel, SimpleRepository simpleRepository,
                                           CubeRepository cubeRepository, AggregateMeasureRepository aggregateMeasureRepository,
                                           LevelRepository granularityLevelRepository, LevelPredicateRepository levelPredicateRepository,
                                           BaseMeasurePredicateRepository baseMeasurePredicateRepository,
                                           AggregateMeasurePredicateRepository aggregateMeasurePredicateRepository, LevelMemberRepository levelMemberRepository,
                                           JoinConditionPredicateRepository joinConditionPredicateRepository,
                                           ComparativeMeasureRepository comparativeMeasureRepository, ComparativeMeasurePredicateRepository comparativeMeasurePredicateRepository) {
        super(currentState, sessionModel);

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
        if (joinConditionPredicateRepository == null)
            throw new IllegalArgumentException("joinConditionPredicateRepository must not be null");
        if (comparativeMeasureRepository == null)
            throw new IllegalArgumentException("comparativeMeasureRepository must not be null");
        if (comparativeMeasurePredicateRepository == null)
            throw new IllegalArgumentException("comparativeMeasurePredicateRepository must not be null");

        this.simpleRepository = simpleRepository;
        this.cubeRepository = cubeRepository;
        this.aggregateMeasureRepository = aggregateMeasureRepository;
        this.granularityLevelRepository = granularityLevelRepository;
        this.levelPredicateRepository = levelPredicateRepository;
        this.baseMeasurePredicateRepository = baseMeasurePredicateRepository;
        this.aggregateMeasurePredicateRepository = aggregateMeasurePredicateRepository;
        this.levelMemberRepository = levelMemberRepository;
        this.joinConditionPredicateRepository = joinConditionPredicateRepository;
        this.comparativeMeasureRepository = comparativeMeasureRepository;
        this.comparativeMeasurePredicateRepository = comparativeMeasurePredicateRepository;
        this.skipDisplay = false;
    }

    /**
     * Instantiates a new instance of class {@link DefaultValueDisplayServiceModel}.
     *
     * @param currentState                        The current state of the state machine.
     * @param locale                              The display locale.
     * @param analysisSituation                   The analysis situation.
     * @param operation                           The operation the user wants to perform.
     * @param sessionModel                        The session model.
     * @param simpleRepository                    The simple repository.
     * @param cubeRepository                      The cube repository.
     * @param aggregateMeasureRepository          The aggregate measure repository.
     * @param granularityLevelRepository          The granularity level repository.
     * @param levelPredicateRepository            The level predicate repository.
     * @param baseMeasurePredicateRepository      The base measure predicate repository.
     * @param aggregateMeasurePredicateRepository The aggregate measure predicate repository.
     * @param levelMemberRepository               The level member repository.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale}).
     */
    public DefaultValueDisplayServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel,
                                           SimpleRepository simpleRepository, CubeRepository cubeRepository, AggregateMeasureRepository aggregateMeasureRepository,
                                           LevelRepository granularityLevelRepository, LevelPredicateRepository levelPredicateRepository,
                                           BaseMeasurePredicateRepository baseMeasurePredicateRepository, AggregateMeasurePredicateRepository aggregateMeasurePredicateRepository,
                                           LevelMemberRepository levelMemberRepository, JoinConditionPredicateRepository joinConditionPredicateRepository,
                                           ComparativeMeasureRepository comparativeMeasureRepository, ComparativeMeasurePredicateRepository comparativeMeasurePredicateRepository) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

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
        if (joinConditionPredicateRepository == null)
            throw new IllegalArgumentException("joinConditionPredicateRepository must not be null");
        if (comparativeMeasureRepository == null)
            throw new IllegalArgumentException("comparativeMeasureRepository must not be null");
        if (comparativeMeasurePredicateRepository == null)
            throw new IllegalArgumentException("comparativeMeasurePredicateRepository must not be null");

        this.simpleRepository = simpleRepository;
        this.cubeRepository = cubeRepository;
        this.aggregateMeasureRepository = aggregateMeasureRepository;
        this.granularityLevelRepository = granularityLevelRepository;
        this.levelPredicateRepository = levelPredicateRepository;
        this.baseMeasurePredicateRepository = baseMeasurePredicateRepository;
        this.aggregateMeasurePredicateRepository = aggregateMeasurePredicateRepository;
        this.levelMemberRepository = levelMemberRepository;
        this.joinConditionPredicateRepository = joinConditionPredicateRepository;
        this.comparativeMeasureRepository = comparativeMeasureRepository;
        this.comparativeMeasurePredicateRepository = comparativeMeasurePredicateRepository;
        this.skipDisplay = false;
    }

    @Override
    public SimpleRepository getSimpleRepository() {
        return simpleRepository;
    }

    @Override
    public CubeRepository getCubeRepository() {
        return cubeRepository;
    }

    @Override
    public AggregateMeasureRepository getAggregateMeasureRepository() {
        return aggregateMeasureRepository;
    }

    @Override
    public LevelRepository getGranularityLevelRepository() {
        return granularityLevelRepository;
    }

    @Override
    public LevelPredicateRepository getLevelPredicateRepository() {
        return levelPredicateRepository;
    }

    @Override
    public BaseMeasurePredicateRepository getBaseMeasurePredicateRepository() {
        return baseMeasurePredicateRepository;
    }

    @Override
    public AggregateMeasurePredicateRepository getAggregateMeasurePredicateRepository() {
        return aggregateMeasurePredicateRepository;
    }

    @Override
    public LevelMemberRepository getLevelMemberRepository() {
        return levelMemberRepository;
    }

    @Override
    public JoinConditionPredicateRepository getJoinConditionPredicateRepository() {
        return joinConditionPredicateRepository;
    }

    @Override
    public ComparativeMeasureRepository getComparativeMeasureRepository() {
        return comparativeMeasureRepository;
    }

    @Override
    public ComparativeMeasurePredicateRepository getComparativeMeasurePredicateRepository() {
        return comparativeMeasurePredicateRepository;
    }

    @Override
    public boolean skipValueDisplay() {
        return this.skipDisplay;
    }

    /**
     * Sets whether to skip the display.
     *
     * @param skipDisplay {@code true} if display values should be skipped; {@code false otherwise}.
     */
    public void setSkipDisplay(boolean skipDisplay) {
        this.skipDisplay = skipDisplay;
    }
}
