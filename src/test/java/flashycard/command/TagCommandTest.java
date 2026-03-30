package flashycard.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import flashycard.context.SessionContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class TagCommandTest {

    @TempDir
    Path tempDir;

    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private Path tempFile;
    private SessionContainer session;

    @BeforeEach
    public void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        tempFile = tempDir.resolve("test_data.txt");
        storage = new Storage(tempFile.toString());

        kb.addCard(new Card(1, "Question", "Answer", "none"));
    }

    @Test
    public void execute_validId_updatesTagInKnowledgeBase() throws CardNotFoundException {
        String newTagName = "Geography";
        TagCommand tagCommand = new TagCommand(1, newTagName);

        tagCommand.execute(kb, ui, storage, session);

        Card updatedCard = kb.getCardById(1);
        assertEquals(newTagName, updatedCard.getTag(), "The tag should be updated to 'Geography'.");
        assertEquals("Question", updatedCard.getQuestion(), "Question should remain unchanged.");
    }

    @Test
    public void execute_validTag_savesToStorage() throws CardNotFoundException, IOException {
        String newTagName = "Science";
        TagCommand tagCommand = new TagCommand(1, newTagName);

        tagCommand.execute(kb, ui, storage, session);

        String content = Files.readString(tempFile);
        assertTrue(content.contains("Science"), "Storage file should reflect the new tag.");
    }

    @Test
    public void execute_invalidId_throwsException() {
        TagCommand tagCommand = new TagCommand(99, "Urgent");

        assertThrows(CardNotFoundException.class, () -> {
            tagCommand.execute(kb, ui, storage, session);
        }, "Executing with a non-existent ID should throw CardNotFoundException.");
    }

    @Test
    public void execute_specialCharactersInTag_escapesCorrectly() throws CardNotFoundException {
        String trickyTag = "Math|Logic";
        TagCommand tagCommand = new TagCommand(1, trickyTag);

        tagCommand.execute(kb, ui, storage, session);

        Card updatedCard = kb.getCardById(1);
        assertEquals("Math|Logic", updatedCard.getTag());
    }
}
