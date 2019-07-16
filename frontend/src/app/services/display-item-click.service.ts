import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DisplayItemClickService {

  private inputChangedSource = new Subject<string>();
  private sendRequestedSource = new Subject<void>();

  private left: string;
  private right: string;

  /**
   * Emits events when the user input should be changed to the given value.
   */
  public inputChanged = this.inputChangedSource.asObservable();

  /**
   * Emits events when the user input should be sent to the server.
   */
  public sendRequested = this.sendRequestedSource.asObservable();

  /**
   * Initializes a new instance of class DisplayItemClickService.
   */
  constructor() {
    this.left = '';
    this.right = '';
  }

  /**
   * Call this method if an item in the list-display has been clicked.
   *
   * @param position The position of the clicked item.
   */
  listItemClicked(position: number): void {
    this.inputChangedSource.next(position + '');
    this.sendRequestedSource.next();
  }

  /**
   * Call this method if an item on the right side of the two-list-display has been clicked.
   *
   * @param position The position of the clicked item.
   */
  rightListItemClicked(position: number): void {
    this.right = position + '';
    this.inputChangedSource.next(this.left + ',' + this.right);
    if (this.left !== '' && this.right !== '') {
      this.left = '';
      this.right = '';
      this.sendRequestedSource.next();
    }
  }

  /**
   * Call this method if an item on the left side of the two-list-display has been clicked.
   *
   * @param position The position of the clicked item.
   */
  leftListItemClicked(position: number): void {
    this.left = position + '';
    this.inputChangedSource.next(this.left + ',' + this.right);
    if (this.left !== '' && this.right !== '') {
      this.left = '';
      this.right = '';
      this.sendRequestedSource.next();
    }
  }
}
