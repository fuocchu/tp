package flashycard.command;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class ExitCommandTest {

    @Test
    public void testIsExit() {
        ExitCommand command = new ExitCommand();
        assertTrue(command.isExit(), "ExitCommand should return true for isExit()");
    }

    @Test
    public void testExecute() {
        ExitCommand command = new ExitCommand();
        KnowledgeBase cards = new KnowledgeBase();
        Ui ui = new Ui();
        Storage storage = null;

        assertDoesNotThrow(() -> command.execute(cards, ui, storage),
                "ExitCommand execute should not throw any exception");
    }
}
