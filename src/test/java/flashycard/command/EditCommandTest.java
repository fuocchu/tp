package flashycard.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class EditCommandTest {

    @TempDir
    Path tempDir;

    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private SessionContainer session;
    private Card original;

    @BeforeEach
    public void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        storage = new Storage(tempDir.resolve("test_data.txt").toString());
        session = new SessionContainer();

        original = new Card("Original question", "Original answer");
        kb.addCard(original);
    }

    @Test
    public void execute_editBothFields_updatesCard() throws CardNotFoundException {
        new EditCommand(original.getId(), "New question", "New answer")
                .execute(kb, ui, storage, session);

        Card updated = kb.getCardById(original.getId());
        assertEquals("New question", updated.getQuestion());
        assertEquals("New answer", updated.getAnswer());
    }

    @Test
    public void execute_editQuestionOnly_keepsOriginalAnswer() throws CardNotFoundException {
        new EditCommand(original.getId(), "New question", null)
                .execute(kb, ui, storage, session);

        Card updated = kb.getCardById(original.getId());
        assertEquals("New question", updated.getQuestion());
        assertEquals("Original answer", updated.getAnswer());
    }

    @Test
    public void execute_editAnswerOnly_keepsOriginalQuestion() throws CardNotFoundException {
        new EditCommand(original.getId(), null, "New answer")
                .execute(kb, ui, storage, session);

        Card updated = kb.getCardById(original.getId());
        assertEquals("Original question", updated.getQuestion());
        assertEquals("New answer", updated.getAnswer());
    }

    @Test
    public void execute_editPreservesTag() throws CardNotFoundException {
        Card taggedCard = new Card(original.getId() + 1, "Q", "A", "science");
        kb.addCard(taggedCard);

        new EditCommand(taggedCard.getId(), "New Q", "New A")
                .execute(kb, ui, storage, session);

        Card updated = kb.getCardById(taggedCard.getId());
        assertEquals("science", updated.getTag());
    }

    @Test
    public void execute_invalidId_throwsCardNotFoundException() {
        assertThrows(CardNotFoundException.class, () ->
                new EditCommand(999, "Q", "A").execute(kb, ui, storage, session));
    }

    @Test
    public void isExit_returnsFalse() {
        assertFalse(new EditCommand(1, "Q", "A").isExit());
    }
}
