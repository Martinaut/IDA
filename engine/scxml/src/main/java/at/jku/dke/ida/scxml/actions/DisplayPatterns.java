package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.operations.Pattern;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;

import java.util.ArrayList;
import java.util.List;

/**
 * This action identifies patterns from which the user can select one.
 */
public class DisplayPatterns extends BaseAction {

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
        List<Displayable> list = new ArrayList<>();
        list.add(new Pattern(Pattern.NONCOMPARATIVE, ctxModel.getLocale()));
        list.add(new Pattern(Pattern.COMPARATIVE, ctxModel.getLocale()));
        ctxModel.setDisplayData(new ListDisplay("selectPattern", ctxModel.getLocale(), list));
    }
}
