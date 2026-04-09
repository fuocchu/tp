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
        Storage storage = new Storage(testFilePath);
        assertTrue(file.exists());

        KnowledgeBase kb = new KnowledgeBase();
        Card card = new Card("Q1?", "A1!");
        kb.addCard(card);

        storage.save(kb);
        assertTrue(Files.size(file.toPath()) > 0);

        KnowledgeBase loadedKb = storage.load();
        assertEquals(1, loadedKb.getSize());
        Card loadedCard = loadedKb.getCardById(card.getId());
        assertEquals("Q1?", loadedCard.getQuestion());
        assertEquals("A1!", loadedCard.getAnswer());
    }

    @Test
    void storage_canSaveAndLoadWithTags() throws Exception {
        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        Card card = new Card(1, "What is Java?", "A language", "Programming");
        kb.addCard(card);

        storage.save(kb);

        KnowledgeBase loadedKb = storage.load();
        Card loadedCard = loadedKb.getCardById(1);
        assertEquals("Programming", loadedCard.getTag());
    }

    @Test
    void storage_handlesEmptyTestSets() throws Exception {
        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        kb.addTestSet("EmptySet", new java.util.ArrayList<>());

        storage.save(kb);

        KnowledgeBase loadedKb = storage.load();
        assertTrue(loadedKb.getAllTestSets().containsKey("EmptySet"));
        assertTrue(loadedKb.getAllTestSets().get("EmptySet").isEmpty());
    }

    @Test
    void storage_escapesPipeCharacters() throws Exception {
        Storage storage = new Storage(testFilePath);
        KnowledgeBase kb = new KnowledgeBase();

        Card card = new Card(1, "True | False?", "True", "logic");
        kb.addCard(card);
        kb.addTestSet("Logic | Math", List.of(1));

        storage.save(kb);

        KnowledgeBase loadedKb = storage.load();
        assertEquals("True | False?", loadedKb.getCardById(1).getQuestion());
        assertTrue(loadedKb.getAllTestSets().containsKey("Logic | Math"));
    }

    @Test
    void storage_handlesLegacyDataWithoutTags() throws Exception {
        Storage storage = new Storage(testFilePath);
        String legacyLine = "99|Old Question|Old Answer\n";
        Files.writeString(file.toPath(), legacyLine);

        KnowledgeBase loadedKb = storage.load();
        Card legacyCard = loadedKb.getCardById(99);
        assertEquals("none", legacyCard.getTag());
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
        assertTrue(loadedKb.getAllTestSets().containsKey("FinalExam"));
        List<Integer> loadedIds = loadedKb.getAllTestSets().get("FinalExam");
        assertEquals(2, loadedIds.size());
        assertTrue(loadedIds.contains(1));
    }

    @Test
    void storage_handlesCorruptedData_throwsCorruptedDataException() throws Exception {
        Storage storage = new Storage(testFilePath);
        String corruptedLine = "NotAnInteger|Missing Parts\n";
        Files.writeString(file.toPath(), corruptedLine);

        assertThrows(CorruptedDataException.class, storage::load);
    }

    @Test
    void storage_loadIgnoresBlankLines() throws Exception {
        Storage storage = new Storage(testFilePath);
        String dataWithBlankLine = "1|Q|A|T\n   \n2|Q2|A2|T2\n";
        Files.writeString(file.toPath(), dataWithBlankLine);

        KnowledgeBase loadedKb = storage.load();
        assertEquals(2, loadedKb.getSize());
    }

    @Test
    void storage_handlesMissingPartsInCard_throwsCorruptedDataException() throws Exception {
        Storage storage = new Storage(testFilePath);
        String corruptedLine = "1|OnlyQuestion\n";
        Files.writeString(file.toPath(), corruptedLine);

        assertThrows(CorruptedDataException.class, storage::load);
    }

    @Test
    void storage_loadThrowsCorruptedDataExceptionOnIOException() throws Exception {
        Storage storage = new Storage(testFilePath);
        // Force IOException by deleting the file right before loading
        file.delete();

        assertThrows(CorruptedDataException.class, storage::load);
    }

    @Test
    void storage_saveThrowsRuntimeExceptionOnIOException() {
        File dirAsFile = new File("fake_dir_as_file");
        dirAsFile.mkdir();

        // This will successfully bypass creation because the directory "exists"
        Storage storage = new Storage("fake_dir_as_file");
        KnowledgeBase kb = new KnowledgeBase();

        // FileWriter will throw a FileNotFoundException (which is an IOException) when
        // attempting to write to a directory
        assertThrows(RuntimeException.class, () -> storage.save(kb));

        dirAsFile.delete();
    }

    @Test
    void storage_handlesBlankTestSetIds() throws Exception {
        Storage storage = new Storage(testFilePath);
        // Triggers the !parts[1].isBlank() false branch in parseAndAddTestSet
        Files.writeString(file.toPath(), "SET:BlankSet|   \n");

        KnowledgeBase loadedKb = storage.load();
        assertTrue(loadedKb.getAllTestSets().containsKey("BlankSet"));
        assertTrue(loadedKb.getAllTestSets().get("BlankSet").isEmpty());
    }
}
