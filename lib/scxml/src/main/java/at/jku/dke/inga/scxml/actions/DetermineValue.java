package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.SetValueServiceModel;
import at.jku.dke.inga.rules.models.ValueDeterminationServiceModel;
import at.jku.dke.inga.rules.services.SetValueService;
import at.jku.dke.inga.rules.services.ValueDeterminationService;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.EventNames;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action determines the selected value and sets the value accordingly in the analysis situation.
 */
public class DetermineValue extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException {
        // Execute
        String value = determineValue(ctxModel);
        setValue(ctxModel, value);
// TODO: add inavlidInputTransition
        // Trigger event
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(EventNames.DETERMINED, TriggerEvent.SIGNAL_EVENT));
    }

    private String determineValue(ContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValueDeterminationServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                ctxModel.getUserInput(),
                ctxModel.getDisplayData()
        );

        // Determine value
        return new ValueDeterminationService().executeRules(model);
    }

    private void setValue(ContextModel ctxModel, String value) throws ModelException {
        // Get data
        var model = new SetValueServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                value,
                ctxModel.getOperation()
        );

        // Execute
        new SetValueService().executeRules(model);
    }
}
