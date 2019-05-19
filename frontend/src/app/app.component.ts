import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

/**
 * The root component of the application.
 */
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  supportsWebsockets = (('WebSocket' in window || 'MozWebSocket' in window) && typeof TextEncoder !== 'undefined');

  /**
   * Initializes a new instance of class AppComponent.
   */
  constructor(private translate: TranslateService) {
    translate.setDefaultLang('en');
  }

}
