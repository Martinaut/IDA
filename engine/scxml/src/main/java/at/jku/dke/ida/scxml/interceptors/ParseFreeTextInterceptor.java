package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.shared.session.SessionModel;

/**
 * Interceptor for performing the parsing in the {@link at.jku.dke.ida.scxml.actions.ParseFreeText} action.
 */
public interface ParseFreeTextInterceptor extends Interceptor<SessionModel, Void, Void> {
    /**
     * This method will never be called.
     */
    @Override
    default SessionModel modifyModel(SessionModel sessionModel) {
        return sessionModel;
    }

    /**
     * Use this method to parse the user input and set the values appropriately.
     *
     * @param sessionModel The basic model.
     * @param result       Nothing
     * @return Nothing
     */
    @Override
    Void modifyResult(SessionModel sessionModel, Void result);
}
