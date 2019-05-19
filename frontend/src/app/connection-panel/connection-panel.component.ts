import { Component, OnDestroy, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { ConnectionService, SpeechToTextService, TextToSpeechService } from '../services';
import { environment } from '../../environments/environment';

/**
 * Panel used to connect to the websocket.
 */
@Component({
  selector: 'app-connection-panel',
  templateUrl: './connection-panel.component.html'
})
export class ConnectionPanelComponent implements OnInit, OnDestroy {

  private connSub: Subscription;

  selectedLanguage: string;
  serverUrl: string;
  isConnected: boolean;
  isCollapsed: boolean;

  /**
   * Initializes a new instance of class ConnectionPanelComponent.
   */
  constructor(private connectionService: ConnectionService,
              private tts: TextToSpeechService,
              private stt: SpeechToTextService,
              private translateService: TranslateService) {
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.setLanguage(this.getLangFromStorage());
    this.serverUrl = this.getUrlFromStorage();
    this.isConnected = false;
    this.isCollapsed = false;
    this.connectionService.initializedStateChanged.subscribe(value => {
      this.isConnected = value;
      this.isCollapsed = value;
    });
    this.connectionService.connectionStateChanged.subscribe(value => {
      if (!value) {
        this.tts.stop();
      }
    });
  }

  /**
   * A lifecycle hook that is called when the directive is destroyed.
   */
  ngOnDestroy(): void {
    if (this.connSub) {
      this.connSub.unsubscribe();
    }
  }

  /**
   * Connects to the websocket.
   */
  connect(): void {
    this.storeSettings();
    this.connectionService.connect(this.serverUrl, this.selectedLanguage);
  }

  /**
   * Disconnects the websocket.
   */
  disconnect(): void {
    this.connectionService.disconnect();
    this.tts.stop();
  }

  /**
   * Sets the selected language.
   *
   * @param lang The selected language.
   */
  setLanguage(lang): void {
    this.selectedLanguage = lang;
    this.tts.language = this.selectedLanguage;
    this.stt.setLanguage(this.selectedLanguage);
    this.translateService.use(lang);
  }

  /**
   * Shows/hides the component.
   *
   * @param event The click-event.
   */
  toggle(event: MouseEvent): void {
    if (event) {
      event.preventDefault();
    }
    this.isCollapsed = !this.isCollapsed;
  }

  // region --- SETTINGS ---
  private storeSettings(): void {
    localStorage.setItem('ida.lang', this.selectedLanguage);
    localStorage.setItem('ida.url', this.serverUrl);
  }

  private getLangFromStorage(): string {
    const lang = localStorage.getItem('ida.lang');
    if (lang) {
      return lang;
    }
    return 'en';
  }

  private getUrlFromStorage(): string {
    const lang = localStorage.getItem('ida.url');
    if (lang) {
      return lang;
    }
    return environment.defaultUrl;
  }

  // endregion
}
