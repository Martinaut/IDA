package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.ResolveValueInputModel;
import at.jku.dke.inga.rules.services.ValueInputResolver;
import at.jku.dke.inga.scxml.context.ContextModel;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

public class ResolveInput extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'ResolveInput'.");

        // Get Data
        String input = ctxModel.getUserInput();


        // Resolve Data
        ResolveValueInputModel model = new ResolveValueInputModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                input
        );
        String event = new ValueInputResolver().resolveValueInput(model);

        // Send to next state
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
    }
}
