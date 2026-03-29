package flashycard.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import flashycard.command.Command;
import flashycard.command.TagCommand;
import flashycard.exceptions.InvalidArgumentException;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    void parse_validInput_returnsTagCommand() throws InvalidArgumentException {
        String input = "tag 1 t/Biology";
        Command command = parser.parse(input);

        assertTrue(command instanceof TagCommand, "Command should be an instance of TagCommand.");
    }

    @Test
    void parse_validInputWithExtraSpaces_returnsTagCommand() throws InvalidArgumentException {
        String input = "tag    5    t/   Space Physics  ";
        Command command = parser.parse(input);

        assertTrue(command instanceof TagCommand);
    }

    @Test
    void parse_missingTagFlag_throwsException() {
        String input = "tag 1 Physics";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void parse_nonNumericId_throwsException() {
        String input = "tag abc t/General";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void parse_missingId_throwsException() {
        String input = "tag t/General";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void parse_emptyTagName_throwsException() {
        String input = "tag 1 t/ ";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void parse_wrongCommandWord_throwsException() {
        String input = "label 1 t/Biology";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void parse_nullOrEmpty_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse(null));
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
