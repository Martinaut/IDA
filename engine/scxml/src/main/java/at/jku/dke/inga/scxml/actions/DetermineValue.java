package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.rules.models.SetValueServiceModel;
import at.jku.dke.inga.rules.models.ValueServiceModel;
import at.jku.dke.inga.rules.results.ConfidenceResult;
import at.jku.dke.inga.rules.results.StringConfidenceResult;
import at.jku.dke.inga.rules.services.SetValueService;
import at.jku.dke.inga.rules.services.ValueService;
import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.interceptors.DetermineValueInterceptor;
import at.jku.dke.inga.scxml.interceptors.SetValueInterceptor;
import at.jku.dke.inga.scxml.session.SessionContextModel;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

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
        if (result instanceof StringConfidenceResult) {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(((StringConfidenceResult) result).getValue(), TriggerEvent.SIGNAL_EVENT));
        } else {
            setValue(getSessionId(ctx), ctxModel, result);
            ctxModel.setOperation(null);
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(EventNames.DETERMINED, TriggerEvent.SIGNAL_EVENT));
        }
    }

    private ConfidenceResult determineValue(SessionContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValueServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                ctxModel.getUserInput(),
                ctxModel.getDisplayData()
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
                    .findFirst().orElse(new StringConfidenceResult(EventNames.INVALID_INPUT));

        return result;
    }

    private void setValue(String sessionId, SessionContextModel ctxModel, ConfidenceResult value) throws ModelException {
        // Get data
        SetValueServiceModel model = new SetValueServiceModel(
                getCurrentState(),
                ctxModel.getLocale(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getOperation(),
                ctxModel.getAdditionalData(),
                value.getValue()
        );
        var interceptor = BeanUtil.getOptionalBean(SetValueInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Execute
        new SetValueService().executeRules(model);

        // Notify
        if (ctxModel.getAnalysisSituationListener() != null && ctxModel.getAnalysisSituation().isCubeDefined())
            ctxModel.getAnalysisSituationListener().changed(new AnalysisSituationEvent(this, sessionId, ctxModel.getAnalysisSituation(), ctxModel.getLocale().getLanguage()));
    }
}
