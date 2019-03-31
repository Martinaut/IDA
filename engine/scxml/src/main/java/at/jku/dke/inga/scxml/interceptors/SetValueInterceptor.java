package at.jku.dke.inga.scxml.interceptors;

import at.jku.dke.inga.rules.models.SetValueServiceModel;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.inga.scxml.actions.DetermineValue} action.
 */
public interface SetValueInterceptor extends Interceptor<SetValueServiceModel, Void, Void> {
    @Override
    default Void modifyResult(Void result) {
        return null;
    }
}
