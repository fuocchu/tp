package flashycard.storage;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private final String testFilePath = "test_storage.txt";

    @Test
    void storageCreatesFileAndCanLoadSave() throws Exception {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
        Storage storage = new Storage(testFilePath);
        assertTrue(file.exists(), "Storage file should be created");

        KnowledgeBase kb = new KnowledgeBase();
        Card card = new Card("Q1?", "A1!");
        kb.addCard(card);

        storage.save(kb);
        assertTrue(Files.size(file.toPath()) > 0, "Storage file should not be empty after save");

        KnowledgeBase loadedKb = storage.load();
        assertEquals(1, loadedKb.getSize(), "Loaded KnowledgeBase should have 1 card");
        Card loadedCard = loadedKb.getCardById(card.getId());
        assertEquals("Q1?", loadedCard.getQuestion());
        assertEquals("A1!", loadedCard.getAnswer());
        file.delete();
    }

    @Test
    void storage_canSaveAndLoadWithTags() throws Exception {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }

        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        Card card = new Card(1, "What is Java?", "A language", "Programming");
        kb.addCard(card);

        storage.save(kb);

        String fileContent = Files.readString(file.toPath());
        assertTrue(fileContent.contains("Programming"), "File should store the tag string");

        KnowledgeBase loadedKb = storage.load();
        Card loadedCard = loadedKb.getCardById(1);
        assertEquals("Programming", loadedCard.getTag(), "Loaded tag should match original");

        file.delete();
    }

    @Test
    void storage_handlesLegacyDataWithoutTags() throws Exception {
        File file = new File(testFilePath);
        String legacyLine = "99|Old Question|Old Answer\n";
        Files.writeString(file.toPath(), legacyLine);

        Storage storage = new Storage(testFilePath);
        KnowledgeBase loadedKb = storage.load();

        Card legacyCard = loadedKb.getCardById(99);
        assertEquals("none", legacyCard.getTag(), "Legacy data should default tag to 'none'");

        file.delete();
    }
}
