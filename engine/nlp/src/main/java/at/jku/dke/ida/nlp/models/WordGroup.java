package at.jku.dke.ida.nlp.models;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A word group represents a group of words belonging together.
 */
public class WordGroup {

    private final String text;
    private final String expression;

    /**
     * Instantiates a new instance of class {@link WordGroup}.
     *
     * @param text the text
     */
    public WordGroup(String text) {
        this.text = text;
        this.expression = null;
    }

    /**
     * Instantiates a new instance of class {@link WordGroup}.
     *
     * @param text       The text.
     * @param expression The expression used to find this word group.
     */
    public WordGroup(String text, String expression) {
        this.text = text;
        this.expression = expression;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WordGroup.class.getSimpleName() + "[", "]")
                .add("text='" + text + "'")
                .add("expression='" + expression + "'")
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
