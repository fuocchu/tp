package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.ListCommand;
import flashycard.exceptions.InvalidArgumentException;

public class ListCommandParser extends CommandParser {
    private static final String LIST_REGEX = "(?:\\s+s/(?<setName>.+))?";

    public ListCommandParser() {
        super("list", LIST_REGEX);
    }

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
