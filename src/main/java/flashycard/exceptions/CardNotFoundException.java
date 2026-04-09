package flashycard.exceptions;

/**
 * Thrown when an operation attempts to access a flashcard that does not exist
 * in the knowledge base.
 */
public class CardNotFoundException extends Exception {

    /**
     * Creates a new exception with a specific error message.
     *
     * @param message The message explaining why the card could not be found.
     */
    public CardNotFoundException(String message) {
        super(message);
    }
}