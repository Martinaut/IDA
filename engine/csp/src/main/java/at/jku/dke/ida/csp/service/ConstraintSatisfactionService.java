package at.jku.dke.ida.csp.service;

import at.jku.dke.ida.csp.ConstraintSatisfactionSettings;
import at.jku.dke.ida.csp.domain.AnalysisSituationSolution;
import at.jku.dke.ida.csp.models.FillDeterminedValuesServiceModel;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.data.models.similarity.Similarity;
import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This service performs a constraint satisfaction algorithm to determine the
 * best-fitting analysis situation.
 */
public class ConstraintSatisfactionService {

    private final Logger logger = LogManager.getLogger(ConstraintSatisfactionService.class);
    private Solver<AnalysisSituationSolution> solver;

    /**
     * Instantiates a new instance of class {@linkplain ConstraintSatisfactionService}.
     */
    public ConstraintSatisfactionService() {
    }

    private void initSolver() {
        logger.info("Initializing solver.");
        SolverFactory<AnalysisSituationSolution> solverFactory = SolverFactory.createFromXmlResource("optaplanner/solverConfig.xml");
        this.solver = solverFactory.buildSolver();
    }

    /**
     * Fills the analysis situation.
     * If the {@code similarities}-Set is {@code null} or empty, nothing will be executed.
     *
     * @param sessionModel The session model.
     * @param similarities The set with found similarities.
     * @throws IllegalArgumentException If {@code sessionModel} is {@code null}.
     */
    public void fillAnalysisSituation(SessionModel sessionModel, Set<CubeSimilarity> similarities) {
        if (sessionModel == null) throw new IllegalArgumentException("sessionModel must not be null");
        if (sessionModel.getAnalysisSituation() == null)
            throw new IllegalArgumentException("sessionModel analysis situation must not be null");
        if (similarities == null || similarities.isEmpty()) return;
        if (this.solver == null) initSolver();

        // Build model
        logSimilarities(similarities);
        AnalysisSituationSolution problem = buildProblem(sessionModel.getAnalysisSituation() instanceof ComparativeAnalysisSituation, similarities);

        // Solve
        logger.info("Solving CSP");
        AnalysisSituationSolution solution = solver.solve(problem);
        logger.info("CSP solved");
        logger.info(solver.explainBestScore());
        logSolution(solution);

        // Set values
        if (solution.getScore().getHardScore().compareTo(BigDecimal.ZERO) >= 0)
            new FillDeterminedValuesService().executeRules(new FillDeterminedValuesServiceModel("CSP", sessionModel, solution.getAnalysisSituation()));
        else
            logger.warn("No suitable solution found");
    }

    private AnalysisSituationSolution buildProblem(boolean isComparative, Set<CubeSimilarity> similarities) {
        logger.debug("Building problem");
        ConstraintSatisfactionSettings settings = BeanUtil.getBean(ConstraintSatisfactionSettings.class);
        return new AnalysisSituationSolution(
                isComparative,
                isComparative ? Collections.singleton("") : similarities.stream().map(CubeSimilarity::getCube).collect(Collectors.toSet()),
                settings.isUseAggregateMeasures() && !isComparative ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_AGGREGATE_MEASURE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseAggregateMeasurePredicates() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_AGGREGATE_MEASURE_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseBaseMeasurePredicates() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_BASE_MEASURE_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseLevels() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_LEVEL)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseLevelPredicates() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_LEVEL_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseComparativeMeasures() && isComparative ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_COMPARATIVE_MEASURE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseComparativeMeasurePredicates() && isComparative ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_COMPARATIVE_MEASURE_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseJoinConditionPredicates() && isComparative ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_JOIN_CONDITION_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet()
        );
    }

    private void logSimilarities(Set<CubeSimilarity> similarities) {
        if (!logger.isTraceEnabled()) return;
        var tmp = new ArrayList<>(similarities);
        tmp.sort(Comparator
                .comparing((Function<CubeSimilarity, String>) x -> x.getTerm().getText())
                .thenComparingDouble(Similarity::getScore));
        tmp.forEach(logger::trace);
    }

    private void logSolution(AnalysisSituationSolution ass) {
        if (!logger.isDebugEnabled()) return;

        var as = ass.getAnalysisSituation();
        Function<Set<CubeSimilarity>, String> format = (x) -> x.stream()
                .map(s -> System.lineSeparator() + "\t\t" + s)
                .collect(Collectors.joining());

        String logString = new StringJoiner("," + System.lineSeparator(), "BEST RESULT [" + System.lineSeparator(), System.lineSeparator() + "]")
                .add("\tcube='" + as.getCube() + "'")
                .add("\tmeasures='" + ((as.getAggregateMeasures() == null) ? "[]" : format.apply(as.getAggregateMeasures().getElements())) + "'")
                .add("\tfilters='" + ((as.getAggregateMeasurePredicates() == null) ? "[]" : format.apply(as.getAggregateMeasurePredicates().getElements())) + "'")
                .add("\tbmcs='" + ((as.getBaseMeasurePredicates() == null) ? "[]" : format.apply(as.getBaseMeasurePredicates().getElements())) + "'")
                .add("\tlevels='" + ((as.getLevels() == null) ? "[]" : format.apply(as.getLevels().getElements())) + "'")
                .add("\tsliceConditions='" + ((as.getLevelPredicates() == null) ? "[]" : format.apply(as.getLevelPredicates().getElements())) + "'")
                .add("\tscores='" + ((as.getComparativeMeasures() == null) ? "[]" : format.apply(as.getComparativeMeasures().getElements())) + "'")
                .add("\tscoreFilters='" + ((as.getComparativeMeasurePredicates() == null) ? "[]" : format.apply(as.getComparativeMeasurePredicates().getElements())) + "'")
                .toString();

        logger.debug(logString);
    }
}
