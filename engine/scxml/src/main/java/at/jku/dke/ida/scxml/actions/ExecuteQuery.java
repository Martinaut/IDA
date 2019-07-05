package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.query.CSVPrettifier;
import at.jku.dke.ida.scxml.query.QueryExecutor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.shared.spring.BeanUtil;
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
        String csv = BeanUtil.getBean(QueryExecutor.class).executeQuery(ctxModel);
        if (csv == null) {
            ctxModel.setDisplayData(new ErrorDisplay("errorExecuteQuery", ctxModel.getLocale()));
            return;
        }

        // Request pretty labels
        csv = BeanUtil.getBean(CSVPrettifier.class).prettify(ctxModel.getLocale().getLanguage(), csv);
        if (csv == null) {
            ctxModel.setDisplayData(new ErrorDisplay("errorExecuteQuery", ctxModel.getLocale()));
            return;
        }

        // Set result
        ctxModel.setQueryResult(csv);
    }
}
