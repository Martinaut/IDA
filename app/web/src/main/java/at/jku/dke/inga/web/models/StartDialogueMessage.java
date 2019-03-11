package at.jku.dke.inga.web.models;

/**
 * A message containing settings to start a new dialogue state machine.
 */
public class StartDialogueMessage {

    private String locale;

    /**
     * Instantiates a new instance of class {@linkplain StartDialogueMessage}.
     */
    public StartDialogueMessage() {
    }

    /**
     * Instantiates a new instance of class {@linkplain StartDialogueMessage}.
     *
     * @param locale The locale.
     */
    public StartDialogueMessage(String locale) {
        this.locale = locale;
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
}
