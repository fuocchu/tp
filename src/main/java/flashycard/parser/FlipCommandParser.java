package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.FlipCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input for the "flip" command.
 * It extracts the ID of the flashcard that the user wants to reveal the answer
 * for.
 */
public class FlipCommandParser extends CommandParser {

    /**
     * Initializes the parser with the "flip" keyword and a pattern to capture the
     * card ID.
     */
    public FlipCommandParser() {
        super("flip", "(?<id>.+?)");
    }

    /**
     * Extracts the ID from the command string and converts it to an integer.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new FlipCommand instance for the specified card.
     * @throws InvalidArgumentException If the ID is not a valid number.
     */
    @Override
    public FlipCommand parse(String fullCommand) throws InvalidArgumentException {
        Matcher matches = this.match(fullCommand);

        try {
            String idStr = matches.group("id").trim();
            int id = Integer.parseInt(idStr);
            return new FlipCommand(id);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid ID given: ID must be a number");
        }

    }

}
