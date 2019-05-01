package at.jku.dke.ida.scxml.interceptors;

/**
 * An interceptor can be used to modify the model and the result before using the in the actions.
 *
 * @param <TModel>        The type of the model.
 * @param <TResultInput>  The type of the delivered by the action result.
 * @param <TResultOutput> The type of the result that should be returned.
 */
public interface Interceptor<TModel, TResultInput, TResultOutput> {
    /**
     * Use this method to modify the model before executing the rules.
     * <p>
     * The default implementation just returns the model.
     *
     * @param model The basic model.
     * @return The modified model (may also be a new instance or an instance of a subclass).
     */
    default TModel modifyModel(TModel model) {
        return model;
    }

    /**
     * Use this method to modify the result before using it.
     *
     * @param model The basic model.
     * @param result The basic result.
     * @return The modified result (may also be a new instance or an instance of a subclass).
     */
    TResultOutput modifyResult(TModel model, TResultInput result);
}
