package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.ServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.*;

/**
 * Service model that implements the basic methods.
 */
public abstract class AbstractServiceModel implements ServiceModel {

    private final String currentState;
    private final Locale locale;

    private final EngineAnalysisSituation analysisSituation;
    private final Event operation;

    private final SessionModel sessionModel;

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    protected AbstractServiceModel(String currentState, SessionModel sessionModel) {
        if (currentState == null || currentState.isBlank())
            throw new IllegalArgumentException("currentState must not be null or empty.");
        if (sessionModel == null)
            throw new IllegalArgumentException("sessionModel must not be null.");

        this.currentState = currentState;
        this.locale = sessionModel.getLocale();
        this.analysisSituation = sessionModel.getAnalysisSituation();
        this.operation = sessionModel.getOperation();
        this.sessionModel = sessionModel;
    }

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    protected AbstractServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel) {
        if (currentState == null || currentState.isBlank())
            throw new IllegalArgumentException("currentState must not be null or empty.");
        if (analysisSituation == null)
            throw new IllegalArgumentException("analysisSituation must not be null.");
        if (sessionModel == null)
            throw new IllegalArgumentException("sessionModel must not be null.");

        this.currentState = currentState;
        this.locale = Objects.requireNonNullElse(locale, Locale.getDefault());
        this.analysisSituation = analysisSituation;
        this.operation = operation;
        this.sessionModel = sessionModel;
    }

    @Override
    public String getCurrentState() {
        return currentState;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public EngineAnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    @Override
    public Event getOperation() {
        return operation;
    }

    @Override
    public Map<String, Object> getAdditionalData() {
        return Collections.unmodifiableMap(sessionModel.getAdditionalData());
    }

    @Override
    public boolean additionalDataContainsKey(String key) {
        return sessionModel.getAdditionalData().containsKey(key);
    }

    @Override
    public Object getAdditionalData(String key) {
        return sessionModel.getAdditionalData().get(key);
    }

    @Override
    public <T> T getAdditionalData(String key, Class<T> clazz) {
        Object value = sessionModel.getAdditionalData().get(key);
        if (value == null) return null;
        if (!clazz.isAssignableFrom(value.getClass())) return null;
        return clazz.cast(value);
    }

    @Override
    public void addAdditionalData(String key, Object value) {
        sessionModel.getAdditionalData().put(key, value);
    }

    @Override
    public void removeAdditionalData(String key) {
        sessionModel.getAdditionalData().remove(key);
    }

    @Override
    public SessionModel getSessionModel() {
        return sessionModel;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractServiceModel.class.getSimpleName() + "[", "]")
                .add("currentState='" + currentState + "'")
                .add("locale=" + locale)
                .add("analysisSituation=" + analysisSituation)
                .add("operation=" + operation)
                .add("sessionModel=" + sessionModel)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractServiceModel that = (AbstractServiceModel) o;
        return Objects.equals(currentState, that.currentState) &&
                Objects.equals(locale, that.locale) &&
                Objects.equals(analysisSituation, that.analysisSituation) &&
                operation == that.operation &&
                Objects.equals(sessionModel, that.sessionModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, locale, analysisSituation, operation, sessionModel);
    }
}
