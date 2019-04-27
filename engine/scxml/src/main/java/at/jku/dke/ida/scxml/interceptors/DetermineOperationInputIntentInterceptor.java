package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.OperationIntentServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.shared.Event;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineOperationInputIntent} action.
 */
public interface DetermineOperationInputIntentInterceptor extends Interceptor<OperationIntentServiceModel, Collection<EventConfidenceResult>, Event> {
    @Override
    default Event modifyResult(Collection<EventConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT)).getValue();
    }
}
