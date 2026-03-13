package flashycard.parser;

import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlipCommandParserTest {

    @Test
    void testParse_validId_returnsCommand() throws InvalidArgumentException {
        FlipCommandParser parser = new FlipCommandParser();
        Command command = parser.parse("flip 42");
        // TODO: Change to specific command type
        assertTrue(command instanceof Command);
    }

    @Test
    void testParse_validIdWithSpaces_returnsCommand() throws InvalidArgumentException {
        FlipCommandParser parser = new FlipCommandParser();
        Command command = parser.parse("flip    7   ");
        // TODO: Change to specific command type
        assertTrue(command instanceof Command);
    }

    @Test
    void testParse_invalidId_throwsException() {
        FlipCommandParser parser = new FlipCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("flip abc"));
    }

    @Test
    void testParse_missingId_throwsException() {
        FlipCommandParser parser = new FlipCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("flip "));
    }

    @Test
    void testParse_emptyString_throwsInvalidArgumentException() {
        FlipCommandParser parser = new FlipCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
