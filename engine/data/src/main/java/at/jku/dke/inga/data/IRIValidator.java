package at.jku.dke.inga.data;

import org.eclipse.rdf4j.common.net.ParsedIRI;

/**
 * A helper class to validate IRIs.
 */
public final class IRIValidator {
    /**
     * Prevents creation of instances of this class.
     */
    private IRIValidator() {
    }

    /**
     * Returns whether the given string is an absolute IRI or not.
     *
     * @param iriString The IRI string to validate.
     * @return {@code true} if the IRI is valid and absolute; {@code false} otherwise.
     */
    public static boolean isValidAbsoluteIRI(String iriString) {
        if (iriString == null) return false;
        try {
            var parsed = ParsedIRI.create(iriString);
            if (parsed == null) return false;
            return parsed.isAbsolute();
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
