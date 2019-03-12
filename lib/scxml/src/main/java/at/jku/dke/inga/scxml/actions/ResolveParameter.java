package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.rules.models.ResolveActionModel;
import at.jku.dke.inga.rules.models.ResolveParameterModel;
import at.jku.dke.inga.rules.services.ActionResolver;
import at.jku.dke.inga.rules.services.ParameterResolver;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.display.Displayable;
import at.jku.dke.inga.shared.display.ListDisplay;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResolveParameter extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'ResolveParameter'.");

        // Get Data
        String input = ctxModel.getUserInput();

        // Resolve Data
        ResolveParameterModel model = new ResolveParameterModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                input,
                convertDisplayToOperationsMap(ctxModel.getDisplayData())
        );
        String event = new ParameterResolver().resolveParameter(model);

        ((NonComparativeAnalysisSituation)ctxModel.getAnalysisSituation()).setCube(event);

        // Send to next state
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(EventNames.RESOLVED, TriggerEvent.SIGNAL_EVENT));
    }


    @SuppressWarnings("Duplicates")
    private Map<Integer, Displayable> convertDisplayToOperationsMap(Display display) {
        if (display == null) {
            logger.error("Display is null, but should have value.");
            return new HashMap<>();
        }
        if (!(display instanceof ListDisplay)) {
            logger.error("Display should be of type ListDisplay, but is of type " + display.getClass());
            return new HashMap<>();
        }
        ListDisplay lDisplay = (ListDisplay) display;

        if (lDisplay.getData().stream().noneMatch(x -> x instanceof Label)) {
            logger.error("Display should contain items of type Operation, but does not.");
            return new HashMap<>();
        }

        Map<Integer, Displayable> map = new HashMap<>();
        int i = 1;
        for (Displayable d : lDisplay.getData()) {
            map.put(i, d);
        }
        return map;
    }
}
