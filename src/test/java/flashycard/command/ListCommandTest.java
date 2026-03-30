package flashycard.command;

import flashycard.context.SessionContainer;
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
    private SessionContainer session;

    @BeforeEach
    void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        storage = null;
        session = new SessionContainer();
        listCommand = new ListCommand(null);
    }

    @Test
    void execute_emptyKnowledgeBase_executesWithoutError() {
        listCommand.execute(kb, ui, storage, session);
        assertFalse(listCommand.isExit());
    }

    @Test
    void execute_withCards_executesWithoutError() {
        kb.addCard(new Card(1, "What is Java?", "A programming language.", "Programming"));
        kb.addCard(new Card(2, "What is JUnit?", "A testing framework.", "Testing"));

        listCommand.execute(kb, ui, storage, session);
        assertFalse(listCommand.isExit());
    }

    @Test
    void isExit_returnsFalse() {
        assertFalse(listCommand.isExit());
    }
}
