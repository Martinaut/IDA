package at.jku.dke.ida.data.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A word group represents a group of words belonging together.
 */
public class WordGroup {

    private final String text;
    private final String expression;
    private final Collection<Integer> indices;

    /**
     * Instantiates a new instance of class {@link WordGroup}.
     *
     * @param text the text
     */
    public WordGroup(String text) {
        this.text = text;
        this.expression = null;
        this.indices = Collections.emptyList();
    }

    /**
     * Instantiates a new instance of class {@link WordGroup}.
     *
     * @param text       The text.
     * @param expression The expression used to find this word group.
     * @param indices    The indices in the sentence for each word.
     */
    public WordGroup(String text, String expression, Collection<Integer> indices) {
        this.text = text;
        this.expression = expression;
        this.indices = Collections.unmodifiableCollection(indices);
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

    /**
     * Gets the indices.
     *
     * @return The positions of the words in the senctence.
     */
    public Collection<Integer> getIndices() {
        return indices;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WordGroup.class.getSimpleName() + "[", "]")
                .add("text='" + text + "'")
                .add("expression='" + expression + "'")
                .add("indices='" + indices + "'")
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
