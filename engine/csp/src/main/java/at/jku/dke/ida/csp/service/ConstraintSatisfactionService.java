package at.jku.dke.ida.csp.service;

import at.jku.dke.ida.csp.ConstraintSatisfactionSettings;
import at.jku.dke.ida.csp.domain.AnalysisSituation;
import at.jku.dke.ida.csp.domain.AnalysisSituationSolution;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.models.DimensionSimilarity;
import at.jku.dke.ida.data.repositories.LevelRepository;
import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.lang3.StringUtils;
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
     * @param language     The required language.
     * @param as           The analysis situation.
     * @param similarities The set with found similarities.
     * @throws IllegalArgumentException If {@code as} is {@code null} or if it is not an instance of {@link NonComparativeAnalysisSituation}.
     */
    public void fillAnalysisSituation(String language, EngineAnalysisSituation as, Set<CubeSimilarity> similarities) {
        if (as == null) throw new IllegalArgumentException("as must not be null");
        if (StringUtils.isBlank(language)) throw new IllegalArgumentException("language must not be empty.");
        if (!(as instanceof NonComparativeAnalysisSituation))
            throw new IllegalArgumentException("Only NonComparativeAnalysisSituations are supported.");
        if (similarities == null || similarities.isEmpty()) return;
        if (this.solver == null) initSolver();

        // Build model
        AnalysisSituationSolution problem = buildProblem(similarities);

        // Solve
        logger.info("Solving CSP");
        AnalysisSituationSolution solution = solver.solve(problem);
        logger.info("CSP solved");
        logger.info(solver.explainBestScore());

        // Set values
        setValues(language, (NonComparativeAnalysisSituation) as, solution.getAnalysisSituation());
    }

    private AnalysisSituationSolution buildProblem(Set<CubeSimilarity> similarities) {
        logger.debug("Building problem");
        ConstraintSatisfactionSettings settings = BeanUtil.getBean(ConstraintSatisfactionSettings.class);
        return new AnalysisSituationSolution(
                similarities.stream().map(CubeSimilarity::getCube).collect(Collectors.toSet()),
                similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_AGGREGATE_MEASURE)).collect(Collectors.toSet()),
                settings.isUseLevels() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_LEVEL)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseLevelPredicates() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_LEVEL_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseBaseMeasurePredicates() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_BASE_MEASURE_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet(),
                settings.isUseAggregateMeasurePredicates() ? similarities.stream().filter(x -> x.getType().equals(IRIConstants.TYPE_AGGREGATE_MEASURE_PREDICATE)).collect(Collectors.toSet()) : Collections.emptySet()
        );
    }

    private void setValues(String language, NonComparativeAnalysisSituation as, AnalysisSituation solution) {
        if (solution == null) return;
        logger.debug("Setting values");
        ConstraintSatisfactionSettings settings = BeanUtil.getBean(ConstraintSatisfactionSettings.class);

        // Cube
        if (solution.getCube() != null) {
            as.setCube(solution.getCube());

            try {
                var baseLevels = BeanUtil.getBean(LevelRepository.class).getTopLevelLabelsByLangAndCube(language, solution.getCube());
                for (DimensionLabel lvl : baseLevels) {
                    var dq = new DimensionQualification(lvl.getDimensionUri());
                    dq.setGranularityLevel(lvl.getUri());
                    as.addDimensionQualification(dq);
                }
            } catch (QueryException ex) {
                logger.fatal("Cannot set dimension qualifications for cube " + solution.getCube() + " as there occurred an error while querying the levels.", ex);
            }
        } else return;

        // Measures
        if (solution.getMeasures() != null && !solution.getMeasures().getElements().isEmpty())
            as.setMeasures(solution.getMeasures().getElements().stream().map(CubeSimilarity::getElement).collect(Collectors.toSet()));

        // Base Measure Conditions
        if (settings.isUseBaseMeasurePredicates() && solution.getBaseMeasureConditions() != null && !solution.getBaseMeasureConditions().getElements().isEmpty())
            as.setBaseMeasureConditions(solution.getBaseMeasureConditions().getElements().stream().map(CubeSimilarity::getElement).collect(Collectors.toSet()));

        // Filter Conditions
        if (settings.isUseAggregateMeasurePredicates() && solution.getFilterConditions() != null && !solution.getFilterConditions().getElements().isEmpty())
            as.setFilterConditions(solution.getFilterConditions().getElements().stream().map(CubeSimilarity::getElement).collect(Collectors.toSet()));

        // Slice Conditions
        if (settings.isUseLevelPredicates() && solution.getSliceConditions() != null && !solution.getSliceConditions().getElements().isEmpty())
            solution.getSliceConditions().getElements().stream()
                    .filter(x -> x instanceof DimensionSimilarity)
                    .map(x -> (DimensionSimilarity) x)
                    .forEach(x -> as.getDimensionQualification(x.getDimension()).addSliceCondition(x.getElement()));

        // Granularity Levels
        if (settings.isUseLevels() && solution.getGranularityLevels() != null && !solution.getGranularityLevels().getElements().isEmpty())
            solution.getGranularityLevels().getElements().stream()
                    .filter(x -> x instanceof DimensionSimilarity)
                    .map(x -> (DimensionSimilarity) x)
                    .forEach(x -> as.getDimensionQualification(x.getDimension()).setGranularityLevel(x.getElement()));
    }
}
