package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.rules.interfaces.PatternServiceModel;
import at.jku.dke.ida.rules.models.DefaultPatternServiceModel;
import at.jku.dke.ida.rules.results.ConfidenceResult;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.rules.services.PatternService;
import at.jku.dke.ida.scxml.interceptors.DeterminePatternSelectionInterceptor;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Pattern;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

public class DeterminePatternSelection extends BaseAction {
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
        PatternServiceModel model = new DefaultPatternServiceModel(
                getCurrentState(),
                ctxModel
        );
        var interceptor = BeanUtil.getOptionalBean(DeterminePatternSelectionInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine pattern
        ConfidenceResult result;
        var values = new PatternService().executeRules(model);
        if (interceptor != null)
            result = interceptor.modifyResult(model, values);
        else
            result = values.stream().sorted()
                    .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT));

        // Check type of result and trigger event
        if (result == null) {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.INVALID_INPUT.getEventName(), TriggerEvent.SIGNAL_EVENT));
            return;
        }
        if (result instanceof EventConfidenceResult) {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(((EventConfidenceResult) result).getValue().getEventName(), TriggerEvent.SIGNAL_EVENT));
            return;
        }
        if (result.getValue() instanceof Pattern) {
            switch (((Pattern) result.getValue()).getDisplayableId()) {
                case Pattern.COMPARATIVE:
                    ComparativeAnalysisSituation comp = new ComparativeAnalysisSituation();

                    ctxModel.setAnalysisSituation(comp.getContextOfInterest());
                    ctxModel.setComparativeActiveAS(Pattern.SI);
                    ctxModel.getAdditionalData().put(Pattern.ADD_DATA_COMPARATIVE, comp);
                    break;
                case Pattern.NONCOMPARATIVE:
                    ctxModel.setAnalysisSituation(new NonComparativeAnalysisSituation());
                    break;
            }
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.DETERMINED.getEventName(), TriggerEvent.SIGNAL_EVENT));
        } else {
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.INVALID_INPUT.getEventName(), TriggerEvent.SIGNAL_EVENT));
        }
    }
}
