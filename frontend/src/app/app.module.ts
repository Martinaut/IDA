import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {AppComponent} from './app.component';
import {ConnectionPanelComponent} from './connection-panel/connection-panel.component';
import { AnalysisSituationPanelComponent } from './analysis-situation-panel/analysis-situation-panel.component';
import { InputPanelComponent } from './input-panel/input-panel.component';
import { ResultPanelComponent } from './result/result-panel/result-panel.component';
import { ListDisplayComponent } from './result/list-display/list-display.component';
import { VoiceSettingsPanelComponent } from './voice-settings-panel/voice-settings-panel.component';
import { TwoListDisplayComponent } from './result/two-list-display/two-list-display.component';

/**
 * The application module with all components.
 */
@NgModule({
  declarations: [
    AppComponent,
    ConnectionPanelComponent,
    AnalysisSituationPanelComponent,
    InputPanelComponent,
    ResultPanelComponent,
    ListDisplayComponent,
    VoiceSettingsPanelComponent,
    TwoListDisplayComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
