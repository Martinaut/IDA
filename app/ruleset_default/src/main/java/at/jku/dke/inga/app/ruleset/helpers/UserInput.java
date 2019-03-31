package at.jku.dke.inga.app.ruleset.helpers;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Contains some helper methods used in the rules for parsing the user input.
 */
public final class UserInput {
    /**
     * Prevents creation of instances of this class.
     */
    private UserInput() {
    }

    /**
     * Checks whether the given String is a parsable number.
     *
     * <p>Parsable numbers include those Strings understood by {@link Integer#parseInt(String)},
     * {@link Long#parseLong(String)}, {@link Float#parseFloat(String)} or
     * {@link Double#parseDouble(String)}.</p>
     *
     * <p>{@code Null} and empty String will return <code>false</code>.</p>
     *
     * @param input the String to check.
     * @return {@code true} if the string is a parsable number.
     */
    public static boolean isNumber(String input) {
        return NumberUtils.isParsable(input);
    }

    /**
     * Parses the string argument as a signed decimal integer.
     *
     * @param input a {@code String} containing the {@code int} representation to be parsed
     * @return the integer value represented by the argument in decimal.
     * @throws NumberFormatException if the string does not contain a parsable integer.
     */
    public static int toInteger(String input) {
        return Integer.parseInt(input);
    }

    /**
     * Returns whether the input has the format: NUMBER COMMA NUMBER (e.g. "1,2").
     *
     * @param input The input.
     * @return {@code true} if the input has the correct format; {@code false} otherwise.
     */
    public static boolean isTwoNumberSelection(String input) {
        if (input == null) return false;

        String[] split = input.split(",");
        if (split.length != 2) return false;

        return NumberUtils.isParsable(split[0]) && NumberUtils.isParsable(split[1]);
    }

    /**
     * Parses the string argument as a pair of numbers.
     *
     * @param input The user input in the format: NUMBER COMMA NUMBER
     * @return the integer representation of the input
     * @throws NumberFormatException if the string does not contain two parsable integers separated by a comma.
     */
    public static Pair<Integer, Integer> toTwoNumberSelectionInteger(String input) {
        if (!isTwoNumberSelection(input)) throw new NumberFormatException("No two number format.");

        String[] split = input.split(",");
        return new ImmutablePair<>(toInteger(split[0]), toInteger(split[1]));
    }
}
