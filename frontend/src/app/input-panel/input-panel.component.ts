import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ConnectionService, SpeechToTextService} from '../services';

/**
 * Displays an input field for the user query.
 */
@Component({
  selector: 'app-input-panel',
  templateUrl: './input-panel.component.html'
})
export class InputPanelComponent implements OnInit, OnDestroy {

  private connSub: Subscription;
  private resSub: Subscription;

  private speechStartSub: Subscription;
  private speechEndSub: Subscription;
  private speechResultSub: Subscription;

  connected: boolean;
  waitingForResult: boolean;
  inputError: boolean;

  sttSupported: boolean;
  recording: boolean;

  userInput: string;

  /**
   * Initializes a new instance of class InputPanelComponent.
   */
  constructor(private connectionService: ConnectionService,
              private stt: SpeechToTextService,
              private changeDetector: ChangeDetectorRef) {
    this.connected = false;
    this.waitingForResult = false;
    this.inputError = false;
    this.sttSupported = false;
    this.recording = false;
  }

  /**
   * Sends a message with the user input to the server.
   */
  sendMessage(): void {
    if (this.userInput == null || this.userInput.trim().length === 0) {
      this.inputError = true;
      return;
    }
    this.inputError = false;
    this.waitingForResult = true;
    this.connectionService.sendMessage(this.userInput);
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.sttSupported = SpeechToTextService.isSupported();
    this.connSub = this.connectionService.connectionStateChanged.subscribe(value => this.connected = value);
    this.resSub = this.connectionService.resultMessageReceived.subscribe(value => this.waitingForResult = false); // TODO: focus input
    this.speechStartSub = this.stt.started.subscribe(value => {
      this.recording = true;
      this.changeDetector.detectChanges();
    });
    this.speechEndSub = this.stt.ended.subscribe(value => {
      this.recording = false;
      this.changeDetector.detectChanges();
    });
    this.speechResultSub = this.stt.resultAvailable.subscribe(value => {
      this.userInput = value.results[value.results.length - 1][0].transcript;
      this.changeDetector.detectChanges();
    });
  }

  /**
   * A lifecycle hook that is called when the directive is destroyed.
   */
  ngOnDestroy(): void {
    if (this.connSub) {
      this.connSub.unsubscribe();
    }
    if (this.resSub) {
      this.resSub.unsubscribe();
    }
    if (this.speechStartSub) {
      this.resSub.unsubscribe();
    }
    if (this.speechEndSub) {
      this.resSub.unsubscribe();
    }
    if (this.speechResultSub) {
      this.resSub.unsubscribe();
    }
  }

  /**
   * The user pressed the enter-key.
   */
  inputFieldEnterPressed() {
    this.sendMessage();
  }

  /**
   * Start speech recognition.
   */
  toggleSpeechRecognition(): void {
    if (this.recording) {
      this.stt.stop();
    } else {
      this.stt.start();
    }
  }
}
