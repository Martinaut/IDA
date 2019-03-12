package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.context.ContextModel;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action executes a query based on the values in the analysis situation and "displays" the result.
 */
public class ExecuteQuery extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
    }

}
