package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.FindCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "find" command.
 * It supports optional scoping to search within questions only (q/), answers
 * only (a/),
 * or both if no scope is provided.
 */
public class FindCommandParser extends CommandParser {
    private static final String FIND_REGEX = "(?:(?<scope>[qa])/)?(?<keyword>.+)";

    /**
     * Initializes the parser with the "find" keyword and the regex required
     * to capture the optional scope and the mandatory search keyword.
     */
    public FindCommandParser() {
        super("find", FIND_REGEX);
    }

    /**
     * Extracts the search scope and keyword from the input string.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new FindCommand instance configured with the parsed scope and
     *         keyword.
     * @throws InvalidArgumentException If the keyword is missing or the format is
     *                                  invalid.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matcher = this.match(fullCommand);
        String scope = matcher.group("scope");
        String keyword = matcher.group("keyword").trim();

        if (keyword.isEmpty()) {
            throw new InvalidArgumentException("Keyword cannot be empty.");
        }

        return new FindCommand(keyword, scope);
    }
}
