package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.interfaces.PatternServiceModel;
import at.jku.dke.ida.rules.models.DefaultPatternServiceModel;
import at.jku.dke.ida.rules.results.ConfidenceResult;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.rules.results.PatternConfidenceResult;
import at.jku.dke.ida.rules.services.PatternService;
import at.jku.dke.ida.scxml.interceptors.DeterminePatternInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Pattern;
import at.jku.dke.ida.shared.operations.PatternPart;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

public class DeterminePattern extends BaseAction {
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
        PatternServiceModel model = new DefaultPatternServiceModel(getCurrentState(), ctxModel);
        var interceptor = BeanUtil.getOptionalBean(DeterminePatternInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine pattern
        ConfidenceResult result;
        var values = new PatternService().executeRules(model);
        if (interceptor != null)
            result = interceptor.modifyResult(model, values);
        else
            result = values.stream().sorted()
                    .findFirst().orElse(new EventConfidenceResult(Event.DETERMINED));

        // Check type of result and trigger event
        if (result instanceof PatternConfidenceResult) {
            switch (((PatternConfidenceResult) result).getValue()) {
                case COMPARATIVE:
                    ComparativeAnalysisSituation comp = new ComparativeAnalysisSituation();

                    ctxModel.setAnalysisSituationWithoutEvent(comp);
                    ctxModel.setComparativeActiveAS(PatternPart.PARENT);
                    ctxModel.getAdditionalData().put(Pattern.ADDITIONAL_DATA_COMPARATIVE, comp);

                    var session = SessionManager.getInstance().getSession(ctxModel.getSessionId());
                    if (session != null) session.setCubeSetFlag(true);
                    break;
                case NON_COMPARATIVE:
                    ctxModel.setAnalysisSituationWithoutEvent(new NonComparativeAnalysisSituation());
                    break;
            }
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.DETERMINED.getEventName(), TriggerEvent.SIGNAL_EVENT));
        } else {
            ctxModel.setAnalysisSituationWithoutEvent(new NonComparativeAnalysisSituation());
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.DETERMINED.getEventName(), TriggerEvent.SIGNAL_EVENT));
        }
    }
}
