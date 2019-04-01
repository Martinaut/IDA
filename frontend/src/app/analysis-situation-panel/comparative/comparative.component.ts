import { Component, Input } from '@angular/core';
import { ComparativeAnalysisSituation } from '../comparative-analysis-situation.model';

/**
 * Panel used to display a comparative analysis situation.
 */
@Component({
  selector: 'app-comparative',
  templateUrl: './comparative.component.html'
})
export class ComparativeComponent {

  @Input() analysisSituation: ComparativeAnalysisSituation;

  /**
   * Initializes a new instance of class ComparativeComponent.
   */
  constructor() {
  }

}
