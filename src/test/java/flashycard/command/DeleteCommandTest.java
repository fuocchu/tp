package flashycard.command;

import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {
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
    void execute_validCardId_deletesCard() throws CardNotFoundException {
        kb.addCard(new Card(1, "What is Java?", "A programming language."));
        DeleteCommand deleteCommand = new DeleteCommand(1);
        assertDoesNotThrow(() -> deleteCommand.execute(kb, ui, storage));
    }

    @Test
    void execute_invalidCardId_throwsCardNotFoundException() {
        DeleteCommand deleteCommand = new DeleteCommand(999);
        assertThrows(CardNotFoundException.class, () -> deleteCommand.execute(kb, ui, storage));
    }

    @Test
    void execute_multipleCards_deletesCorrectCard() throws CardNotFoundException {
        kb.addCard(new Card(4, "Question 1", "Answer 1"));
        kb.addCard(new Card(5, "Question 2", "Answer 2"));
        DeleteCommand deleteCommand = new DeleteCommand(5);
        assertDoesNotThrow(() -> deleteCommand.execute(kb, ui, storage));
    }

    @Test
    void execute_emptyKnowledgeBase_throwsCardNotFoundException() {
        DeleteCommand deleteCommand = new DeleteCommand(1);
        kb = new KnowledgeBase();
        assertThrows(CardNotFoundException.class, () -> deleteCommand.execute(kb, ui, storage));
    }

    @Test
    public void isExit_returnsFalse() {
        DeleteCommand command = new DeleteCommand(1);
        assertFalse(command.isExit());
    }
}
