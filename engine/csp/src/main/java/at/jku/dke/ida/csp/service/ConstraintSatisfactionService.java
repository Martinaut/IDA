package at.jku.dke.ida.csp.service;

import at.jku.dke.ida.csp.ConstraintSatisfactionSettings;
import at.jku.dke.ida.csp.domain.AnalysisSituationSolution;
import at.jku.dke.ida.csp.models.FillDeterminedValuesServiceModel;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

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
        AnalysisSituationSolution problem = buildProblem(sessionModel.getAnalysisSituation() instanceof ComparativeAnalysisSituation, similarities);

        // Solve
        logger.info("Solving CSP");
        AnalysisSituationSolution solution = solver.solve(problem);
        logger.info("CSP solved");
        logger.info(solver.explainBestScore());

        // Set values
        new FillDeterminedValuesService().executeRules(new FillDeterminedValuesServiceModel("CSP", sessionModel, solution.getAnalysisSituation()));
    }

    private AnalysisSituationSolution buildProblem(boolean isComparative, Set<CubeSimilarity> similarities) {
        logger.debug("Building problem");
        ConstraintSatisfactionSettings settings = BeanUtil.getBean(ConstraintSatisfactionSettings.class);
        return new AnalysisSituationSolution(
                isComparative,
                similarities.stream().map(CubeSimilarity::getCube).collect(Collectors.toSet()),
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

}
