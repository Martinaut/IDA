package at.jku.dke.inga.data.repositories;

public class QueryException extends Exception {
    /**
     * Instantiates a new instance of class {@linkplain QueryException} with the specified detail message.
     *
     * @param message the detail message
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * Instantiates a new instance of class {@linkplain QueryException} with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
