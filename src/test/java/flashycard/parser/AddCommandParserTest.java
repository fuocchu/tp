package flashycard.parser;

import flashycard.command.Command;
import flashycard.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddCommandParserTest {

    @Test
    void testParse_validInput_returnsCommand() throws InvalidArgumentException {
        AddCommandParser parser = new AddCommandParser();
        String input = "add q/A question? a/A answer";
        Command command = parser.parse(input);
        // TODO: Change to specific command type
        assertTrue(command instanceof Command);
    }

    @Test
    void testParse_trimsWhitespace() throws InvalidArgumentException {
        AddCommandParser parser = new AddCommandParser();
        String input = "add      q/   hello?   a/   bye  ";
        Command command = parser.parse(input);
        // TODO: Change to specific command type
        assertTrue(command instanceof Command);
    }

    @Test
    void testParse_missingQuestion_throwsException() {
        AddCommandParser parser = new AddCommandParser();
        String input = "add a/answer only";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void testParse_missingAnswer_throwsException() {
        AddCommandParser parser = new AddCommandParser();
        String input = "add q/question only";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void testParse_invalidFormat_throwsException() {
        AddCommandParser parser = new AddCommandParser();
        String input = "add question answer";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void testParse_invalidEmptyQuestionAnswers_throwsException() {
        AddCommandParser parser = new AddCommandParser();
        String input = "add /q /a ";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void testParse_invalidCommand_throwsInvalidArgumentException() {
        AddCommandParser parser = new AddCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse("adddddd q/A question? a/A answer"));
    }

    @Test
    void testParse_emptyString_throwsInvalidArgumentException() {
        AddCommandParser parser = new AddCommandParser();
        assertThrows(InvalidArgumentException.class, () -> parser.parse(""));
    }
}
