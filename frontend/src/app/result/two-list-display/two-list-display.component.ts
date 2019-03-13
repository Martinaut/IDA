import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {TextToSpeechService} from '../../services';

interface TwoListDisplay {
  displayMessage: string;
  dataLeft: Array<{ title: string, details: string, displayableId: string }>;
  dataRight: Array<{ title: string, details: string, displayableId: string }>;
}

@Component({
  selector: 'app-two-list-display',
  templateUrl: './two-list-display.component.html',
  styles: []
})
export class TwoListDisplayComponent implements OnChanges {

  @Input() display: TwoListDisplay;

  /**
   * Initializes a new instance of class TwoListDisplayComponent.
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
      this.voiceOutput(changes.display.currentValue);
    }
  }

  private voiceOutput(display: TwoListDisplay) {
    let text = display.displayMessage + '\r\n';

    text += 'On the left side: \r\n';
    let i = 1;
    for (const d of display.dataLeft) {
      text += 'Left Option ' + i + ': ';
      text += d.title + '\r\n';
      i++;
    }

    text += 'On the right side: \r\n';
    i = 1;
    for (const d of display.dataRight) {
      text += 'Right Option ' + i + ': ';
      text += d.title + '\r\n';
      i++;
    }

    this.tts.speak(text);
  }
}
