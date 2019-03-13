import {Component, OnDestroy, OnInit} from '@angular/core';
import {ConnectionService, SpeechToTextService, TextToSpeechService} from '../services';
import {Subscription} from 'rxjs';

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
              private stt: SpeechToTextService) {
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.selectedLanguage = this.getLangFromStorage();
    this.serverUrl = this.getUrlFromStorage();
    this.isConnected = false;
    this.isCollapsed = false;
    this.connectionService.connectionStateChanged.subscribe(value => {
      this.isConnected = value;
      this.isCollapsed = value;
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
  }

  setLanguage(lang): void {
    this.selectedLanguage = lang;
    this.tts.language = this.selectedLanguage;
    this.stt.setLanguage(this.selectedLanguage);
  }

  // region --- SETTINGS ---
  private storeSettings(): void {
    localStorage.setItem('inga.lang', this.selectedLanguage);
    localStorage.setItem('inga.url', this.serverUrl);
  }

  private getLangFromStorage(): string {
    const lang = localStorage.getItem('inga.lang');
    if (lang) {
      return lang;
    }
    return 'en';
  }

  private getUrlFromStorage(): string {
    const lang = localStorage.getItem('inga.url');
    if (lang) {
      return lang;
    }
    return 'ws://localhost:8080/ws';
  }

  // endregion
}
