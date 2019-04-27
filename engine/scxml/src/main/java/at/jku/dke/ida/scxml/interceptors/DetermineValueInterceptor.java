package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.ValueServiceModel;
import at.jku.dke.ida.rules.results.ConfidenceResult;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.shared.Event;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineValue} action.
 */
public interface DetermineValueInterceptor extends Interceptor<ValueServiceModel, Collection<ConfidenceResult>, ConfidenceResult> {
    @Override
    default ConfidenceResult modifyResult(Collection<ConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT));
    }
}
