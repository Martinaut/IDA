package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;

public class DisplayValues extends BaseAction {

    /**
     * Executes the action.
     *
     * @param ctx The action execution context.
     */
    @Override
    public void execute(ActionExecutionContext ctx) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'DisplayValues'.");

        // Get Data
        // TODO: make usable with comparative AS
        String operation = getContext(ctx).getOperation();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getContext(ctx).getAnalysisSituation();

        // Resolve Data


        // Send to display
        // TODO
    }

}
