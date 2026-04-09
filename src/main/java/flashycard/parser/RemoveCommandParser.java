package flashycard.parser;

import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;
import flashycard.command.Command;
import flashycard.command.RemoveCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "remove" command.
 * It identifies which card IDs, or all cards, should be removed from a specific
 * test set.
 */
public class RemoveCommandParser extends CommandParser {
    private static final String REMOVE_REGEX = "(?<target>all|[\\d\\s]+)\\s+s/(?<setName>.+)";

    /**
     * Initializes the parser with the "remove" keyword and a regex to capture
     * the target (specific IDs or "all") and the set name.
     */
    public RemoveCommandParser() {
        super("remove", REMOVE_REGEX);
    }

    /**
     * Extracts the set name and the target IDs from the command string.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new RemoveCommand instance configured with the target IDs and set
     *         name.
     * @throws InvalidArgumentException If the ID format is invalid or missing
     *                                  required components.
     */
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
