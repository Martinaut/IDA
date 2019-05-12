package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.PatternServiceModel;
import at.jku.dke.ida.rules.results.ConfidenceResult;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.shared.Event;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DeterminePatternSelection} action.
 */
public interface DeterminePatternSelectionInterceptor extends Interceptor<PatternServiceModel, Collection<ConfidenceResult>, ConfidenceResult> {
    @Override
    default ConfidenceResult modifyResult(PatternServiceModel model, Collection<ConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT));
    }
}
