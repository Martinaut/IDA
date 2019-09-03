package at.jku.dke.ida.app.ruleset.csp;

class CSPTest {
//    @Test
//    void test() {
//        // Build the solver
//        SolverFactory<AnalysisSituationSolution> solverFactory = SolverFactory.createFromXmlResource("optaplanner/solverConfig.xml");
//        Solver<AnalysisSituationSolution> solver = solverFactory.buildSolver();
//
//        // Set data
//        Set<String> cubes = new HashSet<>(Arrays.asList("cube1", "cube2", "cube3"));
//        Set<CubeSimilarity> measures = new HashSet<>(Arrays.asList(
//                new CubeSimilarity("a", "cube1", "measureCosts", "measure", 0.5),
//                new CubeSimilarity("a", "cube1", "measureTotalCosts", "measure", 0.4),
//                new CubeSimilarity("b", "cube2", "costs", "measure", 0.8)
//        ));
//        Set<CubeSimilarity> levels = new HashSet<>(Arrays.asList(
//                new CubeSimilarity("x", "cube1", "insurant", "level", 0.5),
//                new CubeSimilarity("hugo dudo", "cube2", "doctor", "level", 0.4),
//                new CubeSimilarity("bla", "cube2", "doctorDistrict", "level", 0.5)
//        ));
//
//        AnalysisSituationSolution pv = new AnalysisSituationSolution(cubes, measures, levels, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
//
//        // Solve the problem
//        AnalysisSituationSolution result = solver.solve(pv);
//
//        // Display the result
//        System.out.println(result.getAnalysisSituation());
//    }
//
//    @Test
//    void test2() {
//        // Build the solver
//        SolverFactory<AnalysisSituationSolution> solverFactory = SolverFactory.createFromXmlResource("optaplanner/solverConfig.xml");
//        Solver<AnalysisSituationSolution> solver = solverFactory.buildSolver();
//
//        // Set data
//        Set<String> cubes = new HashSet<>(Collections.singletonList("http://www.example.org/drugs#DrugPrescriptionCube"));
//        Set<CubeSimilarity> measures = new HashSet<>(Collections.singletonList(
//                new CubeSimilarity("total costs", "http://www.example.org/drugs#DrugPrescriptionCube", "http://www.example.org/drugs#SumCostsMeasure", "http://dke.jku.at/inga/cubes#AggregateMeasure", 0.5172124947265087)
//        ));
//        Set<CubeSimilarity> levels = new HashSet<>(Arrays.asList(
//                new DimensionSimilarity("doctor district", "http://www.example.org/drugs#DrugPrescriptionCube", "http://www.example.org/drugs#DoctorDimensionDocDistrictLevel", "http://dke.jku.at/inga/cubes#Level", 0.4983485554083388,"http://www.example.org/drugs#DoctorDimension"),
//                new DimensionSimilarity("doctor district", "http://www.example.org/drugs#DrugPrescriptionCube", "http://www.example.org/drugs#DoctorDimensionDocProvinceLevel", "http://dke.jku.at/inga/cubes#Level", 0.4092717010223252,"http://www.example.org/drugs#DoctorDimension"),
//                new DimensionSimilarity("insurant province", "http://www.example.org/drugs#DrugPrescriptionCube", "http://www.example.org/drugs#InsurantDimensionInsProvinceLevel", "http://dke.jku.at/inga/cubes#Level", 0.5293516212246044,"http://www.example.org/drugs#InsurantDimension"),
//                new DimensionSimilarity("insurant province", "http://www.example.org/drugs#DrugPrescriptionCube", "http://www.example.org/drugs#InsurantDimensionInsDistrictLevel", "http://dke.jku.at/inga/cubes#Level", 0.4547945307987954,"http://www.example.org/drugs#InsurantDimension")
//        ));
//
//        AnalysisSituationSolution pv = new AnalysisSituationSolution(cubes, measures, levels, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
//
//        // Solve the problem
//        AnalysisSituationSolution result = solver.solve(pv);
//
//        // Display the result
//        System.out.println(result.getScore());
//        System.out.println(result.getAnalysisSituation());
//    }
}
