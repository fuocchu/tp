package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.AddCommand;
import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;

public class AddCommandParser extends CommandParser {
    public AddCommandParser() {
        super("add", "q/(?<question>.+?)\\ba/(?<answer>.+)");
    }

    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matches = this.match(fullCommand);

        String question = matches.group("question").trim();
        String answer = matches.group("answer").trim();

        return new AddCommand(question, answer);
    }
}
