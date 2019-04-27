import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

/**
 * Service providing Speech to Text functionality.
 */
@Injectable({
  providedIn: 'root'
})
export class SpeechToTextService {

  private startedSource = new Subject<Event>();
  private endedSource = new Subject<Event>();
  private resultAvailableSource = new Subject<SpeechRecognitionEvent>();

  private speechRecognition: SpeechRecognition;

  private _autoStart: boolean;

  /**
   * Initializes a new instance of class TextToSpeechService.
   */
  constructor() {
    if (SpeechToTextService.isSupported()) {
      // @ts-ignore
      this.speechRecognition = new window.SpeechRecognition();
      this.speechRecognition.maxAlternatives = 1;
      this.speechRecognition.interimResults = true;
      this.speechRecognition.onstart = evt => this.startedSource.next(evt);
      this.speechRecognition.onend = evt => this.endedSource.next(evt);
      this.speechRecognition.onresult = evt => this.resultAvailableSource.next(evt);
      this.speechRecognition.onerror = evt => console.log('Error during speech recognition: ' + evt);
      this.autoStart = this.getValueFromStorage('ida.voice.autoStart', 'yes') === 'yes';
      this.setLanguage(this.getValueFromStorage('ida.lang', 'en'));
    }
  }

  /**
   * Returns whether speech to text is supported in current browser.
   */
  static isSupported(): boolean {
    return 'SpeechRecognition' in window;
  }

  /**
   * Starts the speech recognition service listening to incoming audio.
   */
  start(): void {
    if (!SpeechToTextService.isSupported()) {
      return;
    }
    this.speechRecognition.start();
  }

  /**
   * Stops the speech recognition service from listening to incoming audio.
   */
  stop(): void {
    if (!SpeechToTextService.isSupported()) {
      return;
    }
    this.speechRecognition.stop();
  }

  // region --- EVENTS ---
  /**
   * Fired when the speech recognition service has begun listening to incoming audio.
   */
  get started(): Observable<Event> {
    return this.startedSource.asObservable();
  }

  /**
   * Fired when the speech recognition service has disconnected.
   */
  get ended(): Observable<Event> {
    return this.endedSource.asObservable();
  }

  /**
   * Fired when the speech recognition service returns a result.
   */
  get resultAvailable(): Observable<SpeechRecognitionEvent> {
    return this.resultAvailableSource.asObservable();
  }

  // endregion

  /**
   * Sets the language of the speech.
   *
   * @param language The language.
   */
  setLanguage(language: string): void {
    if (!SpeechToTextService.isSupported()) {
      return;
    }

    if (language != null && ((language.trim().length === 5 && language.charAt(2) === '-') || language.trim().length === 2)) {
      if (language.length === 2) {
        if (language === 'en') {
          language = 'en-GB';
        }
        if (language === 'de') {
          language = 'de-DE';
        }
      }
      this.speechRecognition.lang = language;
    } else {
      throw new Error('Error setting language. Please verify the language value is a valid language.');
    }
  }

  /**
   * Returns whether listening should start automatically.
   * This has no effect in this class. This value can be used in other parts.
   */
  get autoStart(): boolean {
    return this._autoStart;
  }

  /**
   * Sets whether listening should start automatically.
   *
   * @param value True or False
   */
  set autoStart(value: boolean) {
    this._autoStart = value;
    localStorage.setItem('ida.voice.autoStart', value ? 'yes' : 'no');
  }

  private getValueFromStorage(key: string, fallback) {
    const val = localStorage.getItem(key);
    if (!val) {
      return fallback;
    }
    return val;
  }
}
