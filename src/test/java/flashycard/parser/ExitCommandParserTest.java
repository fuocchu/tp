package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.ExitCommand;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExitCommandParserTest {

    @Test
    void testParse_validExitCommand_returnsCommand() throws InvalidArgumentException {
        ExitCommandParser parser = new ExitCommandParser();
        Command command = parser.parse("exit");
        assertTrue(command instanceof ExitCommand);
    }

    @Test
    void testParse_validExitCommandWithWhitespace_returnsCommand() throws InvalidArgumentException {
        ExitCommandParser parser = new ExitCommandParser();
        Command command = parser.parse("  exit  ");
        assertTrue(command instanceof ExitCommand);
    }

    @Test
    void testParse_invalidCommand_throwsInvalidArgumentException() {
        ExitCommandParser parser = new ExitCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("exitt"));
    }

    @Test
    void testParse_emptyString_throwsInvalidArgumentException() {
        ExitCommandParser parser = new ExitCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
