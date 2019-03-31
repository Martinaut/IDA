package at.jku.dke.inga.scxml.interceptors;

import at.jku.dke.inga.rules.models.OperationIntentServiceModel;
import at.jku.dke.inga.rules.results.StringConfidenceResult;
import at.jku.dke.inga.shared.EventNames;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.inga.scxml.actions.DetermineOperationInputIntent} action.
 */
public interface DetermineOperationInputIntentInterceptor extends Interceptor<OperationIntentServiceModel, Collection<StringConfidenceResult>, String> {
    @Override
    default String modifyResult(Collection<StringConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT)).getValue();
    }
}
