package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.display.ExitDisplay;
import org.apache.commons.scxml2.ActionExecutionContext;

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
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) {
        ctxModel.setDisplayData(new ExitDisplay());
        SessionManager.getInstance().deleteSession(getSessionId(ctx));
    }
}
