export interface NonComparativeAnalysisSituation {
  cube: string;
  baseMeasureConditions: Array<string>;
  measures: Array<string>;
  filterConditions: Array<string>;
  dimensionQualifications: Array<{ dimension: string; diceLevel: string; diceNode: string; granularityLevel: string; sliceConditions: Array<string> }>;
}
