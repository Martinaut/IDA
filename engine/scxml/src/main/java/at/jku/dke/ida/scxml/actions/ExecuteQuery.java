package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.query.CSVPrettifier;
import at.jku.dke.ida.scxml.query.QueryExecutor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;

import java.util.ArrayList;
import java.util.List;

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

        // Build operations list
        List<Displayable> ops = new ArrayList<>(2);
        ops.add(new Operation(Event.REVISE_QUERY, ctxModel.getLocale(), 1));
        ops.add(new Operation(Event.EXIT, ctxModel.getLocale(), 2));
        ctxModel.setDisplayData(new ListDisplay("selectOperation", ctxModel.getLocale(), ops));
    }
}
