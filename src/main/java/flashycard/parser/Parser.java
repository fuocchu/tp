package flashycard.parser;

import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;
import flashycard.exceptions.InvalidCommandException;

public class Parser {
    private static CommandParser[] parsers = new CommandParser[] {
        new AddCommandParser(),
        new DeleteCommandParser(),
        new ExitCommandParser(),
        new FlipCommandParser(),
        new ListCommandParser(),
        new ViewCommandParser(),
        new TagsCommandParser(),
        new TagCommandParser(),
        new FindCommandParser()
    };

    public static Command parse(String fullCommand) throws InvalidCommandException, InvalidArgumentException {
        if (fullCommand == null || fullCommand.isBlank()) {
            throw new InvalidCommandException("Command String cannot be null or empty");
        }

        String commandWord = fullCommand.trim().split("\\s+")[0];

        for (CommandParser p : parsers) {
            if (commandWord.equals(p.MATCH_PREFIX)) {
                return p.parse(fullCommand);
            }
        }
        throw new InvalidCommandException("Unrecognized command");
    }
}
