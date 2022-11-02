package at.jku.dke.ida.shared;

/**
 * This class contains constant IRIs for usage in Java-Code.
 */
public final class IRIConstants {

    // region --- TYPES ---
    /**
     * IRI of Type: Base Cube
     */
    public static final String TYPE_CUBE = "http://dke.jku.at/inga/cubes#BaseCube";

    /**
     * IRI of Type: Base Measure
     */
    public static final String TYPE_BASE_MEASURE = "http://dke.jku.at/inga/cubes#BaseMeasure";

    /**
     * IRI of Type: Aggregate Measure
     */
    public static final String TYPE_AGGREGATE_MEASURE = "http://dke.jku.at/inga/cubes#AggregateMeasure";

    /**
     * IRI of Type: Comparative Measure
     */
    public static final String TYPE_COMPARATIVE_MEASURE = "http://dke.jku.at/inga/cubes#ComparativeMeasure";

    /**
     * IRI of Type: Dimension
     */
    public static final String TYPE_DIMENSION = "http://dke.jku.at/inga/cubes#Dimension";

    /**
     * IRI of Type: Level
     */
    public static final String TYPE_LEVEL = "http://dke.jku.at/inga/cubes#Level";

    /**
     * IRI of Type: Level Predicate
     */
    public static final String TYPE_LEVEL_PREDICATE = "http://dke.jku.at/inga/cubes#LevelPredicate";

    /**
     * IRI of Type: Conjunctive Level Predicate
     */
    public static final String TYPE_CONJUNCTIVE_LEVEL_PREDICATE = "http://dke.jku.at/inga/cubes#ConjunctiveLevelPredicate";

    /**
     * IRI of Type: Base Measure Predicate
     */
    public static final String TYPE_BASE_MEASURE_PREDICATE = "http://dke.jku.at/inga/cubes#BaseMeasurePredicate";

    /**
     * IRI of Type: Aggregate Measure Predicate
     */
    public static final String TYPE_AGGREGATE_MEASURE_PREDICATE = "http://dke.jku.at/inga/cubes#AggregateMeasurePredicate";

    /**
     * IRI of Type: Comparative Measure Predicate
     */
    public static final String TYPE_COMPARATIVE_MEASURE_PREDICATE = "http://dke.jku.at/inga/cubes#ComparativeMeasurePredicate";

    /**
     * IRI of Type: Join Condition Predicate
     */
    public static final String TYPE_JOIN_CONDITION_PREDICATE = "http://dke.jku.at/inga/cubes#JoinConditionPredicate";

    /**
     * IRI of Type: Level Member
     */
    public static final String TYPE_LEVEL_MEMBER = "http://dke.jku.at/inga/cubes#LevelMember";
    // endregion

    // region --- RESOURCE ---
    /**
     * Constant resource "top" level.
     */
    public static final String RESOURCE_TOP_LEVEL = "http://dke.jku.at/ida/constants#top";

    /**
     * Constant resource "all" dice node.
     */
    public static final String RESOURCE_ALL_NODES = "http://dke.jku.at/ida/constants#all";
    // endregion

    /**
     * Prevents creation of instances of this class.
     */
    private IRIConstants() {
    }
}
