package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.SetValueServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Locale;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.SetValueService}.
 */
public class DefaultSetValueServiceModel extends AbstractServiceModel implements SetValueServiceModel {

    private final Object value;

    /**
     * Instantiates a new instance of class {@link DefaultSetValueServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @param value        The value to set.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultSetValueServiceModel(String currentState, SessionModel sessionModel, Object value) {
        super(currentState, sessionModel);
        if (value == null)
            throw new IllegalArgumentException("value must not be null");
        this.value = value;
    }

    /**
     * Instantiates a new instance of class {@link DefaultSetValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param value             The value to set.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code operation}).
     */
    public DefaultSetValueServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation,
                                       SessionModel sessionModel, Object value) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

        if (operation == null)
            throw new IllegalArgumentException("operation must not be null");
        if (value == null)
            throw new IllegalArgumentException("value must not be null");

        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T getValue(Class<T> clazz) {
        if (value == null) return null;
        if (!clazz.isAssignableFrom(value.getClass())) return null;
        return clazz.cast(value);
    }
}
