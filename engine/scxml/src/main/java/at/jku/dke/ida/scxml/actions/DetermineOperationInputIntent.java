package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.models.OperationIntentServiceModel;
import at.jku.dke.ida.rules.results.StringConfidenceResult;
import at.jku.dke.ida.rules.services.OperationIntentService;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInputIntentInterceptor;
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
 * This action determines the intent of the user based on the input data.
 * The input data are the users input after the possible operation are displayed.
 */
public class DetermineOperationInputIntent extends BaseAction {
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
        var model = new OperationIntentServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                ctxModel.getUserInput(),
                convertDisplayToOperationsMap(ctxModel.getDisplayData())
        );
        var interceptor = BeanUtil.getOptionalBean(DetermineOperationInputIntentInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine possible events
        String operation;
        Collection<StringConfidenceResult> operations = new OperationIntentService().executeRules(model);
        if (interceptor != null)
            operation = interceptor.modifyResult(operations);
        else
            operation = operations.stream().sorted()
                    .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT)).getValue();

        // Trigger event
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(operation, TriggerEvent.SIGNAL_EVENT));
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
