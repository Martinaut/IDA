import { Component, Input } from '@angular/core';
import { NonComparativeAnalysisSituation } from '../non-comparative-analysis-situation.model';

/**
 * Panel used to display a non comparative analysis situation.
 */
@Component({
  selector: 'app-non-comparative',
  templateUrl: './non-comparative.component.html'
})
export class NonComparativeComponent {

  @Input() analysisSituation: NonComparativeAnalysisSituation;

  /**
   * Initializes a new instance of class NonComparativeComponent.
   */
  constructor() {
  }

}
