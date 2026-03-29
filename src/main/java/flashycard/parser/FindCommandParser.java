package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.FindCommand;
import flashycard.exceptions.InvalidArgumentException;

public class FindCommandParser extends CommandParser {
    private static final String FIND_REGEX = "(?:(?<scope>[qa])/)?(?<keyword>.+)";

    public FindCommandParser() {
        super("find", FIND_REGEX);
    }

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
