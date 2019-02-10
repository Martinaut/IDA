package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.context.ContextManager;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.Action;
import org.apache.commons.scxml2.model.ModelException;

public class ExitApplication extends BaseAction {

    /**
     * Executes the action.
     *
     * @param ctx The action execution context.
     */
    @Override
    public void execute(ActionExecutionContext ctx) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action 'ExitApplication'.");
        ContextManager.deleteContext(getOrCreateContextId(ctx));

        // TODO: exit application or other cleanup
    }

}
