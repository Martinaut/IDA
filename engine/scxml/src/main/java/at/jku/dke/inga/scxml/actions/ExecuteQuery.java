package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.session.SessionContextModel;
import org.apache.commons.scxml2.ActionExecutionContext;

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
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) {
    }
}
