package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.ValueDisplayServiceModel;
import at.jku.dke.ida.shared.display.Display;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DisplayValues} action.
 */
public interface DisplayValuesInterceptor extends Interceptor<ValueDisplayServiceModel, Display, Display> {
    @Override
    default Display modifyResult(Display result) {
        return result;
    }
}
