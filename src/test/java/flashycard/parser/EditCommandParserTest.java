package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.EditCommand;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditCommandParserTest {

    private EditCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new EditCommandParser();
    }

    @Test
    void parse_validIdAndBothFields_returnsEditCommand() throws InvalidArgumentException {
        Command command = parser.parse("edit 1 q/New Question a/New Answer");
        assertTrue(command instanceof EditCommand);
    }

    @Test
    void parse_onlyQuestion_returnsEditCommand() throws InvalidArgumentException {
        Command command = parser.parse("edit 42 q/Updated Question");
        assertTrue(command instanceof EditCommand);
    }

    @Test
    void parse_onlyAnswer_returnsEditCommand() throws InvalidArgumentException {
        Command command = parser.parse("edit 10 a/Updated Answer");
        assertTrue(command instanceof EditCommand);
    }

    @Test
    void parse_missingAllUpdateFields_throwsInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("edit 1"));
    }

    @Test
    void parse_invalidIdFormat_throwsInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("edit abc q/Question"));
    }

    @Test
    void parse_emptyString_throwsInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
