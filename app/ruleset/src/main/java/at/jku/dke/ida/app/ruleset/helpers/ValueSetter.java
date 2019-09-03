package at.jku.dke.ida.app.ruleset.helpers;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.similarity.ComparativeMeasureSimilarity;
import at.jku.dke.ida.data.models.similarity.ComparativeSimilarity;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.data.models.labels.DimensionLabel;
import at.jku.dke.ida.data.models.similarity.Similarity;
import at.jku.dke.ida.data.repositories.ComparativeMeasureRepository;
import at.jku.dke.ida.data.repositories.JoinConditionPredicateRepository;
import at.jku.dke.ida.data.repositories.LevelRepository;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.PatternPart;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains some helper methods used in the rules for setting values.
 */
public final class ValueSetter {

    private static final Logger LOGGER = LogManager.getLogger(ValueSetter.class);

    /**
     * Prevents creation of instances of this class.
     */
    private ValueSetter() {
    }

    /**
     * Sets the cube of the analysis situation to the given value.
     *
     * @param lang The requested language.
     * @param as   The analysis situation.
     * @param cube The cube.
     */
    public static void setCube(String lang, NonComparativeAnalysisSituation as, String cube) {
        as.setCube(cube);

        try {
            var baseLevels = BeanUtil.getBean(LevelRepository.class).getTopLevelLabelsByLangAndCube(lang, cube);
            for (DimensionLabel lvl : baseLevels) {
                var dq = new DimensionQualification(lvl.getDimensionUri());
                dq.setGranularityLevel(lvl.getUri());
                as.addDimensionQualification(dq);
            }
        } catch (QueryException ex) {
            LOGGER.fatal("Cannot set dimension qualifications for cube " + cube + " as there occurred an error while querying the levels.", ex);
        }
    }

    /**
     * Sets the aggregate measures associated with the selected scores.
     *
     * @param lang        The language.
     * @param as          The comparative analysis situation.
     * @param compMeasure The comparative measures.
     */
    public static void setScoreMeasures(String lang, ComparativeAnalysisSituation as, Set<CubeSimilarity> compMeasure) {
        var cm = compMeasure.stream().map(x -> (ComparativeMeasureSimilarity) x).collect(Collectors.groupingBy(Similarity::getElement));

        // Set cube
        var first = cm.values().iterator().next();
        if (as.getContextOfInterest().getCube() == null)
            setCube(lang, as.getContextOfInterest(), first.stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST).findFirst().orElseThrow().getCube());
        if (as.getContextOfComparison().getCube() == null)
            setCube(lang, as.getContextOfComparison(), first.stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON).findFirst().orElseThrow().getCube());

        // Set measures
        for (String key : cm.keySet()) {
            as.getContextOfInterest().addMeasure(cm.get(key).stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST).findFirst().orElseThrow().getMeasure());
            as.getContextOfComparison().addMeasure(cm.get(key).stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON).findFirst().orElseThrow().getMeasure());
        }
    }

    /**
     * Sets the scores and aggregate measures based on the given comparative measure predicates.
     *
     * @param lang             The language.
     * @param as               The comparative analysis situation.
     * @param compMeasurePreds The comparative measure predicates.
     */
    public static void setScoreMeasurePredicates(String lang, ComparativeAnalysisSituation as, Set<CubeSimilarity> compMeasurePreds) {
        var cm = compMeasurePreds.stream().map(x -> (ComparativeMeasureSimilarity) x).collect(Collectors.groupingBy(Similarity::getElement));

        // Set cube
        var first = cm.values().iterator().next();
        if (as.getContextOfInterest().getCube() == null)
            setCube(lang, as.getContextOfInterest(), first.stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST).findFirst().orElseThrow().getCube());
        if (as.getContextOfComparison().getCube() == null)
            setCube(lang, as.getContextOfComparison(), first.stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON).findFirst().orElseThrow().getCube());

        // Set Predicates
        cm.values().forEach(x -> {
            try {
                as.addScore(BeanUtil.getBean(ComparativeMeasureRepository.class).getAllByPredicate(x.iterator().next().getElement()).stream().findFirst().orElseThrow());
            } catch (QueryException ex) {
                LOGGER.error("Could not load comparative measures of predicate.", ex);
            }
            x.forEach(s -> {
                switch (s.getPatternPart()) {
                    case SET_OF_INTEREST:
                        as.getContextOfInterest().addMeasure(s.getMeasure());
                        break;
                    case SET_OF_COMPARISON:
                        as.getContextOfComparison().addMeasure(s.getMeasure());
                        break;
                }
            });
        });
    }

    /**
     * Sets the granularity levels based on the given join condition predicate.
     *
     * @param lang          The language.
     * @param as            The comparative analysis situation.
     * @param joinCondPreds The join condition predicates.
     */
    public static void setJoinConditionPredicates(String lang, ComparativeAnalysisSituation as, Set<CubeSimilarity> joinCondPreds) {
        var jc = joinCondPreds.stream().map(x -> (ComparativeSimilarity) x).collect(Collectors.groupingBy(Similarity::getElement));

        // Set cube
        var first = jc.values().iterator().next();
        if (as.getContextOfInterest().getCube() == null)
            setCube(lang, as.getContextOfInterest(), first.stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_INTEREST).findFirst().orElseThrow().getCube());
        if (as.getContextOfComparison().getCube() == null)
            setCube(lang, as.getContextOfComparison(), first.stream().filter(x -> x.getPatternPart() == PatternPart.SET_OF_COMPARISON).findFirst().orElseThrow().getCube());

        // Set Granularity levels
        var repo = BeanUtil.getBean(JoinConditionPredicateRepository.class);
        for (String element : jc.keySet()) {
            try {
                var levels = repo.getLevelAndDimensionForPredicate(element);
                for (Triple<String, String, String> triple : levels) {
                    if (triple.getRight().equals("setOfInterest"))
                        as.getContextOfInterest().getDimensionQualification(triple.getLeft()).setGranularityLevel(triple.getMiddle());
                    else if (triple.getRight().equals("setOfComparison"))
                        as.getContextOfComparison().getDimensionQualification(triple.getLeft()).setGranularityLevel(triple.getMiddle());
                }
            } catch (QueryException ex) {
                LOGGER.error("Could not load dimension and level of join condition predicate.", ex);
            }
        }
    }
}
