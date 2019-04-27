package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.models.OperationServiceModel;
import at.jku.dke.ida.rules.results.StringConfidenceResult;
import at.jku.dke.ida.rules.services.OperationService;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.EventNames;
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
        var model = new OperationServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                ctxModel.getUserInput(),
                convertDisplayToOperationsMap(ctxModel.getDisplayData())
        );
        var interceptor = BeanUtil.getOptionalBean(DetermineOperationInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine possible events
        Collection<StringConfidenceResult> operations = new OperationService().executeRules(model);
        if (interceptor != null)
            ctxModel.setOperation(interceptor.modifyResult(operations));
        else
            ctxModel.setOperation(operations.stream().sorted()
                    .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT)).getValue());
        ctxModel.setAdditionalData(new HashMap<>(model.getAdditionalData()));

        // Trigger event
        if (ctxModel.getOperation().equals(EventNames.INVALID_INPUT) || ctxModel.getOperation().equals(EventNames.EXIT)) {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(ctxModel.getOperation(), TriggerEvent.SIGNAL_EVENT));
        } else {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(EventNames.DETERMINED, TriggerEvent.SIGNAL_EVENT));
        }
    }

    @SuppressWarnings("Duplicates")
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
