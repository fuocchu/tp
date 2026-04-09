package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.SaveCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "save" command.
 * It handles the grouping of flashcards into named test sets by capturing
 * either a specific card ID or the "all" keyword, along with the target set
 * name.
 */
public class SaveCommandParser extends CommandParser {

    private static final String SAVE_REGEX = "(?<target>all|\\d+)\\s+s/(?<setName>.+)";

    /**
     * Initializes the parser with the "save" keyword and a regex to capture
     * the target cards and the set name prefixed by 's/'.
     */
    public SaveCommandParser() {
        super("save", SAVE_REGEX);
    }

    /**
     * Extracts the target (ID or "all") and the set name from the command string.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new SaveCommand instance configured with the target and set name.
     * @throws InvalidArgumentException If the set name is empty or the format is
     *                                  invalid.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matcher = this.match(fullCommand);

        String target = matcher.group("target").trim();
        String setName = matcher.group("setName").trim();

        if (setName.isEmpty()) {
            throw new InvalidArgumentException("Set name cannot be empty. Usage: save [ID/all] s/SET_NAME");
        }

        return new SaveCommand(target, setName);
    }
}
