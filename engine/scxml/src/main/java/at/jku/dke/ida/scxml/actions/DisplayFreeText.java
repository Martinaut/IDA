package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.display.MessageDisplay;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;

/**
 * Displays a message depending on the type of pattern that asks the user what to do.
 */
public class DisplayFreeText extends BaseAction {

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     * @throws ModelException           If an error occurred while executing the action.
     * @throws SCXMLExpressionException If an error occurred while executing the action.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException, SCXMLExpressionException {
        ctxModel.setDisplayData(new MessageDisplay("enterQuery", ctxModel.getLocale()));
    }

}
