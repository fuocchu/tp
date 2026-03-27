package flashycard.command;

import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ViewCommandTest {
    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        storage = null;
    }

    @Test
    void execute_validCardId_displaysCard() throws CardNotFoundException {
        kb.addCard(new Card(1, "What is Java?", "A programming language."));
        ViewCommand viewCommand = new ViewCommand(1);
        assertDoesNotThrow(() -> viewCommand.execute(kb, ui, storage));
    }

    @Test
    void execute_invalidCardId_throwsCardNotFoundException() {
        ViewCommand viewCommand = new ViewCommand(999);
        assertThrows(CardNotFoundException.class, () -> viewCommand.execute(kb, ui, storage));
    }

    @Test
    void execute_multipleCards_viewsCorrectCard() throws CardNotFoundException {
        kb.addCard(new Card("Question 1", "Answer 1"));
        kb.addCard(new Card("Question 2", "Answer 2"));
        ViewCommand viewCommand = new ViewCommand(1);
        assertDoesNotThrow(() -> viewCommand.execute(kb, ui, storage));
    }

    @Test
    void execute_emptyKnowledgeBase_throwsCardNotFoundException() {
        ViewCommand viewCommand = new ViewCommand(1);
        assertThrows(CardNotFoundException.class, () -> viewCommand.execute(kb, ui, storage));
    }
}
