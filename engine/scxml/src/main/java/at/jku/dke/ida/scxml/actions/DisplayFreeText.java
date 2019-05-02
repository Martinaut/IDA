package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.MessageDisplay;
import at.jku.dke.ida.shared.operations.Pattern;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
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
        if (ctxModel.isComparativePattern()) {
            switch (ctxModel.getComparativeActiveAS()) {
                case Pattern.SI:
                    ctxModel.setDisplayData(new MessageDisplay("enterQuerySI", ctxModel.getLocale()));
                    break;
                case Pattern.SC:
                    ctxModel.setDisplayData(new MessageDisplay("enterQuerySC", ctxModel.getLocale()));
                    break;
                default:
                    ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.EXIT.getEventName(), TriggerEvent.SIGNAL_EVENT));
                    break;
            }
        } else
            ctxModel.setDisplayData(new MessageDisplay("enterQuery", ctxModel.getLocale()));
    }

}
