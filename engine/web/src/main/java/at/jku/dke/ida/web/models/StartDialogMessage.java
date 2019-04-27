package at.jku.dke.ida.web.models;

/**
 * A message containing settings to start a new dialog.
 */
public class StartDialogMessage {

    private String locale;
    private String initialSentence;

    /**
     * Instantiates a new instance of class {@linkplain StartDialogMessage}.
     */
    public StartDialogMessage() {
    }

    /**
     * Instantiates a new instance of class {@linkplain StartDialogMessage}.
     *
     * @param locale          The locale.
     * @param initialSentence The initial sentence.
     */
    public StartDialogMessage(String locale, String initialSentence) {
        this.locale = locale;
        this.initialSentence = initialSentence;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the locale.
     *
     * @param locale the locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Gets the initial sentence.
     *
     * @return the initial sentence
     */
    public String getInitialSentence() {
        return initialSentence;
    }

    /**
     * Sets the initial sentence.
     *
     * @param initialSentence the initial sentence
     */
    public void setInitialSentence(String initialSentence) {
        this.initialSentence = initialSentence;
    }
}
