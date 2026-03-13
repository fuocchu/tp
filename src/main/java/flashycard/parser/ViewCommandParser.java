package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.Command;
import flashycard.command.DummyCommand;
import flashycard.exceptions.InvalidArgumentException;

public class ViewCommandParser extends CommandParser {
    public ViewCommandParser() {
        super("view", "(?<id>.+?)");
    }

    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matches = this.match(fullCommand);

        try {
            String idStr = matches.group("id").trim();
            int id = Integer.parseInt(idStr);
            return new DummyCommand(id); // TODO: return the correct class

        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid ID given: ID must be a number");
        }
    }
}
