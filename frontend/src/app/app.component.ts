import {Component} from '@angular/core';

/**
 * The root component of the application.
 */
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  supportsWebsockets = (('WebSocket' in window || 'MozWebSocket' in window) && typeof TextEncoder !== 'undefined');
}
