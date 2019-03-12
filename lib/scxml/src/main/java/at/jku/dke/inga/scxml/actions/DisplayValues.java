package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.ValuesDisplayDeterminationServiceModel;
import at.jku.dke.inga.rules.services.ValuesDisplayDeterminationService;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.display.Display;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action identifies values from which the user can select a value.
 * Values can be cubes, measures, ...
 */
public class DisplayValues extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValuesDisplayDeterminationServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                ctxModel.getOperation()
        );

        // Determine display data
        Display display = new ValuesDisplayDeterminationService().executeRules(model);

        // Send to display
        ctxModel.setDisplayData(display);
    }

}
