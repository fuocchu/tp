package flashycard.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import flashycard.command.Command;
import flashycard.command.RemoveCommand;
import flashycard.exceptions.InvalidArgumentException;

public class RemoveCommandParserTest {

    private final RemoveCommandParser parser = new RemoveCommandParser();

    @Test
    void parse_allTarget_returnsRemoveCommand() throws InvalidArgumentException {
        Command command = parser.parse("remove all s/mySet");
        assertTrue(command instanceof RemoveCommand);
    }

    @Test
    void parse_singleId_returnsRemoveCommand() throws InvalidArgumentException {
        Command command = parser.parse("remove 1 s/mySet");
        assertTrue(command instanceof RemoveCommand);
    }

    @Test
    void parse_multipleIds_returnsRemoveCommand() throws InvalidArgumentException {
        Command command = parser.parse("remove 1 2 3 s/test set");
        assertTrue(command instanceof RemoveCommand);
    }

    @Test
    void parse_multipleIdsWithExtraSpaces_returnsRemoveCommand() throws InvalidArgumentException {
        Command command = parser.parse("remove   1   2   3   s/  test set  ");
        assertTrue(command instanceof RemoveCommand);
    }

    @Test
    void parse_idTooLarge_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("remove 99999999999 s/mySet"));
    }

    @Test
    void parse_invalidIdFormat_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("remove 1a2 s/mySet"));
    }

    @Test
    void parse_missingSetName_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("remove all"));
    }

    @Test
    void parse_missingTarget_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("remove s/mySet"));
    }

    @Test
    void parse_wrongCommand_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse("delete 1 s/mySet"));
    }

    @Test
    void parse_nullInput_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse(null));
    }
}
