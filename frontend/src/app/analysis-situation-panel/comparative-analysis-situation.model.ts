import { NonComparativeAnalysisSituation } from './non-comparative-analysis-situation.model';

export interface ComparativeAnalysisSituation {
  contextOfInterest: NonComparativeAnalysisSituation;
  contextOfComparison: NonComparativeAnalysisSituation;
  joinConditions: Array<any>;
  scores: Array<string>;
  scoreFilters: Array<string>;
}
