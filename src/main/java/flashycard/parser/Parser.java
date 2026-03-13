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
        new ViewCommandParser()
    };

    public static Command parse(String fullCommand) throws InvalidCommandException, InvalidArgumentException {
        if (fullCommand == null) {
            throw new InvalidCommandException("Command String cannot be null");
        }

        for (CommandParser p : parsers) {
            if (fullCommand.startsWith(p.MATCH_PREFIX)) {
                return p.parse(fullCommand);
            }
        }

        throw new InvalidCommandException("Unrecognized command");
    }
}
