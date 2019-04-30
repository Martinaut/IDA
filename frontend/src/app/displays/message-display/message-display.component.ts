import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { TextToSpeechService } from '../../services';

interface MessageDisplay {
  displayMessage: string;
}

@Component({
  selector: 'app-message-display',
  templateUrl: './message-display.component.html'
})
export class MessageDisplayComponent implements OnChanges {

  @Input() display: MessageDisplay;

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
