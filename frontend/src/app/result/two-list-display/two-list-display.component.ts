import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { TextToSpeechService } from '../../services';

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
  constructor(private tts: TextToSpeechService,
              private translateService: TranslateService) {
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

    text += this.translateService.instant('result.leftSide') + ': \r\n'; // on the left side
    let i = 1;
    for (const d of display.dataLeft) {
      text += this.translateService.instant('result.leftOption') + ' ' + i + ': '; // Left Option
      text += d.title + '\r\n';
      i++;
    }

    text += this.translateService.instant('result.rightSide') + ': \r\n'; // on the right side
    i = 1;
    for (const d of display.dataRight) {
      text += this.translateService.instant('result.rightOption') + ' ' + i + ': '; // Right Option
      text += d.title + '\r\n';
      i++;
    }

    this.tts.speak(text);
  }
}
