package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.ExitCommand;
import flashycard.exceptions.InvalidArgumentException;

public class ExitCommandParser extends CommandParser {
    public ExitCommandParser() {
        super("exit", "");
    }

    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        this.match(fullCommand);

        return new ExitCommand();
    }

}
