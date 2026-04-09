package flashycard.exceptions;

/**
 * Thrown when the application receives a command that it does not recognize
 * or cannot process.
 */
public class InvalidCommandException extends Exception {

    /**
     * Creates a new exception with a specific error message.
     *
     * @param message A description of why the command is considered invalid.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}