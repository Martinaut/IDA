package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.OperationServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.shared.Event;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineOperation} action.
 */
public interface DetermineOperationInterceptor extends Interceptor<OperationServiceModel, Collection<EventConfidenceResult>, Event> {
    @Override
    default Event modifyResult(OperationServiceModel model, Collection<EventConfidenceResult> result) {
        return result.stream()
                .sorted()
                .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT)).getValue();
    }
}
