package at.jku.dke.inga.scxml.interceptors;

import at.jku.dke.inga.rules.models.OperationDisplayServiceModel;
import at.jku.dke.inga.shared.operations.Operation;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.inga.scxml.actions.DisplayOperations} action.
 */
public interface DisplayOperationsInterceptor extends Interceptor<OperationDisplayServiceModel, Collection<Operation>, Collection<Operation>> {
    @Override
    default Collection<Operation> modifyResult(Collection<Operation> result) {
        return result;
    }
}
