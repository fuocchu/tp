package flashycard.exceptions;

/**
 * Thrown when a command is recognized but the provided arguments are incorrect
 * or missing.
 */
public class InvalidArgumentException extends InvalidCommandException {

    /**
     * Creates a new exception with a message explaining the argument error.
     *
     * @param message Details about which argument is invalid and why.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}