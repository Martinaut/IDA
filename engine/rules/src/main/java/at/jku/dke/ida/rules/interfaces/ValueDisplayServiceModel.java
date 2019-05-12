package at.jku.dke.ida.rules.interfaces;

import at.jku.dke.ida.data.repositories.*;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.ValueDisplayService}.
 */
public interface ValueDisplayServiceModel extends ServiceModel {

    /**
     * Gets the simple repository.
     *
     * @return the simple repository
     */
    SimpleRepository getSimpleRepository();

    /**
     * Gets the cube repository.
     *
     * @return the cube repository
     */
    CubeRepository getCubeRepository();

    /**
     * Gets the aggregate measure repository.
     *
     * @return the aggregate measure repository
     */
    AggregateMeasureRepository getAggregateMeasureRepository();

    /**
     * Gets the granularity level repository.
     *
     * @return the granularity level repository
     */
    LevelRepository getGranularityLevelRepository();

    /**
     * Gets the level predicate repository.
     *
     * @return the level predicate repository
     */
    LevelPredicateRepository getLevelPredicateRepository();

    /**
     * Gets the base measure predicate repository.
     *
     * @return the base measure predicate repository
     */
    BaseMeasurePredicateRepository getBaseMeasurePredicateRepository();

    /**
     * Gets the aggregate measure predicate repository.
     *
     * @return the aggregate measure predicate repository
     */
    AggregateMeasurePredicateRepository getAggregateMeasurePredicateRepository();

    /**
     * Gets the level member repository.
     *
     * @return the level member repository
     */
    LevelMemberRepository getLevelMemberRepository();

    /**
     * Gets the join condition predicate repository.
     *
     * @return the join condition predicate repository
     */
    JoinConditionPredicateRepository getJoinConditionPredicateRepository();

    /**
     * Gets the comparative measure repository.
     *
     * @return the comparative measure repository
     */
    ComparativeMeasureRepository getComparativeMeasureRepository();

    /**
     * Gets the comparative measure predicate repository.
     *
     * @return the comparative measure predicate repository
     */
    ComparativeMeasurePredicateRepository getComparativeMeasurePredicateRepository();

    /**
     * A flag whether to skip displaying the values and to go directly to "DeterminingValueInputIntent".
     *
     * @return {@code true} if display values should be skipped; {@code false otherwise}.
     */
    default boolean skipValueDisplay() {
        return false;
    }
}
