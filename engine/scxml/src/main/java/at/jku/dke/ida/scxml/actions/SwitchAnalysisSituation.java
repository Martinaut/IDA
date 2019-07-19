package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.repositories.LevelRepository;
import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.session.Session;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.operations.Pattern;
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
        ComparativeAnalysisSituation as = ctxModel.getAdditionalData(Pattern.ADD_DATA_COMPARATIVE, ComparativeAnalysisSituation.class);
        try {
            switch (ctxModel.getOperation()) {
                case SWITCH_SC:
                    if (!as.getContextOfComparison().isCubeDefined()) {
                        var baseLevels = BeanUtil.getBean(LevelRepository.class).getTopLevelLabelsByLangAndCube(ctxModel.getLocale().getLanguage(), as.getContextOfInterest().getCube());
                        for (DimensionLabel lvl : baseLevels) {
                            var dq = new DimensionQualification(lvl.getDimensionUri());
                            dq.setGranularityLevel(lvl.getUri());
                            as.getContextOfComparison().addDimensionQualification(dq);
                        }
                        as.getContextOfComparison().setCube(as.getContextOfInterest().getCube());
                    }
                    ctxModel.setAnalysisSituation(as.getContextOfComparison());
                    setCubeSet(ctxModel, as.getContextOfComparison().isCubeDefined());
                    ctxModel.setComparativeActiveAS(Pattern.SC);
                    break;
                case SWITCH_SI:
                    ctxModel.setAnalysisSituation(as.getContextOfInterest());
                    setCubeSet(ctxModel, as.getContextOfInterest().isCubeDefined());
                    ctxModel.setComparativeActiveAS(Pattern.SI);
                    break;
                case SWITCH_COMP:
                    ctxModel.setAnalysisSituation(as);
                    break;
                default:
                    ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.INVALID_INPUT.getEventName(), TriggerEvent.SIGNAL_EVENT));
                    return;
            }

            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.SWITCH.getEventName(), TriggerEvent.SIGNAL_EVENT));
        } catch (QueryException ex) {
            logger.fatal("Cannot set dimension qualifications for set of comparison as there occurred an error while querying the levels.", ex);
            ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.INVALID_INPUT.getEventName(), TriggerEvent.SIGNAL_EVENT));
        }
    }

    private void setCubeSet(SessionContextModel ctxModel, boolean set) {
        Session session = SessionManager.getInstance().getSession(ctxModel.getSessionId());
        if (session != null) {
            session.setCubeSetFlag(true);
            if (ctxModel.getAnalysisSituationListener() != null)
                ctxModel.getAnalysisSituationListener().changed(new AnalysisSituationEvent(this,
                        ctxModel.getSessionId(),
                        ctxModel.getAnalysisSituation(),
                        ctxModel.getLocale().getLanguage()));
        }
    }
}
