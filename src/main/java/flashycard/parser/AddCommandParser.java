package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.AddCommand;
import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "add" command.
 * It expects a question prefixed with 'q/' and an answer prefixed with 'a/'.
 */
public class AddCommandParser extends CommandParser {

    /**
     * Initializes the parser with the "add" keyword and the required regex pattern
     * for capturing the question and answer groups.
     */
    public AddCommandParser() {
        super("add", "q/(?<question>.+?)\\ba/(?<answer>.+)");
    }

    /**
     * Extracts the question and answer from the command string and creates
     * a new AddCommand.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new AddCommand instance containing the parsed data.
     * @throws InvalidArgumentException If the input does not follow the q/ and a/
     *                                  format.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matches = this.match(fullCommand);

        String question = matches.group("question").trim();
        String answer = matches.group("answer").trim();

        return new AddCommand(question, answer);
    }
}
