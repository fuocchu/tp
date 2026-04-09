package flashycard.storage;

import flashycard.exceptions.CorruptedDataException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private final String testFilePath = "test_storage.txt";
    private File file;

    @BeforeEach
    void setUp() {
        file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

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
    void storage_handlesEmptyTestSets() throws Exception {
        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        kb.addTestSet("EmptySet", new java.util.ArrayList<>());

        storage.save(kb);

        KnowledgeBase loadedKb = storage.load();
        assertTrue(loadedKb.getAllTestSets().containsKey("EmptySet"));
        assertTrue(loadedKb.getAllTestSets().get("EmptySet").isEmpty(),
                "Empty set should load as an empty list, not null");
    }

    @Test
    void storage_escapesPipeCharacters() throws Exception {
        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        Card card = new Card(1, "True | False?", "True", "logic");
        kb.addCard(card);
        kb.addTestSet("Logic | Math", List.of(1));

        storage.save(kb);

        String fileContent = Files.readString(file.toPath());
        assertTrue(fileContent.contains("True \\| False?"), "Pipes in questions should be escaped");
        assertTrue(fileContent.contains("SET:Logic \\| Math"), "Pipes in set names should be escaped");

        KnowledgeBase loadedKb = storage.load();
        assertEquals("True | False?", loadedKb.getCardById(1).getQuestion());
        assertTrue(loadedKb.getAllTestSets().containsKey("Logic | Math"));
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

    @Test
    void storage_canSaveAndLoadPopulatedTestSets() throws Exception {
        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        kb.addCard(new Card(1, "Q1", "A1", "tag1"));
        kb.addCard(new Card(2, "Q2", "A2", "tag2"));
        kb.addTestSet("FinalExam", List.of(1, 2));

        storage.save(kb);

        KnowledgeBase loadedKb = storage.load();

        assertTrue(loadedKb.getAllTestSets().containsKey("FinalExam"), "Set name should exist");
        List<Integer> loadedIds = loadedKb.getAllTestSets().get("FinalExam");
        assertEquals(2, loadedIds.size(), "Should contain exactly 2 IDs");
        assertTrue(loadedIds.contains(1));
        assertTrue(loadedIds.contains(2));

        file.delete();
    }

    @Test
    void storage_handlesCorruptedData_throwsCorruptedDataException() throws Exception {
        File file = new File(testFilePath);
        String corruptedLine = "NotAnInteger|Missing Parts\n";
        Files.writeString(file.toPath(), corruptedLine);

        Storage storage = new Storage(testFilePath);

        assertThrows(CorruptedDataException.class, storage::load,
                "Loading a file with malformed data should throw CorruptedDataException");

        file.delete();
    }
}
