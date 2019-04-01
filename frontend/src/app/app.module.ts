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
import { ResultPanelComponent } from './result/result-panel/result-panel.component';
import { ListDisplayComponent } from './result/list-display/list-display.component';
import { VoiceSettingsPanelComponent } from './voice-settings-panel/voice-settings-panel.component';
import { TwoListDisplayComponent } from './result/two-list-display/two-list-display.component';
import { NonComparativeComponent } from './analysis-situation-panel/non-comparative/non-comparative.component';
import { ComparativeComponent } from './analysis-situation-panel/comparative/comparative.component';

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
    TwoListDisplayComponent,
    NonComparativeComponent,
    ComparativeComponent
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
