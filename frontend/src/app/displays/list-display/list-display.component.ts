import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { TextToSpeechService } from '../../services';

interface ListDisplay {
  displayMessage: string;
  data: Array<{ title: string, details: string, displayableId: string }>;
}

@Component({
  selector: 'app-list-display',
  templateUrl: './list-display.component.html'
})
export class ListDisplayComponent implements OnChanges {

  @Input() display: ListDisplay;

  /**
   * Initializes a new instance of class ListDisplayComponent.
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

  private voiceOutput(display: ListDisplay) {
    let text = display.displayMessage + '\r\n';

    let i = 1;
    const option = this.translateService.instant('display.option');
    for (const d of display.data) {
      text += option + i + ': '; // option
      text += d.title + '\r\n';
      i++;
    }

    setTimeout(() => this.tts.speak(text), 600);
  }
}
