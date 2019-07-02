package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.ValueIntentServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.shared.Event;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineValueInputIntent} action.
 */
public interface DetermineValueInputIntentInterceptor extends Interceptor<ValueIntentServiceModel, Collection<EventConfidenceResult>, Event> {
    @Override
    default Event modifyResult(ValueIntentServiceModel model, Collection<EventConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new EventConfidenceResult(Event.NAVIGATE)).getValue();
    }
}
