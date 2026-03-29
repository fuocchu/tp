package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.TagsCommand;
import flashycard.exceptions.InvalidArgumentException;

public class TagsCommandParser extends CommandParser {

    public TagsCommandParser() {
        super("tags", "");
    }

    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        this.match(fullCommand);
        return new TagsCommand();
    }
}
