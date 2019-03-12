package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.context.ContextManager;
import at.jku.dke.inga.scxml.context.ContextModel;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action deletes the current context.
 */
public class ExitApplication extends BaseAction {
    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        ContextManager.deleteContext(getContextId(ctx));
        // TODO: other cleanup? evtl. mittels events?
    }
}
