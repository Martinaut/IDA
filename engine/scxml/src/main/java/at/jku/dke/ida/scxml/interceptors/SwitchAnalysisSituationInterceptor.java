package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.SwitchAnalysisSituationServiceModel;
import at.jku.dke.ida.rules.results.AnalysisSituationConfidenceResult;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineValue} action.
 */
public interface SwitchAnalysisSituationInterceptor extends Interceptor<SwitchAnalysisSituationServiceModel, AnalysisSituationConfidenceResult, EngineAnalysisSituation> {
    @Override
    default EngineAnalysisSituation modifyResult(SwitchAnalysisSituationServiceModel model, AnalysisSituationConfidenceResult result) {
        return result.getValue();
    }
}
