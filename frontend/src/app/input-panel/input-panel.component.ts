import { ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { ConnectionService, SpeechToTextService, TextToSpeechService } from '../services';

/**
 * Displays an input field for the user query.
 */
@Component({
  selector: 'app-input-panel',
  templateUrl: './input-panel.component.html'
})
export class InputPanelComponent implements OnInit, OnDestroy {

  private connSub: Subscription;
  private dispSub: Subscription;
  private resSub: Subscription;

  private speechStartSub: Subscription;
  private speechEndSub: Subscription;
  private speechResultSub: Subscription;

  connected: boolean;
  waitingForResult: boolean;
  inputError: boolean;
  showReviseQueryBtn: boolean;

  sttSupported: boolean;
  recording: boolean;

  userInput: string;
  @ViewChild('input') inputField: ElementRef;

  /**
   * Initializes a new instance of class InputPanelComponent.
   */
  constructor(private connectionService: ConnectionService,
              private stt: SpeechToTextService,
              private tts: TextToSpeechService,
              private changeDetector: ChangeDetectorRef) {
    this.connected = false;
    this.waitingForResult = false;
    this.inputError = false;
    this.sttSupported = false;
    this.recording = false;
    this.showReviseQueryBtn = false;
  }

  /**
   * Sends a message with the user input to the server.
   */
  sendMessage(): void {
    if ((this.userInput == null || this.userInput.trim().length === 0) && this.connectionService.isConnected()) {
      this.inputError = true;
      return;
    }
    this.inputError = false;
    this.waitingForResult = true;
    this.tts.stop();
    this.stt.stop();
    this.connectionService.sendMessage(this.userInput);
  }

  /**
   * A lifecycle hook that is called after Angular has initialized  all data-bound properties of a directive.
   */
  ngOnInit(): void {
    this.sttSupported = SpeechToTextService.isSupported();
    this.connSub = this.connectionService.initializedStateChanged.subscribe(value => {
      this.connected = value;
      if (!value) {
        this.stt.stop();
      }
    });
    this.dispSub = this.connectionService.displayMessageReceived.subscribe(value => {
      this.waitingForResult = false;
      this.userInput = null;

      if (value != null && JSON.parse(value).type === 'ExitDisplay') {
        this.connectionService.disconnect();
      } else {
        if (!this.showReviseQueryBtn) {
          setTimeout(() => {
            this.inputField.nativeElement.focus();

            if (this.stt.autoStart) {
              this.stt.start();
            }
          }, 100);
        }
      }
    });
    this.resSub = this.connectionService.resultMessageReceived.subscribe(value => {
      if (value != null && value.trim().length > 0) {
        this.showReviseQueryBtn = true;
        this.stt.stop();
      }
    });

    this.speechStartSub = this.stt.started.subscribe(value => {
      this.recording = true;
      this.changeDetector.detectChanges();
    });
    this.speechEndSub = this.stt.ended.subscribe(value => {
      this.recording = false;
      this.changeDetector.detectChanges();

      if (this.userInput != null && this.userInput.trim().length > 0) {
        this.sendMessage();
      }
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
    if (this.dispSub) {
      this.dispSub.unsubscribe();
    }
    if (this.resSub) {
      this.resSub.unsubscribe();
    }
    if (this.speechStartSub) {
      this.dispSub.unsubscribe();
    }
    if (this.speechEndSub) {
      this.dispSub.unsubscribe();
    }
    if (this.speechResultSub) {
      this.dispSub.unsubscribe();
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

  /**
   * The user clicked the revise query button.
   */
  reviseQuery(): void {
    this.waitingForResult = true;
    this.tts.stop();
    this.connectionService.sendReviseQuery();
    this.showReviseQueryBtn = false;
  }
}
