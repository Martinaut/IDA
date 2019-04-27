import { Displayable } from './displayable.model';

export interface NonComparativeAnalysisSituation {
  cube: Displayable;
  baseMeasureConditions: Array<Displayable>;
  measures: Array<Displayable>;
  filterConditions: Array<Displayable>;
  dimensionQualifications: Array<{
    dimension: Displayable;
    diceLevel: Displayable;
    diceNode: Displayable;
    granularityLevel: Displayable;
    sliceConditions: Array<Displayable>
  }>;
}
