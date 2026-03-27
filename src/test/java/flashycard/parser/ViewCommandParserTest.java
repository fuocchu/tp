package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.ViewCommand;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ViewCommandParserTest {

    @Test
    void testParse_validId_returnsCommand() throws InvalidArgumentException {
        ViewCommandParser parser = new ViewCommandParser();
        Command command = parser.parse("view 42");
        assertTrue(command instanceof ViewCommand);
    }

    @Test
    void testParse_validIdWithSpaces_returnsCommand() throws InvalidArgumentException {
        ViewCommandParser parser = new ViewCommandParser();
        Command command = parser.parse("view    7   ");
        assertTrue(command instanceof ViewCommand);
    }

    @Test
    void testParse_invalidId_throwsException() {
        ViewCommandParser parser = new ViewCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("view abc"));
    }

    @Test
    void testParse_missingId_throwsException() {
        ViewCommandParser parser = new ViewCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("view "));
    }

    @Test
    void testParse_emptyString_throwsInvalidArgumentException() {
        ViewCommandParser parser = new ViewCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
