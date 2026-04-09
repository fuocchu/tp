package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.TagCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "tag" command.
 * It identifies the numeric ID of a card and the tag string to be associated
 * with it.
 */
public class TagCommandParser extends CommandParser {

    /**
     * Initializes the parser with the "tag" keyword and a regex pattern
     * requiring a numeric ID and a tag prefixed with 't/'.
     */
    public TagCommandParser() {
        super("tag", "(?<id>\\d+)\\s+t/(?<tag>.+)\\s*");
    }

    /**
     * Extracts the ID and tag from the command string.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new TagCommand instance containing the parsed ID and tag.
     * @throws InvalidArgumentException If the ID is not a valid number or the
     *                                  format is incorrect.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matches = this.match(fullCommand);
        try {
            int id = Integer.parseInt(matches.group("id"));
            String tag = matches.group("tag").trim();
            return new TagCommand(id, tag);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("ID must be a valid number.");
        }
    }
}
