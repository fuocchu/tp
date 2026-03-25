package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.ListCommand;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListCommandParserTest {

    @Test
    void testParse_validListCommand_returnsCommand() throws InvalidArgumentException {
        ListCommandParser parser = new ListCommandParser();
        Command command = parser.parse("list");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    void testParse_validListCommandWithWhitespace_returnsCommand() throws InvalidArgumentException {
        ListCommandParser parser = new ListCommandParser();
        Command command = parser.parse("  list  ");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    void testParse_invalidCommand_throwsInvalidArgumentException() {
        ListCommandParser parser = new ListCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("listt"));
    }

    @Test
    void testParse_emptyString_throwsInvalidArgumentException() {
        ListCommandParser parser = new ListCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
