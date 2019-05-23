package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.interfaces.OperationServiceModel;
import at.jku.dke.ida.rules.models.DefaultOperationServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.rules.services.OperationService;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This action determines the selected operation and triggers the appropriate event.
 */
public class DetermineOperation extends BaseAction {
    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @SuppressWarnings("Duplicates")
    @Override
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException {
        // Get data
        OperationServiceModel model = new DefaultOperationServiceModel(
                getCurrentState(),
                ctxModel,
                convertDisplayToOperationsMap(ctxModel.getDisplayData())
        );
        var interceptor = BeanUtil.getOptionalBean(DetermineOperationInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine possible events
        Collection<EventConfidenceResult> operations = new OperationService().executeRules(model);
        if (interceptor != null)
            ctxModel.setOperation(interceptor.modifyResult(model, operations));
        else
            ctxModel.setOperation(operations.stream().sorted()
                    .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT)).getValue());

        // Trigger event
        if (ctxModel.getOperation().equals(Event.INVALID_INPUT) || ctxModel.getOperation().equals(Event.EXIT)) {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(ctxModel.getOperation().getEventName(), TriggerEvent.SIGNAL_EVENT));
        } else {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.DETERMINED.getEventName(), TriggerEvent.SIGNAL_EVENT));
        }
    }

    private Map<Integer, Operation> convertDisplayToOperationsMap(Display display) {
        if (display == null) {
            logger.fatal("Display is null, but should have value.");
            return new HashMap<>();
        }
        if (!(display instanceof ListDisplay)) {
            logger.error("Display should be of type ListDisplay, but is of type " + display.getClass());
            return new HashMap<>();
        }

        ListDisplay lDisplay = (ListDisplay) display;
        if (!lDisplay.getData().stream().allMatch(x -> x instanceof Operation)) {
            logger.error("Display should only contain items of type Operation, but does not.");
            return new HashMap<>();
        }

        AtomicInteger i = new AtomicInteger(1);
        return lDisplay.getData().stream()
                .map(x -> (Operation) x)
                .collect(Collectors.toMap(o -> i.getAndIncrement(), Function.identity()));
    }
}
