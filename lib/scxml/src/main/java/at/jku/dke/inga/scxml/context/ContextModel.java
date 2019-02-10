package at.jku.dke.inga.scxml.context;

import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;

import java.util.Locale;

public class ContextModel {

    private String userInput;
    private Integer page;
    private String operation;
    private AnalysisSituation analysisSituation;
    private Locale locale;

    public ContextModel() {
        this.userInput = null;
        this.page = null;
        this.operation = EventNames.NAVIGATE_CUBE_SELECT;
        this.locale = Locale.getDefault();
        this.analysisSituation = new NonComparativeAnalysisSituation();
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    public void setAnalysisSituation(AnalysisSituation analysisSituation) {
        this.analysisSituation = analysisSituation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
