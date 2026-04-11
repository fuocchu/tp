package flashycard.parser;

import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;
import flashycard.exceptions.InvalidCommandException;

/**
 * The main entry point for converting user input strings into executable
 * Command objects. It maintains a registry of specific command parsers and
 * delegates the parsing task to the one that matches the user's input keyword.
 */
public class Parser {
    private static CommandParser[] parsers = new CommandParser[] {
        new AddCommandParser(),
        new DeleteCommandParser(),
        new EditCommandParser(),
        new ExitCommandParser(),
        new FlipCommandParser(),
        new ListCommandParser(),
        new ViewCommandParser(),
        new TagsCommandParser(),
        new TagCommandParser(),
        new FindCommandParser(),
        new SaveCommandParser(),
        new RemoveCommandParser(),
        new TestCommandParser()
    };

    /**
     * Identifies the command word from the user input and selects the appropriate
     * parser to handle the arguments.
     *
     * @param fullCommand The raw input string provided by the user.
     * @return An executable Command object.
     * @throws InvalidCommandException  If the command keyword is not recognized or
     *                                  is empty.
     * @throws InvalidArgumentException If the arguments following the keyword are
     *                                  incorrectly formatted.
     */
    public static Command parse(String fullCommand) throws InvalidCommandException, InvalidArgumentException {
        if (fullCommand == null || fullCommand.isBlank()) {
            throw new InvalidCommandException("Command String cannot be null or empty");
        }

        String commandWord = fullCommand.trim().split("\\s+")[0];

        for (CommandParser p : parsers) {
            if (commandWord.equalsIgnoreCase(p.MATCH_PREFIX)) {
                return p.parse(fullCommand);
            }
        }
        throw new InvalidCommandException("Unrecognized command");
    }
}
