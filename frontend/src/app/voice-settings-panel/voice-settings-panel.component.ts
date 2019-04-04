import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { SpeechToTextService, TextToSpeechService } from '../services';

@Component({
  selector: 'app-voice-settings-panel',
  templateUrl: './voice-settings-panel.component.html'
})
export class VoiceSettingsPanelComponent implements OnInit, OnDestroy {

  isCollapsed: boolean;
  ttsSupported: boolean;
  autoStartListening: boolean;

  voices: SpeechSynthesisVoice[];
  sub: Subscription;

  /**
   * Initializes a new instance of class VoiceSettingsPanelComponent.
   */
  constructor(public tts: TextToSpeechService,
              private stt: SpeechToTextService) {
    this.isCollapsed = true;
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.ttsSupported = TextToSpeechService.isSupported();
    this.sub = this.tts.supportedVoicesChanged.subscribe(value => this.voices = value);
    setTimeout(() => this.voices = this.tts.getVoices(), 600);
  }

  /**
   * A lifecycle hook that is called when the directive is destroyed.
   */
  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
}
