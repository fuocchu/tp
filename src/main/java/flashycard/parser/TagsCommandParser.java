package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.TagsCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input for the "tags" command.
 * This command has no arguments and is used to list all unique tags present
 * in the knowledge base.
 */
public class TagsCommandParser extends CommandParser {

    /**
     * Initializes the parser with the "tags" keyword and expects no additional
     * arguments.
     */
    public TagsCommandParser() {
        super("tags", "");
    }

    /**
     * Validates the command format and returns a TagsCommand.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new TagsCommand instance.
     * @throws InvalidArgumentException If any unexpected arguments are provided.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        this.match(fullCommand);
        return new TagsCommand();
    }
}