import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import * as Papa from 'papaparse';
import { ConnectionService } from '../services';

declare var $: any;

/**
 * Panel used to display the result-panel of the executed query.
 */
@Component({
  selector: 'app-result-panel',
  templateUrl: './result-panel.component.html'
})
export class ResultPanelComponent implements OnInit, OnDestroy {

  private sub: Subscription;
  private resultTableElement: ElementRef;
  parsed: Array<Array<any>>;

  /**
   * Initializes a new instance of class ResultPanelComponent.
   */
  constructor(private connectionService: ConnectionService) {
    this.parsed = null;
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.sub = this.connectionService.resultMessageReceived.subscribe(value => {
      if (value == null) {
        this.parsed = null;
      } else {
        const pr = Papa.parse(value, {skipEmptyLines: true});
        this.parsed = pr.data;

        setTimeout(() => {
          // Create
          this.createPivotTable();

          // Scroll
          if (this.resultTableElement && this.resultTableElement.nativeElement) {
            this.resultTableElement.nativeElement.scrollIntoView({left: 0, block: 'start', behavior: 'smooth'});
          }
        }, 500);
      }
    });
  }

  /**
   * A lifecycle hook that is called when the directive is destroyed.
   */
  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  @ViewChild('pivotTable') set content(content: ElementRef) {
    this.resultTableElement = content;
  }

  private createPivotTable(): void {
    if (!this.parsed) {
      return;
    }
    if (!this.resultTableElement) {
      return;
    }

    // Prepare rows
    let rows = this.parsed[0];

    // Find numeric columns
    let tmp = [];
    for (let i = 0; i < rows.length; i++) {
      if (isNaN(this.parsed[1][i])) {
        tmp = [];
      } else {
        tmp.push(rows[i]);
      }
    }
    rows = rows.filter(el => !tmp.includes(el));

    // Find all columns
    const allCol = [];
    for (let i = 0; i < rows.length; i++) {
      if (this.parsed[1][i] === '(All)') {
        allCol.push(rows[i]);
      }
    }
    rows = rows.filter((el) => !allCol.includes(el));

    // Create aggreagators
    const tpl = $.pivotUtilities.aggregatorTemplates;
    const aggregators = {};
    for (const m of tmp) {
      aggregators['Sum of ' + m] = () => tpl.sum()([m]);
    }

    // Create
    const div = $(this.resultTableElement.nativeElement);
    while (div.firstChild) {
      div.removeChild(div.firstChild);
    }
    div.pivotUI(this.parsed, {
      rows,
      aggregators
    });
  }
}
