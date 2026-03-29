package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.TagCommand;
import flashycard.exceptions.InvalidArgumentException;

public class TagCommandParser extends CommandParser {
    public TagCommandParser() {
        super("tag", "(?<id>\\d+)\\s+t/(?<tag>.+)\\s*");
    }

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
