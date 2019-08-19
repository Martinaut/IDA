package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.interfaces.SwitchAnalysisSituationServiceModel;
import at.jku.dke.ida.rules.models.DefaultSwitchAnalysisSituationServiceModel;
import at.jku.dke.ida.rules.results.AnalysisSituationConfidenceResult;
import at.jku.dke.ida.rules.services.SwitchAnalysisSituationService;
import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.interceptors.SwitchAnalysisSituationInterceptor;
import at.jku.dke.ida.scxml.session.Session;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

/**
 * Switches the analysis situation.
 */
public class SwitchAnalysisSituation extends BaseAction {

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
        // Get data
        SwitchAnalysisSituationServiceModel model = new DefaultSwitchAnalysisSituationServiceModel(
                getCurrentState(),
                ctxModel
        );
        var interceptor = BeanUtil.getOptionalBean(SwitchAnalysisSituationInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine data
        AnalysisSituationConfidenceResult asResult = new SwitchAnalysisSituationService().executeRules(model);
        EngineAnalysisSituation as;
        if (interceptor == null)
            as = asResult.getValue();
        else
            as = interceptor.modifyResult(model, asResult);

        // Set values and trigger event
        if (as == null)
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.INVALID_INPUT.getEventName(), TriggerEvent.SIGNAL_EVENT));
        else {
            var oldAS = ctxModel.getAnalysisSituation().isCubeDefined();
            ctxModel.setAnalysisSituation(as);
            setCubeSetFlagAndFireEvent(ctxModel, oldAS);
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.SWITCH.getEventName(), TriggerEvent.SIGNAL_EVENT));
        }
    }

    private void setCubeSetFlagAndFireEvent(SessionContextModel ctxModel, boolean set) {
        Session session = SessionManager.getInstance().getSession(ctxModel.getSessionId());
        if (session != null) {
            session.setCubeSetFlag(set);
            if (ctxModel.getAnalysisSituationListener() != null)
                ctxModel.getAnalysisSituationListener().changed(new AnalysisSituationEvent(this,
                        ctxModel.getSessionId(),
                        ctxModel.getAnalysisSituation(),
                        ctxModel.getLocale().getLanguage()));
        }
    }
}
