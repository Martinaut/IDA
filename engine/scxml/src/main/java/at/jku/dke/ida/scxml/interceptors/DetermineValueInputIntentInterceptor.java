package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.models.ValueIntentServiceModel;
import at.jku.dke.ida.rules.results.StringConfidenceResult;
import at.jku.dke.ida.shared.EventNames;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineValueInputIntent} action.
 */
public interface DetermineValueInputIntentInterceptor extends Interceptor<ValueIntentServiceModel, Collection<StringConfidenceResult>, String> {
    @Override
    default String modifyResult(Collection<StringConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT)).getValue();
    }
}
