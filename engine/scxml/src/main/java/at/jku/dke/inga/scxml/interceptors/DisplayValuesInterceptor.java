package at.jku.dke.inga.scxml.interceptors;

import at.jku.dke.inga.rules.models.ValueDisplayServiceModel;
import at.jku.dke.inga.shared.display.Display;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.inga.scxml.actions.DisplayValues} action.
 */
public interface DisplayValuesInterceptor extends Interceptor<ValueDisplayServiceModel, Display, Display> {
    @Override
    default Display modifyResult(Display result) {
        return result;
    }
}
