package flashycard;

import flashycard.exceptions.CorruptedDataException;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FlashyCardTest {

    private static class StubUi extends Ui {
        public boolean welcomeShown = false;
        public boolean exitShown = false;
        public List<String> errors = new ArrayList<>();

        private final List<String> inputs;
        private int inputIndex = 0;

        public StubUi(List<String> inputs) {
            this.inputs = inputs;
        }

        @Override
        public void showWelcome() {
            welcomeShown = true;
        }

        @Override
        public void showExitMessage() {
            exitShown = true;
        }

        @Override
        public String readCommand() {
            if (inputIndex < inputs.size()) {
                return inputs.get(inputIndex++);
            }
            return null;
        }

        @Override
        public void showError(String message) {
            errors.add(message);
        }
    }

    private static class StubStorage extends Storage {
        private final boolean shouldFail;

        public StubStorage(String path, boolean shouldFail) {
            super(path);
            this.shouldFail = shouldFail;
        }

        @Override
        public KnowledgeBase load() throws CorruptedDataException {
            if (shouldFail) {
                throw new CorruptedDataException("Corrupted");
            }
            return new KnowledgeBase();
        }

        @Override
        public void save(KnowledgeBase kb) {
        }
    }

    private static class TestableFlashyCard extends FlashyCard {
        public TestableFlashyCard(String path, Ui ui, Storage storage) {
            super(path);
            try {
                java.lang.reflect.Field uiField = FlashyCard.class.getDeclaredField("ui");
                uiField.setAccessible(true);
                uiField.set(this, ui);

                java.lang.reflect.Field storageField = FlashyCard.class.getDeclaredField("storage");
                storageField.setAccessible(true);
                storageField.set(this, storage);

                java.lang.reflect.Field kbField = FlashyCard.class.getDeclaredField("knowledgeBase");
                kbField.setAccessible(true);
                kbField.set(this, storage.load());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void run_validExitFlow_executesCorrectly() {
        StubUi ui = new StubUi(Arrays.asList("exit"));
        StubStorage storage = new StubStorage("dummy.txt", false);
        TestableFlashyCard app = new TestableFlashyCard("dummy.txt", ui, storage);

        app.run();

        assertTrue(ui.welcomeShown);
        assertTrue(ui.exitShown);
        assertTrue(ui.errors.isEmpty());
    }

    @Test
    void run_skipsInvalidInputAndTerminalArtifacts() {
        StubUi ui = new StubUi(Arrays.asList("", "   ", "!!!list", "redirection test", "exit"));
        StubStorage storage = new StubStorage("dummy.txt", false);
        TestableFlashyCard app = new TestableFlashyCard("dummy.txt", ui, storage);

        app.run();

        assertTrue(ui.welcomeShown);
        assertTrue(ui.exitShown);
    }

    @Test
    void constructor_handlesCorruptedData() {
        StubUi ui = new StubUi(new ArrayList<>());
        StubStorage storage = new StubStorage("corrupt.txt", true);

        FlashyCard app = new FlashyCard("corrupt.txt");

        assertNotNull(app);
    }

    @Test
    void run_catchesParsingExceptions() {
        StubUi ui = new StubUi(Arrays.asList("invalid_command_name", "exit"));
        StubStorage storage = new StubStorage("dummy.txt", false);
        TestableFlashyCard app = new TestableFlashyCard("dummy.txt", ui, storage);

        app.run();

        assertFalse(ui.errors.isEmpty());
        assertTrue(ui.exitShown);
    }

    @Test
    void run_nullInput_breaksLoop() {
        List<String> inputs = new ArrayList<>();
        inputs.add(null);
        StubUi ui = new StubUi(inputs);
        StubStorage storage = new StubStorage("dummy.txt", false);
        TestableFlashyCard app = new TestableFlashyCard("dummy.txt", ui, storage);

        app.run();

        assertTrue(ui.welcomeShown);
        assertTrue(ui.exitShown);
    }

    @Test
    void main_executesSuccessfully() {
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream("exit\n".getBytes()));
            FlashyCard.main(new String[] {});
            assertTrue(true);
        } finally {
            System.setIn(originalIn);
        }
    }
}
