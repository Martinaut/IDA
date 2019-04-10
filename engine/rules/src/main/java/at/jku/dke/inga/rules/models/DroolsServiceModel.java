package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Base-class for all rule-service data models containing data needed for all actions.
 */
public abstract class DroolsServiceModel {

    private final String currentState;
    private final Locale locale;
    private final AnalysisSituation analysisSituation;
    private final String operation;
    private final Map<String, Object> additionalData;

    /**
     * Instantiates a new instance of class {@link DroolsServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param additionalData    Additional data.
     * @throws IllegalArgumentException If the {@code currentState} or {@code analysisSituation} is {@code null} or empty.
     */
    protected DroolsServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData) {
        if (StringUtils.isBlank(currentState))
            throw new IllegalArgumentException("currentState must not be null or empty.");
        if (analysisSituation == null)
            throw new IllegalArgumentException("analysisSituation must not be null.");

        this.currentState = currentState;
        this.locale = locale;
        this.analysisSituation = analysisSituation;
        this.operation = operation;
        this.additionalData = Objects.requireNonNullElseGet(additionalData, HashMap::new);
    }

    /**
     * Gets the current state of the state machine.
     *
     * @return the current state
     */
    public String getCurrentState() {
        return currentState;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    /**
     * Gets the display locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns the display language.
     *
     * @return the language
     */
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     * Gets the operation the user wants to perform.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    // region --- Additional Data ---

    /**
     * Returns {@code true} if the additional data contains a mapping for the specified key.
     *
     * @param key The key whose presence in the additional data is to be tested.
     * @return {@code true} If the additional data contains a mapping for the specified key.
     */
    public boolean additionalDataContainsKey(String key) {
        return additionalData.containsKey(key);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value to which the specified key is mapped,
     * or {@code null} if the additional data contains no mapping for the key.
     */
    public Object getAdditionalData(String key) {
        return additionalData.get(key);
    }

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
    public <T> T getAdditionalData(String key, Class<T> clazz) {
        Object value = additionalData.get(key);
        if (value == null) return null;
        if (!clazz.isAssignableFrom(value.getClass())) return null;
        return (T) value;
    }

    /**
     * Associates the specified value with the specified key in the additional data map.
     * If the additional data previously contained a mapping for the key, the old value is replaced by the specified value.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    public void addAdditionalData(String key, Object value) {
        additionalData.put(key, value);
    }

    /**
     * Removes the mapping for a key from the additional data if it is present.
     *
     * @param key The key whose mapping is to be removed from the additional data.
     */
    public void removeAdditionalData(String key) {
        additionalData.remove(key);
    }
    // endregion

    @Override
    public String toString() {
        return new StringJoiner(", ", DroolsServiceModel.class.getSimpleName() + "[", "]")
                .add("currentState='" + currentState + "'")
                .add("locale=" + locale)
                .add("analysisSituation=" + analysisSituation)
                .add("operation='" + operation + "'")
                .add("additionalData=" + additionalData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroolsServiceModel that = (DroolsServiceModel) o;
        return Objects.equals(currentState, that.currentState) &&
                Objects.equals(locale, that.locale) &&
                Objects.equals(analysisSituation, that.analysisSituation) &&
                Objects.equals(operation, that.operation) &&
                Objects.equals(additionalData, that.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, locale, analysisSituation, operation, additionalData);
    }
}
