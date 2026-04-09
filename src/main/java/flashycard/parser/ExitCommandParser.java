package flashycard.parser;

import flashycard.command.ExitCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input for the "exit" command.
 * It ensures that no additional arguments are provided when the user wants to
 * quit.
 */
public class ExitCommandParser extends CommandParser {

    /**
     * Initializes the parser with the "exit" keyword and expects no additional
     * arguments.
     */
    public ExitCommandParser() {
        super("exit", "");
    }

    /**
     * Validates that the command format is correct and returns an ExitCommand.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new ExitCommand instance.
     * @throws InvalidArgumentException If the command contains unexpected
     *                                  arguments.
     */
    @Override
    public ExitCommand parse(String fullCommand) throws InvalidArgumentException {
        this.match(fullCommand);

        return new ExitCommand();
    }

}
