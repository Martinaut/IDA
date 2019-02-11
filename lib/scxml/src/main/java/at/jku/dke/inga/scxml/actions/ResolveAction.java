package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.ResolveActionModel;
import at.jku.dke.inga.rules.services.ActionResolver;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResolveAction extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'ResolveAction'.");

        // Get Data
        String input = ctxModel.getUserInput();

        // Resolve Data
        ResolveActionModel model = new ResolveActionModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                input,
                convertDisplayToOperationsMap(ctxModel.getDisplayData())
        );
        String event = new ActionResolver().resolveAction(model);

        // Send to next state
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
    }

    @SuppressWarnings("Duplicates")
    private Map<Integer, Operation> convertDisplayToOperationsMap(Display display) {
        if (display == null) {
            logger.error("Display is null, but should have value.");
            return new HashMap<>();
        }
        if (!(display instanceof ListDisplay)) {
            logger.error("Display should be of type ListDisplay, but is of type " + display.getClass());
            return new HashMap<>();
        }
        ListDisplay lDisplay = (ListDisplay) display;

        if (lDisplay.getData().stream().noneMatch(x -> x instanceof Operation)) {
            logger.error("Display should contain items of type Operation, but does not.");
            return new HashMap<>();
        }

        return lDisplay.getData().stream()
                .map(x -> (Operation) x)
                .collect(Collectors.toMap(Operation::getPosition, Function.identity()));
    }
}
