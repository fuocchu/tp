package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.Command;
import flashycard.command.DummyCommand;
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

        return new DummyCommand(question, answer); // TODO: return the correct class
    }

}
