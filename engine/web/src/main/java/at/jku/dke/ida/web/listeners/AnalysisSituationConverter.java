package at.jku.dke.ida.web.listeners;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.repositories.SimpleRepository;
import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.web.models.analysissituation.ComparativeAnalysisSituationDisplay;
import at.jku.dke.ida.web.models.analysissituation.DimensionQualificationDisplay;
import at.jku.dke.ida.web.models.analysissituation.NonComparativeAnalysisSituationDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Converts analysis situations used in the state chart to analysis situations for display.
 */
final class AnalysisSituationConverter {

    private static final Logger LOGGER = LogManager.getLogger(AnalysisSituationConverter.class);

    /**
     * Prevents creation of instances of this class.
     */
    private AnalysisSituationConverter() {
    }

    /**
     * Convert the analysis situation to a displayable analysis situation.
     *
     * @param lang The requested language.
     * @param repo The repository.
     * @param as   The analysis situation to convert.
     * @return The displayable analysis situation.
     * @throws IllegalArgumentException If {@code lang}, {@code as} or {@code repo} is {@code null} or if {@code as} is not an
     *                                  instance of {@link NonComparativeAnalysisSituation} or {@link ComparativeAnalysisSituation}.
     */
    static AnalysisSituation convert(String lang, SimpleRepository repo, EngineAnalysisSituation as) {
        if (lang == null || lang.isBlank()) throw new IllegalArgumentException("lang must not be empty");
        if (repo == null) throw new IllegalArgumentException("repo must not be null");
        if (as == null) throw new IllegalArgumentException("as must not be null");

        if (as instanceof NonComparativeAnalysisSituation) {
            return translate(lang, repo, (NonComparativeAnalysisSituation) as);
        } else {
            if (as instanceof ComparativeAnalysisSituation) {
                return translate(lang, repo, (ComparativeAnalysisSituation) as);
            } else {
                LOGGER.error("The analysis situation is of unsupported type {}.", as.getClass());
                throw new IllegalArgumentException("Analysis Situation is of an invalid type.");
            }
        }
    }

    private static NonComparativeAnalysisSituationDisplay translate(String lang, SimpleRepository repo, NonComparativeAnalysisSituation as) {
        LOGGER.debug("Translating NonComparativeAnalysisSituation with cube {} for language {}.", as.getCube(), lang);

        // region Get Labels
        Set<String> uris = new HashSet<>();
        uris.add(as.getCube());
        uris.addAll(as.getMeasures());
        uris.addAll(as.getBaseMeasureConditions());
        uris.addAll(as.getFilterConditions());
        as.getDimensionQualifications().forEach(dq -> {
            uris.add(dq.getDimension());
            uris.add(dq.getGranularityLevel());
            uris.add(dq.getDiceNode());
            uris.add(dq.getDiceLevel());
            uris.addAll(dq.getSliceConditions());
        });
        // endregion

        try {
            Map<String, Label> lbls = repo.getLabelsByLangAndIris(lang, uris);
            NonComparativeAnalysisSituationDisplay newAs = new NonComparativeAnalysisSituationDisplay();
            // region Set Labels
            newAs.setCube(lbls.getOrDefault(as.getCube(), new Label(as.getCube())));
            as.getMeasures().forEach(m -> newAs.addMeasure(lbls.getOrDefault(m, new Label(m))));
            as.getBaseMeasureConditions().forEach(bm -> newAs.addBaseMeasureCondition(lbls.getOrDefault(bm, new Label(bm))));
            as.getFilterConditions().forEach(f -> newAs.addFilterCondition(lbls.getOrDefault(f, new Label(f))));
            as.getDimensionQualifications().forEach(dq -> {
                DimensionQualificationDisplay mapped = new DimensionQualificationDisplay(lang);
                mapped.setDimension(lbls.getOrDefault(dq.getDimension(), new Label(dq.getDimension())));
                mapped.setGranularityLevel(lbls.getOrDefault(dq.getGranularityLevel(), new Label(dq.getGranularityLevel())));
                mapped.setDiceNode(lbls.getOrDefault(dq.getDiceNode(), new Label(dq.getDiceNode())));
                mapped.setDiceLevel(lbls.getOrDefault(dq.getDiceLevel(), new Label(dq.getDiceLevel())));
                dq.getSliceConditions().forEach(sc -> mapped.addSliceCondition(lbls.getOrDefault(sc, new Label(sc))));
                newAs.addDimensionQualification(mapped);
            });
            // endregion
            return newAs;
        } catch (QueryException ex) {
            LOGGER.error("Could not load translations of NonComparativeAnalysisSituation.", ex);
        }
        return null;
    }

    private static AnalysisSituation translate(String lang, SimpleRepository repo, ComparativeAnalysisSituation as) {
        LOGGER.debug("Translating ComparativeAnalysisSituation with cubes {} and {} for language {}.", as.getContextOfInterest().getCube(), as.getContextOfComparison().getCube(), lang);

        ComparativeAnalysisSituationDisplay newAs = new ComparativeAnalysisSituationDisplay(
                translate(lang, repo, as.getContextOfInterest()),
                translate(lang, repo, as.getContextOfComparison())
        );

        // region Get Labels
        Set<String> uris = new HashSet<>();
        uris.addAll(as.getJoinConditions());
        uris.addAll(as.getScores());
        uris.addAll(as.getScoreFilters());
        // endregion

        try {
            Map<String, Label> lbls = repo.getLabelsByLangAndIris(lang, uris);
            // region Set Labels
            as.getScores().forEach(s -> newAs.addScore(lbls.getOrDefault(s, new Label(s))));
            as.getScoreFilters().forEach(sf -> newAs.addScoreFilter(lbls.getOrDefault(sf, new Label(sf))));
            as.getJoinConditions().forEach(jc -> newAs.addJoinCondition(lbls.getOrDefault(jc, new Label(jc))));
            // endregion
            return newAs;
        } catch (QueryException ex) {
            LOGGER.error("Could not load translations of ComparativeAnalysisSituation.", ex);
        }
        return null;
    }
}
