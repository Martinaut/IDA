package at.jku.dke.ida.data;

/**
 * This exception will be thrown when an error has occurred while executing a query.
 */
public class QueryException extends Exception {

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by
     *                the {@link #getMessage()} method.
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
