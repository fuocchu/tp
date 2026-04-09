package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.ListCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "list" command.
 * It can list all available flashcards or filter them by a specific test set
 * name.
 */
public class ListCommandParser extends CommandParser {
    private static final String LIST_REGEX = "(?:\\s+s/(?<setName>.+))?";

    /**
     * Initializes the parser with the "list" keyword and an optional
     * parameter for a set name prefixed by 's/'.
     */
    public ListCommandParser() {
        super("list", LIST_REGEX);
    }

    /**
     * Extracts the optional set name from the command string.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new ListCommand instance, potentially scoped to a specific set.
     * @throws InvalidArgumentException If the command format is incorrect.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matcher = this.match(fullCommand);
        String setName = matcher.group("setName");

        if (setName != null) {
            setName = setName.trim();
        }

        return new ListCommand(setName);
    }
}
