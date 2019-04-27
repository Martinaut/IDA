package at.jku.dke.ida.rules.interfaces;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.SetValueService}.
 */
public interface SetValueServiceModel extends ServiceModel {

    /**
     * Gets the value.
     *
     * @return the value
     */
    Object getValue();

    /**
     * Returns the value, or {@code null} if the value is not of the requested type.
     *
     * @param <T>   The type of the data to return. Tries to cast the value to this type.
     * @param clazz The type of the return type.
     * @return The casted value, or {@code null} if the value is not assignable to the specified class.
     */
    <T> T getValue(Class<T> clazz);
    
}
