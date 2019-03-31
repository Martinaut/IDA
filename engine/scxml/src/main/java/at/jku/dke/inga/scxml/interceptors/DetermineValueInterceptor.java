package at.jku.dke.inga.scxml.interceptors;

import at.jku.dke.inga.rules.models.ValueServiceModel;
import at.jku.dke.inga.rules.results.ConfidenceResult;
import at.jku.dke.inga.rules.results.StringConfidenceResult;
import at.jku.dke.inga.shared.EventNames;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.inga.scxml.actions.DetermineValue} action.
 */
public interface DetermineValueInterceptor extends Interceptor<ValueServiceModel, Collection<ConfidenceResult>, ConfidenceResult> {
    @Override
    default ConfidenceResult modifyResult(Collection<ConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT));
    }
}
