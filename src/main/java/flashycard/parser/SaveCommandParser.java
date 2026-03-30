package flashycard.parser;

import java.util.regex.Matcher;
import flashycard.command.Command;
import flashycard.command.SaveCommand;
import flashycard.exceptions.InvalidArgumentException;

public class SaveCommandParser extends CommandParser {

    private static final String SAVE_REGEX = "(?<target>all|\\d+)\\s+s/(?<setName>.+)";

    public SaveCommandParser() {
        super("save", SAVE_REGEX);
    }

    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matcher = this.match(fullCommand);

        String target = matcher.group("target").trim();
        String setName = matcher.group("setName").trim();

        if (setName.isEmpty()) {
            throw new InvalidArgumentException("Set name cannot be empty. Usage: save [ID/all] s/SET_NAME");
        }

        return new SaveCommand(target, setName);
    }
}
