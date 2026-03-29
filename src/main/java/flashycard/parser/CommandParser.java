package flashycard.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;

public abstract class CommandParser {
    public final String MATCH_PREFIX;
    private final Pattern ARGS_REGEX;

    public CommandParser(String prefix, String argsRegex) {
        this.MATCH_PREFIX = prefix;

        if (argsRegex == null || argsRegex.trim().isEmpty()) {
            this.ARGS_REGEX = Pattern.compile(String.format("^%s\\s*$", prefix));
        } else {
            this.ARGS_REGEX = Pattern.compile(String.format("^%s\\s*%s$", prefix, argsRegex));
        }
    }

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

    public abstract Command parse(String fullCommand) throws InvalidArgumentException;
}
