package at.jku.dke.ida.web.models;

/**
 * A message containing settings to start a new dialog.
 */
public class StartDialogMessage {

    private String locale;

    /**
     * Instantiates a new instance of class {@linkplain StartDialogMessage}.
     */
    public StartDialogMessage() {
    }

    /**
     * Instantiates a new instance of class {@linkplain StartDialogMessage}.
     *
     * @param locale          The locale.
     */
    public StartDialogMessage(String locale) {
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
