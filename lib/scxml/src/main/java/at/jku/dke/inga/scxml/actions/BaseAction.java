package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.scxml.context.ContextManager;
import at.jku.dke.inga.scxml.context.ContextModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.Action;
import org.apache.commons.scxml2.model.Data;
import org.apache.commons.scxml2.model.Datamodel;
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
        execute(actionExecutionContext, getContext(actionExecutionContext));
    }

    /**
     * Executes the action operations.
     *
     * @param ctx      The application execution context.
     * @param ctxModel The context data.
     */
    protected abstract void execute(final ActionExecutionContext ctx, final ContextModel ctxModel) throws ModelException, SCXMLExpressionException;

    /**
     * Returns the context ID.
     *
     * @param ctx The action execution context.
     * @return The context ID.
     */
    protected final String getContextId(ActionExecutionContext ctx) {
        return getFromDataModel(ctx.getStateMachine().getDatamodel(), "contextId");
    }

    /**
     * Returns the context model.
     *
     * @param ctx The action execution context.
     * @return The context model. If it does not exist an empty context model will be returned.
     */
    private ContextModel getContext(ActionExecutionContext ctx) {
        return ContextManager.getContext(getContextId(ctx));
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
     * Returns the value from the state-machine data model.
     * <p>
     * If the data with the specified {@code key} does not exist or if the data
     * is {@code null}, {@code null} will be returned.
     *
     * @param datamodel The data model.
     * @param key       The id of the data to be retrieved.
     * @return The Expression of the data or {@code null}.
     */
    private static String getFromDataModel(Datamodel datamodel, String key) {
        Data data = datamodel.getData().stream()
                .filter(x -> x.getId().equals(key))
                .findFirst()
                .orElse(null);
        if (data == null) return null;
        if (data.getExpr() == null) return null;
        if (data.getExpr().startsWith("'") && data.getExpr().endsWith("'"))
            return data.getExpr().substring(1, data.getExpr().length() - 1);
        return data.getExpr();
    }

    /**
     * Sets or overrides data in the state-machine data model.
     *
     * @param datamodel The data model.
     * @param key       The id of the data to be set.
     * @param value     The value to be set.
     */
    private static void setData(Datamodel datamodel, String key, String value) {
        Data data = datamodel.getData().stream()
                .filter(x -> x.getId().equals(key))
                .findFirst()
                .orElse(null);
        if (data == null) {
            data = new Data();
            data.setId(key);
            datamodel.addData(data);
        }
        data.setExpr("'" + value + "'");
    }
}
