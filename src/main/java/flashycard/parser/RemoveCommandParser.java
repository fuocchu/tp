package flashycard.parser;

import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;
import flashycard.command.Command;
import flashycard.command.RemoveCommand;
import flashycard.exceptions.InvalidArgumentException;

public class RemoveCommandParser extends CommandParser {
    private static final String REMOVE_REGEX = "(?<target>all|[\\d\\s]+)\\s+s/(?<setName>.+)";

    public RemoveCommandParser() {
        super("remove", REMOVE_REGEX);
    }

    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matcher = this.match(fullCommand);

        String target = matcher.group("target").trim().toLowerCase();
        String setName = matcher.group("setName").trim();

        if (target.equals("all")) {
            return new RemoveCommand(null, setName);
        }

        List<Integer> idList = new ArrayList<>();
        String[] parts = target.split("\\s+");
        for (String part : parts) {
            try {
                idList.add(Integer.parseInt(part));
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("ID '" + part + "' must be a valid number or 'all'.");
            }
        }

        return new RemoveCommand(idList, setName);
    }
}
