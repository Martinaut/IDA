import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ConnectionService } from '../../services';

@Component({
  selector: 'app-display-panel',
  templateUrl: './display-panel.component.html'
})
export class DisplayPanelComponent implements OnInit, OnDestroy {

  private resSub: Subscription;
  type: string;
  display: any;

  /**
   * Initializes a new instance of class InputPanelComponent.
   */
  constructor(private connectionService: ConnectionService) {
    this.type = null;
    this.display = null;
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.resSub = this.connectionService.displayMessageReceived.subscribe(value => {
      if (value == null) {
        this.type = null;
        this.display = null;
      } else {
        const obj = JSON.parse(value);
        this.type = obj.type;
        this.display = obj.display;
      }
    });
  }

  /**
   * A lifecycle hook that is called when the directive is destroyed.
   */
  ngOnDestroy(): void {
    if (this.resSub) {
      this.resSub.unsubscribe();
    }
  }
}
