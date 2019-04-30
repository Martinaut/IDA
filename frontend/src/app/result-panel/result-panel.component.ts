import { Component, OnDestroy, OnInit, QueryList, ViewChildren } from '@angular/core';
import * as Papa from 'papaparse';
import { ConnectionService } from '../services';
import { Subscription } from 'rxjs';
import { SortableColumnComponent } from './sortable-column.component';

/**
 * Panel used to display the result-panel of the executed query.
 */
@Component({
  selector: 'app-result-panel',
  templateUrl: './result-panel.component.html'
})
export class ResultPanelComponent implements OnInit, OnDestroy {

  private sub: Subscription;
  parsed: Array<any>;
  sorted: Array<any>;

  @ViewChildren(SortableColumnComponent) columns: QueryList<SortableColumnComponent>;

  /**
   * Initializes a new instance of class ResultPanelComponent.
   */
  constructor(private connectionService: ConnectionService) {
    this.parsed = null;
    this.sorted = null;
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.sub = this.connectionService.resultMessageReceived.subscribe(value => {
      if (value == null) {
        this.parsed = null;
        this.sorted = null;
      } else {
        const pr = Papa.parse(value, {});
        this.parsed = pr.data;
        this.sorted = pr.data;
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

  /**
   * Sorts the table.
   * @param evt The sort event.
   */
  onSort(evt: { column: number, direction: 'asc' | 'desc' | 'none' }): void {
    // reset other columns
    this.columns.forEach(col => {
      if (col.index !== evt.column) {
        col.direction = 'none';
      }
    });

    // Sort
    if (evt.direction === 'none') {
      this.sorted = this.parsed;
    } else {
      const tmp = this.parsed.slice(1);
      tmp.sort((a, b) => {
        const compare = a[evt.column] < b[evt.column] ? -1 :
          a[evt.column] > b[evt.column] ? 1 : 0;
        return evt.direction === 'asc' ? compare : compare * -1;
      });
      this.sorted = this.parsed.slice(0, 1).concat(tmp);
    }
  }
}
