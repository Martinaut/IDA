package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.interfaces.ValueIntentServiceModel;
import at.jku.dke.ida.rules.models.DefaultValueIntentServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.rules.services.ValueIntentService;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInputIntentInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

import java.util.Collection;

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
        ValueIntentServiceModel model = new DefaultValueIntentServiceModel(
                getCurrentState(),
                ctxModel
        );
        var interceptor = BeanUtil.getOptionalBean(DetermineValueInputIntentInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine possible events
        Event operation;
        Collection<EventConfidenceResult> operations = new ValueIntentService().executeRules(model);
        if (interceptor != null)
            operation = interceptor.modifyResult(operations);
        else
            operation = operations.stream().sorted()
                    .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT)).getValue();

        // Trigger event
        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(operation.getEventName(), TriggerEvent.SIGNAL_EVENT));
    }
}
