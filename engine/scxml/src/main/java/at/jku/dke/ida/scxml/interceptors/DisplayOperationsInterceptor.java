package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.OperationDisplayServiceModel;
import at.jku.dke.ida.shared.operations.Operation;

import java.util.Collection;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DisplayOperations} action.
 */
public interface DisplayOperationsInterceptor extends Interceptor<OperationDisplayServiceModel, Collection<Operation>, Collection<Operation>> {
    @Override
    default Collection<Operation> modifyResult(OperationDisplayServiceModel model, Collection<Operation> result) {
        return result;
    }
}
