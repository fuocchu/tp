package flashycard.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class AddCommandTest {

    @TempDir
    Path tempDir;

    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private Path tempFile;

    @BeforeEach
    public void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        tempFile = tempDir.resolve("test_data.txt");
        storage = new Storage(tempFile.toString());
    }

    @Test
    public void execute_validCard_addsToKnowledgeBase() {
        String question = "What is Polymorphism?";
        String answer = "The ability of an object to take on many forms.";
        AddCommand addCommand = new AddCommand(question, answer);

        addCommand.execute(kb, ui, storage);

        assertEquals(1, kb.getSize(), "KnowledgeBase should contain exactly one card.");

        Card addedCard = kb.getAllCards().iterator().next();
        assertEquals(question, addedCard.getQuestion());
        assertEquals(answer, addedCard.getAnswer());
    }

    @Test
    public void execute_validCard_savesToStorage() throws IOException {

        AddCommand addCommand = new AddCommand("Q", "A");


        addCommand.execute(kb, ui, storage);


        assertTrue(Files.exists(tempFile), "The storage file should be created on disk.");

        String content = Files.readString(tempFile);
        assertTrue(content.contains("Q"), "Storage file should contain the question.");
        assertTrue(content.contains("A"), "Storage file should contain the answer.");
    }
}
