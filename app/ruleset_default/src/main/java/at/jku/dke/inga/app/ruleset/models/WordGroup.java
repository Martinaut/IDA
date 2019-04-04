package at.jku.dke.inga.app.ruleset.models;

import java.util.Objects;
import java.util.StringJoiner;

public class WordGroup {

    private final String text;

    public WordGroup(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WordGroup.class.getSimpleName() + "[", "]")
                .add("text='" + text + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordGroup wordGroup = (WordGroup) o;
        return Objects.equals(text, wordGroup.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
