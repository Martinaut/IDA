package at.jku.dke.ida.rules.interfaces;

import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Locale;
import java.util.Map;

/**
 * Interface all service models have to implement.
 * The interface provides basic methods needed in the Drools rules services.
 */
public interface ServiceModel {

    /**
     * Gets the current state of the state machine.
     *
     * @return the current state
     */
    String getCurrentState();

    /**
     * Gets the display locale.
     *
     * @return the locale
     */
    Locale getLocale();

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    EngineAnalysisSituation getAnalysisSituation();

    /**
     * Returns the display language.
     *
     * @return the language
     */
    default String getLanguage() {
        return getLocale().getLanguage();
    }

    /**
     * Gets the operation the user wants to perform.
     *
     * @return the operation
     */
    Event getOperation();


    /**
     * Gets the additional data (unmodifiable).
     *
     * @return the additional data
     */
    Map<String, Object> getAdditionalData();

    /**
     * Returns {@code true} if the additional data contains a mapping for the specified key.
     *
     * @param key The key whose presence in the additional data is to be tested.
     * @return {@code true} If the additional data contains a mapping for the specified key.
     */
    boolean additionalDataContainsKey(String key);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value to which the specified key is mapped,
     * or {@code null} if the additional data contains no mapping for the key.
     */
    Object getAdditionalData(String key);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param <T>   The type of the data to return. Tries to cast the value to this type.
     * @param key   The key whose associated value is to be returned.
     * @param clazz The type of the return type.
     * @return The value to which the specified key is mapped,
     * or {@code null} if the additional data contains no mapping for the key or if the value is not assignable to the specified class.
     */
    <T> T getAdditionalData(String key, Class<T> clazz);

    /**
     * Associates the specified value with the specified key in the additional data map.
     * If the additional data previously contained a mapping for the key, the old value is replaced by the specified value.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    void addAdditionalData(String key, Object value);

    /**
     * Removes the mapping for a key from the additional data if it is present.
     *
     * @param key The key whose mapping is to be removed from the additional data.
     */
    void removeAdditionalData(String key);

    /**
     * Gets the session model.
     *
     * @return the session model
     */
    SessionModel getSessionModel();
}
