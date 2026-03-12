package flashycard.exceptions;

public class MissingArgumentException extends InvalidCommandException {
    public MissingArgumentException(String message) {
        super(message);
    }
}