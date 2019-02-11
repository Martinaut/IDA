package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.ResolveValuesDataModel;
import at.jku.dke.inga.rules.services.ValuesResolver;
import at.jku.dke.inga.scxml.context.ContextModel;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;

/**
 * Action that determines the values to display.
 */
@SuppressWarnings("unused")
public class DisplayValues extends BaseAction {

    /**
     * Executes the action.
     *
     * @param ctx The action execution context.
     * @param ctxModel The context data.
     */
    @Override
    public void execute(final ActionExecutionContext ctx, final ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'DisplayValues'.");

        // Get Data
        // TODO: make usable with comparative AS
        String operation = ctxModel.getOperation();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) ctxModel.getAnalysisSituation();

        // Resolve Data
        ResolveValuesDataModel model = new ResolveValuesDataModel(
                getCurrentState(),
                as,
                ctxModel.getLocale(),
                operation
        );
        Display display = new ValuesResolver().resolveValues(model);

        // Send to display
        ctxModel.setDisplayData(display);
        // TODO
    }

}
