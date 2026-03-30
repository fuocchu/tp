package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {

    @TempDir
    Path tempDir;

    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private SessionContainer session;

    @BeforeEach
    void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        String tempPath = tempDir.resolve("test_del.txt").toString();
        storage = null;
    }

    @Test
    void execute_validCardId_deletesCard() throws CardNotFoundException {
        kb.addCard(new Card(1, "What is Java?", "A programming language.", "Programming"));
        DeleteCommand deleteCommand = new DeleteCommand(1);
        assertDoesNotThrow(() -> deleteCommand.execute(kb, ui, storage, session));

        assertEquals(0, kb.getSize(), "KnowledgeBase should be empty after deletion.");
    }

    @Test
    void execute_invalidCardId_throwsCardNotFoundException() {
        DeleteCommand deleteCommand = new DeleteCommand(999);
        assertThrows(CardNotFoundException.class, () -> deleteCommand.execute(kb, ui, storage, session));
    }

    @Test
    void execute_multipleCards_deletesCorrectCard() throws CardNotFoundException {
        kb.addCard(new Card(4, "Question 1", "Answer 1", "Tag 1"));
        kb.addCard(new Card(5, "Question 2", "Answer 2", "Tag 2"));
        DeleteCommand deleteCommand = new DeleteCommand(5);
        assertDoesNotThrow(() -> deleteCommand.execute(kb, ui, storage, session));
        assertEquals(1, kb.getSize());
        assertThrows(CardNotFoundException.class, () -> kb.getCardById(5));
        assertEquals("Question 1", kb.getCardById(4).getQuestion());
    }

    @Test
    void execute_emptyKnowledgeBase_throwsCardNotFoundException() {
        DeleteCommand deleteCommand = new DeleteCommand(1);
        kb = new KnowledgeBase();
        assertThrows(CardNotFoundException.class, () -> deleteCommand.execute(kb, ui, storage, session));
    }

    @Test
    public void isExit_returnsFalse() {
        DeleteCommand command = new DeleteCommand(1);
        assertFalse(command.isExit());
    }
}
