package flashycard.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;

/**
 * A base class for parsing user input into specific application commands.
 * It uses regular expressions to validate and extract arguments from the
 * command string.
 */
public abstract class CommandParser {
    /** The keyword that identifies the command (e.g., "add", "delete"). */
    public final String MATCH_PREFIX;
    private final Pattern ARGS_REGEX;

    /**
     * Sets up a parser with a specific command keyword and an expected argument
     * pattern.
     *
     * @param prefix    The command keyword.
     * @param argsRegex The regex pattern for the arguments following the keyword.
     */
    public CommandParser(String prefix, String argsRegex) {
        MATCH_PREFIX = prefix;

        if (argsRegex == null || argsRegex.trim().isEmpty()) {
            ARGS_REGEX = Pattern.compile(String.format("^%s\\s*$", prefix));
        } else {
            ARGS_REGEX = Pattern.compile(String.format("^%s\\s*%s$", prefix, argsRegex));
        }
    }

    /**
     * Checks if the user input matches the expected command format.
     *
     * @param fullCommand The raw input string from the user.
     * @return A Matcher containing the captured groups from the regex.
     * @throws InvalidArgumentException If the input is null or doesn't match the
     *                                  required format.
     */
    protected Matcher match(String fullCommand) throws InvalidArgumentException {
        if (fullCommand == null) {
            throw new InvalidArgumentException("Command string cannot be null");
        }

        Matcher matcher = ARGS_REGEX.matcher(fullCommand.trim());
        if (!matcher.matches()) {
            throw new InvalidArgumentException(
                    String.format("Invalid argument format given for %s command", MATCH_PREFIX));
        }

        return matcher;
    }

    /**
     * Converts a raw command string into an executable Command object.
     *
     * @param fullCommand The raw input string to parse.
     * @return The resulting Command object.
     * @throws InvalidArgumentException If the arguments are formatted incorrectly.
     */
    public abstract Command parse(String fullCommand) throws InvalidArgumentException;
}
