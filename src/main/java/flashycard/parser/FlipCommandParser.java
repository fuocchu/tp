package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.FlipCommand;
import flashycard.exceptions.InvalidArgumentException;

public class FlipCommandParser extends CommandParser {
    public FlipCommandParser() {
        super("flip", "(?<id>.+?)");
    }

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
