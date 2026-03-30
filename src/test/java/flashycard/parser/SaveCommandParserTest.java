package flashycard.parser;

import flashycard.command.Command;
import flashycard.command.SaveCommand;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaveCommandParserTest {

    private SaveCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new SaveCommandParser();
    }

    @Test
    void parse_validTargetId_returnsSaveCommand() throws InvalidArgumentException {
        String input = "save 1 s/mySet";
        Command command = parser.parse(input);

        assertNotNull(command);
        assertTrue(command instanceof SaveCommand);
    }

    @Test
    void parse_validTargetAll_returnsSaveCommand() throws InvalidArgumentException {
        String input = "save all s/chemistry";
        Command command = parser.parse(input);

        assertNotNull(command);
        assertTrue(command instanceof SaveCommand);
    }

    @Test
    void parse_extraSpaces_handlesCorrectly() throws InvalidArgumentException {
        String input = "save    5    s/  physics  ";
        Command command = parser.parse(input);

        assertNotNull(command);
    }

    @Test
    void parse_missingSetName_throwsException() {
        String input = "save 1 s/";

        assertThrows(InvalidArgumentException.class, () -> {
            parser.parse(input);
        });
    }

    @Test
    void parse_invalidTarget_throwsException() {
        String input = "save some s/mySet";

        assertThrows(InvalidArgumentException.class, () -> {
            parser.parse(input);
        });
    }

    @Test
    void parse_missingPrefix_throwsException() {
        String input = "save 1 mySet";

        assertThrows(InvalidArgumentException.class, () -> {
            parser.parse(input);
        });
    }
}
