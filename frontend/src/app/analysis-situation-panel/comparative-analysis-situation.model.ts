import { NonComparativeAnalysisSituation } from './non-comparative-analysis-situation.model';
import { Displayable } from './displayable.model';

export interface ComparativeAnalysisSituation {
  contextOfInterest: NonComparativeAnalysisSituation;
  contextOfComparison: NonComparativeAnalysisSituation;
  joinConditions: Array<Displayable>;
  scores: Array<Displayable>;
  scoreFilters: Array<Displayable>;
}
