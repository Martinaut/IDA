import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { TextToSpeechService } from '../../services';

interface ErrorDisplay {
  displayMessage: string;
}

@Component({
  selector: 'app-error-display',
  templateUrl: './error-display.component.html'
})
export class ErrorDisplayComponent implements OnChanges {

  @Input() display: ErrorDisplay;

  /**
   * Initializes a new instance of class MessageDisplayComponent.
   */
  constructor(private tts: TextToSpeechService) {
  }

  /**
   * A lifecycle hook that is called when any data-bound property of the directive changes.
   *
   * @param changes The change.
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes.display) {
      this.tts.speak(changes.display.currentValue.displayMessage);
    }
  }

}
