package at.jku.dke.inga.scxml.exceptions;

/**
 * This exception will be thrown if a session already expired or does not exist.
 */
public class SessionExpiredException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SessionExpiredException(String message) {
        super(message);
    }
}
