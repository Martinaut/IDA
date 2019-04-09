package at.jku.dke.inga.app.ruleset.csp;

import at.jku.dke.inga.app.ruleset.csp.domain.AnalysisSituationSolution;
import at.jku.dke.inga.data.models.Similarity;
import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class CSPTest {
    @Test
    void test() {
        // Build the solver
        SolverFactory<AnalysisSituationSolution> solverFactory = SolverFactory.createFromXmlResource("optaplanner/solverConfig.xml");
        Solver<AnalysisSituationSolution> solver = solverFactory.buildSolver();

        // Set data
        Set<String> cubes = new HashSet<>(Arrays.asList("cube1", "cube2", "cube3"));
        Set<Similarity> measures = new HashSet<>(Arrays.asList(
                new Similarity("a", "cube1", "measureCosts", "measure", 0.5),
                new Similarity("a", "cube1", "measureTotalCosts", "measure", 0.4),
                new Similarity("b", "cube2", "costs", "measure", 0.8)
        ));
        Set<Similarity> levels = new HashSet<>(Arrays.asList(
                new Similarity("x", "cube1", "insurant", "level", 0.5),
                new Similarity("hugo dudo", "cube2", "doctor", "level", 0.4),
                new Similarity("bla", "cube2", "doctorDistrict", "level", 0.5)
        ));

        AnalysisSituationSolution pv = new AnalysisSituationSolution(cubes, measures, levels, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());

        // Solve the problem
        AnalysisSituationSolution result = solver.solve(pv);

        // Display the result
        System.out.println(result.getAnalysisSituation());
    }
}
