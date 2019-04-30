import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppComponent } from './app.component';
import { ConnectionPanelComponent } from './connection-panel/connection-panel.component';
import { AnalysisSituationPanelComponent } from './analysis-situation-panel/analysis-situation-panel.component';
import { InputPanelComponent } from './input-panel/input-panel.component';
import { DisplayPanelComponent } from './displays/display-panel/display-panel.component';
import { ListDisplayComponent } from './displays/list-display/list-display.component';
import { VoiceSettingsPanelComponent } from './voice-settings-panel/voice-settings-panel.component';
import { TwoListDisplayComponent } from './displays/two-list-display/two-list-display.component';
import { NonComparativeComponent } from './analysis-situation-panel/non-comparative/non-comparative.component';
import { ComparativeComponent } from './analysis-situation-panel/comparative/comparative.component';
import { ErrorDisplayComponent } from './displays/error-display/error-display.component';
import { ResultPanelComponent } from './result-panel/result-panel.component';
import { MessageDisplayComponent } from './displays/message-display/message-display.component';
import { SortableColumnComponent } from './result-panel/sortable-column.component';

/**
 * The application module with all components.
 */
@NgModule({
  declarations: [
    AppComponent,
    ConnectionPanelComponent,
    AnalysisSituationPanelComponent,
    InputPanelComponent,
    DisplayPanelComponent,
    ListDisplayComponent,
    VoiceSettingsPanelComponent,
    TwoListDisplayComponent,
    NonComparativeComponent,
    ComparativeComponent,
    ErrorDisplayComponent,
    MessageDisplayComponent,
    ResultPanelComponent,
    SortableColumnComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    NgbModule,

    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}

// required for AOT compilation
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/');
}
