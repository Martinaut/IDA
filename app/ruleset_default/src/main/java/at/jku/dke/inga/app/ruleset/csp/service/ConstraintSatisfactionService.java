package at.jku.dke.inga.app.ruleset.csp.service;

import at.jku.dke.inga.app.ruleset.csp.domain.AnalysisSituation;
import at.jku.dke.inga.app.ruleset.csp.domain.AnalysisSituationSolution;
import at.jku.dke.inga.app.ruleset.helpers.ValueSetter;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.data.repositories.LevelPredicateRepository;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.spring.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

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
    public void fillAnalysisSituation(String language, at.jku.dke.inga.shared.models.AnalysisSituation as, Set<Similarity> similarities) {
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

    private AnalysisSituationSolution buildProblem(Set<Similarity> similarities) {
        logger.debug("Building problem");
        return new AnalysisSituationSolution(
                similarities.stream().map(Similarity::getCube).collect(Collectors.toSet()),
                similarities.stream().filter(x -> x.getType().equals("http://dke.jku.at/inga/cubes#AggregateMeasure")).collect(Collectors.toSet()),
                similarities.stream().filter(x -> x.getType().equals("http://dke.jku.at/inga/cubes#Level")).collect(Collectors.toSet()),
                similarities.stream().filter(x -> x.getType().equals("http://dke.jku.at/inga/cubes#LevelPredicate") || x.getType().equals("http://dke.jku.at/inga/cubes#ConjunctiveLevelPredicate")).collect(Collectors.toSet()),
                similarities.stream().filter(x -> x.getType().equals("http://dke.jku.at/inga/cubes#BaseMeasurePredicate")).collect(Collectors.toSet()),
                similarities.stream().filter(x -> x.getType().equals("http://dke.jku.at/inga/cubes#AggregateMeasurePredicate")).collect(Collectors.toSet())
        );
    }

    private void setValues(String language, NonComparativeAnalysisSituation as, AnalysisSituation solution) {
        if (solution == null) return;

        logger.debug("Setting values");
        if (solution.getCube() != null) {
            ValueSetter.setCube(language, as, solution.getCube());
        } else return;

        if (solution.getMeasures() != null)
            as.setMeasures(solution.getMeasures().getElements().stream().map(Similarity::getElement).collect(Collectors.toSet()));
        if (solution.getBaseMeasureConditions() != null)
            as.setBaseMeasureConditions(solution.getMeasures().getElements().stream().map(Similarity::getElement).collect(Collectors.toSet()));
        if (solution.getFilterConditions() != null)
            as.setFilterConditions(solution.getMeasures().getElements().stream().map(Similarity::getElement).collect(Collectors.toSet()));
        if (solution.getSliceConditions() != null && !solution.getSliceConditions().getElements().isEmpty()) {
            try {
                Set<Pair<String, String>> lpreds = BeanUtil.getBean(LevelPredicateRepository.class).getByIri(solution.getSliceConditions().getElements().stream().map(Similarity::getElement).collect(Collectors.toSet()));
                lpreds.forEach(lp -> as.getDimensionQualification(lp.getLeft()).addSliceCondition(lp.getRight()));
            } catch (QueryException ex) {
                logger.error("Could not query level predicates.", ex);
            }
        }
        if (solution.getGranularityLevels() != null && !solution.getGranularityLevels().getElements().isEmpty()) {
            try {
                Set<Pair<String, String>> lpreds = BeanUtil.getBean(GranularityLevelRepository.class).getByIri(solution.getGranularityLevels().getElements().stream().map(Similarity::getElement).collect(Collectors.toSet()));
                lpreds.forEach(lp -> as.getDimensionQualification(lp.getLeft()).setGranularityLevel(lp.getRight()));
            } catch (QueryException ex) {
                logger.error("Could not query granularity levels.", ex);
            }
        }
    }
}
