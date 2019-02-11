package at.jku.dke.inga.scxml.context;

import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;

import java.util.Locale;

/**
 * The model of a context.
 * A context holds all relevant information for a specific session.
 */
public class ContextModel {

    private String userInput;
    private Integer page;
    private String operation;
    private AnalysisSituation analysisSituation;
    private Locale locale;
    private Display displayData;

    /**
     * Instantiates a new instance of class {@link ContextModel}.
     */
    public ContextModel() {
        this.userInput = null;
        this.page = null;
        this.displayData = null;
        this.operation = EventNames.NAVIGATE_CUBE_SELECT;
        this.locale = Locale.getDefault();
        this.analysisSituation = new NonComparativeAnalysisSituation();
    }

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * Sets the user input.
     *
     * @param userInput the user input
     */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    /**
     * Gets the page.
     *
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Sets the page.
     *
     * @param page the page
     */
    public void setPage(Integer page) {
        this.page = page;
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
     * Sets the analysis situation.
     *
     * @param analysisSituation the analysis situation
     */
    public void setAnalysisSituation(AnalysisSituation analysisSituation) {
        this.analysisSituation = analysisSituation;
    }

    /**
     * Gets the operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the operation.
     *
     * @param operation the operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the locale.
     *
     * @param locale the locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Gets the display data.
     *
     * @return the display data
     */
    public Display getDisplayData() {
        return displayData;
    }

    /**
     * Sets the display data.
     *
     * @param displayData the display data
     */
    public void setDisplayData(Display displayData) {
        this.displayData = displayData;
    }
}
