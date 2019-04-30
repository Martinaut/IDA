import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';

@Component({
  selector: '[sortable-index]',
  template: `
    <i class="fas fa-chevron-up" *ngIf="direction === 'asc'"></i>
    <i class="fas fa-chevron-down" *ngIf="direction === 'desc'"></i>
    <ng-content></ng-content>
  `,
  styles: [`
    :host {
      cursor: pointer;
    }
  `]
})
export class SortableColumnComponent {

  @Input('sortable-index') index: number;
  @Input('sort-direction') direction: 'asc' | 'desc' | 'none';
  @Output() columnSort = new EventEmitter<{ column: number, direction: 'asc' | 'desc' | 'none' }>();

  /**
   * Creates a new instance of class SortableColumnComponent.
   */
  constructor() {
    this.direction = 'none';
  }

  /**
   * Switches the direction and emits a sort event on click.
   */
  @HostListener('click')
  sort(): void {
    this.direction = this.direction === 'asc' ? 'desc' :
      this.direction === 'desc' ? 'none' : 'asc';
    this.columnSort.emit({column: this.index, direction: this.direction});
  }
}
