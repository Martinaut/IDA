package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.models.ValueIntentServiceModel;
import at.jku.dke.ida.rules.results.StringConfidenceResult;
import at.jku.dke.ida.rules.services.ValueIntentService;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInputIntentInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.EventNames;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

import java.util.Collection;
import java.util.HashMap;

/**
 * This action determines the intent of the user based on the input data.
 * The input data are the users input after the possible values are displayed.
 */
public class DetermineValueInputIntent extends BaseAction {
    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @SuppressWarnings("Duplicates")
    @Override
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValueIntentServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                ctxModel.getUserInput(),
                ctxModel.getDisplayData()
        );
        var interceptor = BeanUtil.getOptionalBean(DetermineValueInputIntentInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine possible events
        String operation;
        Collection<StringConfidenceResult> operations = new ValueIntentService().executeRules(model);
        if (interceptor != null)
            operation = interceptor.modifyResult(operations);
        else
            operation = operations.stream().sorted()
                    .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT)).getValue();
        ctxModel.setAdditionalData(new HashMap<>(model.getAdditionalData()));

        // Trigger event
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(operation, TriggerEvent.SIGNAL_EVENT));
    }
}
