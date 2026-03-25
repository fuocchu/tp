package flashycard.command;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ListCommandTest {
    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private ListCommand listCommand;

    @BeforeEach
    void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        storage = null;
        listCommand = new ListCommand();
    }

    @Test
    void execute_emptyKnowledgeBase_executesWithoutError() {
        listCommand.execute(kb, ui, storage);
        assertFalse(listCommand.isExit());
    }

    @Test
    void execute_withCards_executesWithoutError() {
        kb.addCard(new Card("What is Java?", "A programming language."));
        kb.addCard(new Card("What is JUnit?", "A testing framework."));

        listCommand.execute(kb, ui, storage);
        assertFalse(listCommand.isExit());
    }

    @Test
    void isExit_returnsFalse() {
        assertFalse(listCommand.isExit());
    }
}
