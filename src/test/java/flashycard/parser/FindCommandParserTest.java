package flashycard.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import flashycard.command.FindCommand;
import flashycard.exceptions.InvalidArgumentException;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    void parse_globalSearch_returnsCorrectCommand() throws InvalidArgumentException {
        String input = "find France";
        FindCommand command = (FindCommand) parser.parse(input);


        assertEquals("france", command.getKeyword(), "Keyword should be lowercased");
        assertNull(command.getScope(), "Scope should be null for global search");
    }

    @Test
    void parse_questionScope_returnsCorrectCommand() throws InvalidArgumentException {
        String input = "find q/Paris";
        FindCommand command = (FindCommand) parser.parse(input);

        assertEquals("paris", command.getKeyword());
        assertEquals("q", command.getScope(), "Scope should be 'q'");
    }

    @Test
    void parse_answerScope_returnsCorrectCommand() throws InvalidArgumentException {
        String input = "find a/Napoleon";
        FindCommand command = (FindCommand) parser.parse(input);

        assertEquals("napoleon", command.getKeyword());
        assertEquals("a", command.getScope(), "Scope should be 'a'");
    }

    @Test
    void parse_withExtraSpaces_trimsKeyword() throws InvalidArgumentException {
        String input = "find    q/   Ancient Rome   ";
        FindCommand command = (FindCommand) parser.parse(input);

        assertEquals("ancient rome", command.getKeyword(), "Keyword should be trimmed and lowercased");
        assertEquals("q", command.getScope());
    }

    @Test
    void parse_emptyKeyword_throwsException() {
        String input = "find  ";
        assertThrows(InvalidArgumentException.class, () -> parser.parse(input));
    }

    @Test
    void parse_invalidScope_treatsAsGlobalKeyword() throws InvalidArgumentException {

        String input = "find x/Paris";
        FindCommand command = (FindCommand) parser.parse(input);

        assertNull(command.getScope());
        assertEquals("x/paris", command.getKeyword());
    }

    @Test
    void parse_nullInput_throwsException() {
        assertThrows(InvalidArgumentException.class, () -> parser.parse(null));
    }
}
