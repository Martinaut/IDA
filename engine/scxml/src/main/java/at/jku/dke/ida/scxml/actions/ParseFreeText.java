package at.jku.dke.ida.scxml.actions;

import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.interceptors.ParseFreeTextInterceptor;
import at.jku.dke.ida.scxml.session.Session;
import at.jku.dke.ida.scxml.session.SessionContextModel;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.model.ModelException;

/**
 * Parses the entered free text of the user.
 */
public class ParseFreeText extends BaseAction {

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
        var interceptor = BeanUtil.getOptionalBean(ParseFreeTextInterceptor.class);
        if (interceptor != null)
            interceptor.modifyResult(ctxModel, null);

        if (ctxModel.getAnalysisSituation().isCubeDefined()) {
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

        ctx.getInternalIOProcessor().addEvent(new TriggerEvent(Event.DETERMINED.getEventName(), TriggerEvent.SIGNAL_EVENT));
    }

}
