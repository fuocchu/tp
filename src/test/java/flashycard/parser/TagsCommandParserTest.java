package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.TagsCommand;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagsCommandParserTest {

    private TagsCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new TagsCommandParser();
    }

    @Test
    void parse_validInput_returnsTagsCommand() throws InvalidArgumentException {
        String input = "tags";
        Command result = parser.parse(input);

        assertTrue(result instanceof TagsCommand, "Result should be an instance of TagsCommand");
    }

    @Test
    void parse_validInputWithTrailingSpaces_returnsTagsCommand() throws InvalidArgumentException {
        String input = "tags   ";
        Command result = parser.parse(input);

        assertTrue(result instanceof TagsCommand);
    }

    @Test
    void parse_invalidExtraArguments_throwsInvalidArgumentException() {
        String input = "tags extra_stuff";

        assertThrows(InvalidArgumentException.class, () -> {
            parser.parse(input);
        }, "Should throw exception when extra arguments are provided");
    }

    @Test
    void parse_invalidCommandPrefix_throwsInvalidArgumentException() {
        String input = "nottags";

        assertThrows(InvalidArgumentException.class, () -> {
            parser.parse(input);
        });
    }
}
