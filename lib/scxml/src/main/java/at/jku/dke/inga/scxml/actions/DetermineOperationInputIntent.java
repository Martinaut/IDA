package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.OperationIntentDeterminationServiceModel;
import at.jku.dke.inga.rules.services.OperationIntentDeterminationService;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.display.ListDisplay;
import at.jku.dke.inga.shared.operations.Operation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

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
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException {
        // Get data
        var model = new OperationIntentDeterminationServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                ctxModel.getUserInput(),
                convertDisplayToOperationsMap(ctxModel.getDisplayData())
        );

        // Determine event
        String event = new OperationIntentDeterminationService().executeRules(model);

        // Trigger event
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
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
