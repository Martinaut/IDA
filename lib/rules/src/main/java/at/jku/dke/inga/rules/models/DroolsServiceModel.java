package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Base-class for all rule-service data models containing data needed for all actions.
 */
public abstract class DroolsServiceModel {

    private final String currentState;
    private final AnalysisSituation analysisSituation;
    private final Locale locale;

    /**
     * Instantiates a new instance of class {@link DroolsServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    protected DroolsServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale) {
        if (StringUtils.isBlank(currentState))
            throw new IllegalArgumentException("currentState must not be null or empty.");
        if (analysisSituation == null)
            throw new IllegalArgumentException("analysisSituation must not be null.");

        this.currentState = currentState;
        this.analysisSituation = analysisSituation;
        if (locale == null)
            this.locale = Locale.getDefault();
        else
            this.locale = locale;
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
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DroolsServiceModel)) return false;
        DroolsServiceModel droolsServiceModel = (DroolsServiceModel) o;
        return Objects.equals(currentState, droolsServiceModel.currentState) &&
                Objects.equals(analysisSituation, droolsServiceModel.analysisSituation) &&
                Objects.equals(locale, droolsServiceModel.locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(currentState, analysisSituation, locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", DroolsServiceModel.class.getSimpleName() + "[", "]")
                .add("currentState='" + currentState + "'")
                .add("analysisSituation=" + analysisSituation)
                .add("locale=" + locale)
                .toString();
    }
}
