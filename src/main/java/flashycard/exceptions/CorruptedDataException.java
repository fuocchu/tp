package flashycard.exceptions;

/**
 * Thrown when the application encounters data in a format it cannot read or
 * process.
 * This typically happens if the storage file has been manually altered or
 * damaged.
 */
public class CorruptedDataException extends Exception {

    /**
     * Creates a new exception with a message describing the data error.
     *
     * @param message A description of what went wrong during data parsing.
     */
    public CorruptedDataException(String message) {
        super(message);
    }
}