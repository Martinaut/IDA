import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

// Based on https://github.com/tom-s/speak-tts/blob/master/src/speak-tts.js
/**
 * Service providing Text to Speech functionality.
 */
@Injectable({
  providedIn: 'root'
})
export class TextToSpeechService {

  private supportedVoicesChangedSource = new Subject<SpeechSynthesisVoice[]>();
  private synth: SpeechSynthesis;
  private voices: SpeechSynthesisVoice[];

  private _voiceUri: string;
  private _voice: SpeechSynthesisVoice;
  private _volume: number;
  private _pitch: number;
  private _rate: number;
  private _language: string;

  /**
   * Initializes a new instance of class TextToSpeechService.
   */
  constructor() {
    if (TextToSpeechService.isSupported()) {
      this.synth = TextToSpeechService.isSupported() ? window.speechSynthesis : null;
      this.language = this.getValueFromStorage('ida.lang', 'en');
      this.volume = this.getValueFromStorage('ida.voice.volume', 1.0);
      this.pitch = this.getValueFromStorage('ida.voice.pitch', 1.0);
      this.rate = this.getValueFromStorage('ida.voice.rate', 1.0);
      this.voiceUri = this.getValueFromStorage('ida.voice.' + this.language, null);
      this.loadVoices().then(value => {
        this.voices = value;
        this.supportedVoicesChangedSource.next(this.getVoices());
      });
    }
  }

  /**
   * Returns whether text to speech is supported in current browser.
   */
  static isSupported(): boolean {
    return 'speechSynthesis' in window;
  }

  /**
   * Adds an utterance to the utterance queue and stops possible active utterances.
   *
   * @param text The text to utter.
   */
  speak(text: string): void {
    if (text == null || text.trim().length === 0) {
      return;
    }
    if (!TextToSpeechService.isSupported()) {
      return;
    }

    // Stop current utterance
    this.synth.cancel();

    // Build utterance
    const utter = new SpeechSynthesisUtterance(text);
    if (this._voice) {
      utter.voice = this._voice;
    }
    utter.lang = this._language;
    utter.volume = this._volume;
    utter.rate = this._rate;
    utter.pitch = this._pitch;
    utter.onerror = evt => console.log('An error has occurred with the speech synthesis: ' + evt.error);

    // Speak
    this.synth.speak(utter);
  }

  /**
   * Cancel the voice output.
   */
  stop(): void {
    if (!TextToSpeechService.isSupported()) {
      return;
    }
    this.synth.cancel();
  }

  /**
   * Returns all available voices for the configured language.
   */
  getVoices(): SpeechSynthesisVoice[] {
    if (!this.voices || !this._language || !TextToSpeechService.isSupported()) {
      return [];
    }

    const part1 = this._language.split('-')[0];
    return this.voices.filter(voice => voice.lang.startsWith(part1));
  }

  /**
   * This events gets emitted when the list of available voices changed.
   */
  get supportedVoicesChanged(): Observable<SpeechSynthesisVoice[]> {
    return this.supportedVoicesChangedSource.asObservable();
  }

  // region --- GETTER ---
  /**
   * Returns the voice.
   */
  get voice(): SpeechSynthesisVoice {
    return this._voice;
  }

  /**
   * Returns the volume.
   */
  get volume(): number {
    return this._volume;
  }

  /**
   * Returns the pitch.
   */
  get pitch(): number {
    return this._pitch;
  }

  /**
   * Returns the rate.
   */
  get rate(): number {
    return this._rate;
  }

  /**
   * Returns the language.
   */
  get language(): string {
    return this._language;
  }

  /**
   * Returns the voice uri.
   */
  get voiceUri(): string {
    return this._voiceUri;
  }

  // endregion

  // region --- SETTER ---
  /**
   * Sets the volume of speech synthesis.
   *
   * @param value The volume.
   */
  set volume(value: number) {
    if (value >= 0 && value <= 1) {
      this._volume = value;
      localStorage.setItem('ida.voice.volume', value.toString());
    } else {
      throw new Error('Error setting volume. Please verify the volume value is a number between 0 and 1.');
    }
  }

  /**
   * Sets the voice pitch of speech synthesis.
   *
   * @param value The voice pitch.
   */
  set pitch(value: number) {
    if (value >= 0 && value <= 2) {
      this._pitch = value;
      localStorage.setItem('ida.voice.pitch', value.toString());
    } else {
      throw new Error('Error setting pitch. Please verify the pitch value is a number between 0 and 2.');
    }
  }

  /**
   * Sets the voice rate of speech synthesis.
   *
   * @param value The voice rate.
   */
  set rate(value: number) {
    if (value >= 0 && value <= 10) {
      this._rate = value;
      localStorage.setItem('ida.voice.rate', value.toString());
    } else {
      throw new Error('Error setting rate. Please verify the rate value is a number between 0 and 10.');
    }
  }

  /**
   * Sets the language of the speech.
   *
   * @param value The language.
   */
  set language(value: string) {
    if (value != null && ((value.trim().length === 5 && value.charAt(2) === '-') || value.trim().length === 2)) {
      if (value.length === 2) {
        if (value === 'en') {
          value = 'en-GB';
        }
        if (value === 'de') {
          value = 'de-DE';
        }
      }
      this._language = value;
      this.voiceUri = this.getValueFromStorage('ida.voice.' + this.language, null);
      this.supportedVoicesChangedSource.next(this.getVoices());
    } else {
      throw new Error('Error setting language. Please verify the language value is a valid language.');
    }
  }

  /**
   * Sets the voice of the speech synthesizer.
   *
   * @param value The voice Uri.
   */
  set voiceUri(value: string) {
    if (value == null || value.trim().length === 0) {
      this._voice = null;
      this._voiceUri = '';
      localStorage.removeItem('ida.voice.' + this.language);
      return;
    }

    const timeout = !this.voices || this.voices.length === 0 ? 550 : 0;
    setTimeout(() => {
      const langPart = this._language.split('-')[0];
      for (const v of this.voices) {
        if (v.voiceURI === value && v.lang.startsWith(langPart)) {
          this._voice = v;
          this._voiceUri = value;
          localStorage.setItem('ida.voice.' + this.language, v.voiceURI);
          return;
        }
      }
      this._voice = null;
      this._voiceUri = '';
      localStorage.removeItem('ida.voice.' + this.language);
    }, timeout);
  }
  // endregion

  // region --- Helper-Methods ---
  private getValueFromStorage(key: string, fallback) {
    const val = localStorage.getItem(key);
    if (!val) {
      return fallback;
    }
    return val;
  }

  private fetchVoices(): Promise<SpeechSynthesisVoice[]> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const voices = this.synth.getVoices();
        if (voices.length > 0) {
          return resolve(voices);
        } else {
          return reject('Could not fetch voices');
        }
      }, 200);
    });
  }

  private loadVoices(attempts = 5): Promise<SpeechSynthesisVoice[]> {
    return this.fetchVoices().catch(err => {
      if (attempts === 0) {
        throw new Error(err);
      }
      return this.loadVoices(attempts - 1);
    });
  }

  // endregion
}
