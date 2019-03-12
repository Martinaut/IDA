package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.ValueIntentDeterminationServiceModel;
import at.jku.dke.inga.rules.services.ValueIntentDeterminationService;
import at.jku.dke.inga.scxml.context.ContextModel;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action determines the intent of the user based on the input data.
 * The input data are the users input after the possible values are displayed.
 */
public class DetermineValueInputIntent extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValueIntentDeterminationServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                ctxModel.getUserInput(),
                ctxModel.getDisplayData()
        );

        // Determine event
        String event = new ValueIntentDeterminationService().executeRules(model);

        // Trigger event
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT));
    }

}
