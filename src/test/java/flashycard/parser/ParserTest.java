package flashycard.parser;

import flashycard.command.Command;

import flashycard.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    @Test
    void parse_validAddCommand_returnsAddCommand() throws Exception {
        String command = "add q/abc? a/a b c d e f g ";
        Command result = Parser.parse(command);
        // TODO: Change to specific command type
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validDeleteCommand_returnsDeleteCommand() throws Exception {
        String command = "delete 1";
        Command result = Parser.parse(command);
        // TODO: Change to specific command type
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validExitCommand_returnsExitCommand() throws Exception {
        String command = "exit";
        Command result = Parser.parse(command);

        // TODO: Change to specific command type
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validFlipCommand_returnsFlipCommand() throws Exception {
        String command = "flip 1";
        Command result = Parser.parse(command);
        // TODO: Change to specific command type
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validListCommand_returnsListCommand() throws Exception {
        String command = "list";
        Command result = Parser.parse(command);
        // TODO: Change to specific command type
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validViewCommand_returnsViewCommand() throws Exception {
        String command = "view 1";
        Command result = Parser.parse(command);
        // TODO: Change to specific command type
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validTagCommand_returnsTagCommand() throws Exception {
        String command = "tag 1 t/Coding";
        Command result = Parser.parse(command);
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_validTagsCommand_returnsTagsCommand() throws Exception {
        String command = "tags";
        Command result = Parser.parse(command);
        assertTrue(result instanceof Command);
    }

    @Test
    void parse_invalidCommand_throwsInvalidCommandException() {
        String command = "unknowncommand";
        assertThrows(InvalidCommandException.class, () -> Parser.parse(command));
    }

    @Test
    void parse_nullCommand_throwsInvalidCommandException() {
        assertThrows(InvalidCommandException.class, () -> Parser.parse(null));
    }
}
