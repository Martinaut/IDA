package at.jku.dke.inga.web.models;

import at.jku.dke.inga.shared.display.Display;

public class DisplayResult {
    private String type;
    private Display display;

    public DisplayResult(Display display) {
        this.type = display.getClass().getSimpleName();
        this.display = display;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }
}
