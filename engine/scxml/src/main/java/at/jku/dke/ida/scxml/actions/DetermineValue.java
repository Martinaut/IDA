package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.interfaces.SetValueServiceModel;
import at.jku.dke.ida.rules.interfaces.ValueServiceModel;
import at.jku.dke.ida.rules.models.DefaultSetValueServiceModel;
import at.jku.dke.ida.rules.models.DefaultValueServiceModel;
import at.jku.dke.ida.rules.results.ConfidenceResult;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.rules.services.SetValueService;
import at.jku.dke.ida.rules.services.ValueService;
import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInterceptor;
import at.jku.dke.ida.scxml.interceptors.SetValueInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

import java.util.HashMap;

/**
 * This action determines the selected value and sets the value accordingly in the analysis situation.
 */
public class DetermineValue extends BaseAction {
    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException {
        ConfidenceResult result = determineValue(ctxModel);
        if (result instanceof EventConfidenceResult) {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(((EventConfidenceResult) result).getValue().getEventName(), TriggerEvent.SIGNAL_EVENT));
        } else {
            setValue(getSessionId(ctx), ctxModel, result);
            ctxModel.setOperation(null);
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.DETERMINED.getEventName(), TriggerEvent.SIGNAL_EVENT));
        }
    }

    private ConfidenceResult determineValue(SessionContextModel ctxModel) throws ModelException {
        // Get data
        ValueServiceModel model = new DefaultValueServiceModel(
                getCurrentState(),
                ctxModel
        );
        var interceptor = BeanUtil.getOptionalBean(DetermineValueInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine value
        ConfidenceResult result;
        var values = new ValueService().executeRules(model);
        if (interceptor != null)
            result = interceptor.modifyResult(values);
        else
            result = values.stream().sorted()
                    .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT));

        return result;
    }

    private void setValue(String sessionId, SessionContextModel ctxModel, ConfidenceResult value) throws ModelException {
        // Get data
        SetValueServiceModel model = new DefaultSetValueServiceModel(
                getCurrentState(),
                ctxModel,
                value.getValue()
        );
        var interceptor = BeanUtil.getOptionalBean(SetValueInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Execute
        new SetValueService().executeRules(model);
        ctxModel.setAdditionalData(new HashMap<>(model.getAdditionalData()));

        // Notify
        if (ctxModel.getAnalysisSituationListener() != null && ctxModel.getAnalysisSituation().isCubeDefined())
            ctxModel.getAnalysisSituationListener().changed(new AnalysisSituationEvent(this, sessionId, ctxModel.getAnalysisSituation(), ctxModel.getLocale().getLanguage()));
    }
}
