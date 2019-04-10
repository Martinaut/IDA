package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.session.Session;
import at.jku.dke.inga.scxml.session.SessionContextModel;
import at.jku.dke.inga.scxml.session.SessionManager;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.Action;
import org.apache.commons.scxml2.model.Data;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base-class for all SCXML-Actions.
 */
public abstract class BaseAction extends Action {
    /**
     * The logger.
     */
    protected final Logger logger;

    /**
     * Instantiates a new instance of class {@linkplain BaseAction}.
     */
    protected BaseAction() {
        this.logger = LogManager.getLogger(getClass());
    }

    /**
     * Executes the action operations.
     *
     * @param actionExecutionContext The application execution context.
     */
    @Override
    public final void execute(ActionExecutionContext actionExecutionContext) throws ModelException, SCXMLExpressionException {
        logger.info("Executing action '{}' in state '{}'.", getClass().getSimpleName(), getCurrentState());
        execute(actionExecutionContext, getSessionContextModel(actionExecutionContext));
    }

    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     * @throws ModelException           If an error occurred while executing the action.
     * @throws SCXMLExpressionException If an error occurred while executing the action.
     */
    protected abstract void execute(final ActionExecutionContext ctx, final SessionContextModel ctxModel) throws ModelException, SCXMLExpressionException;

    /**
     * Returns the session ID.
     *
     * @param ctx The action execution context.
     * @return The session ID.
     */
    protected final String getSessionId(ActionExecutionContext ctx) {
        Data data = ctx.getStateMachine().getDatamodel().getData().stream()
                .filter(x -> x.getId().equals("sessionId"))
                .findFirst()
                .orElse(null);
        if (data == null) return null;
        if (data.getExpr() == null) return null;
        if (data.getExpr().startsWith("'") && data.getExpr().endsWith("'"))
            return data.getExpr().substring(1, data.getExpr().length() - 1);
        return data.getExpr();
    }

    /**
     * Returns the current state.
     *
     * @return The current state.
     * @throws ModelException If the state could not be determined.
     */
    protected final String getCurrentState() throws ModelException {
        return getParentEnterableState().getId();
    }

    /**
     * Returns the context model.
     *
     * @param ctx The action execution context.
     * @return The context model. If it does not exist {@code null} will be returned.
     */
    private SessionContextModel getSessionContextModel(ActionExecutionContext ctx) {
        Session s = SessionManager.getInstance().getSession(getSessionId(ctx));
        if (s == null)
            return null;
        return s.getSessionContextModel();
    }
}
