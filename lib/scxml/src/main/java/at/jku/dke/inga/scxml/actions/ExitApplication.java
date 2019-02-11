package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.context.ContextManager;
import at.jku.dke.inga.scxml.context.ContextModel;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.Action;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action deletes the current context.
 */
@SuppressWarnings("unused")
public class ExitApplication extends BaseAction {

    /**
     * Executes the action.
     *
     * @param ctx The action execution context.
     * @param ctxModel The context data.
     */
    @Override
    public void execute(final ActionExecutionContext ctx, final ContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'ExitApplication'.");
        ContextManager.deleteContext(getContextId(ctx));

        // TODO: exit application or other cleanup
    }

}
