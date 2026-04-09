package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoveCommandTest {

    private KnowledgeBase kb;
    private StubUi ui;
    private StubStorage storage;
    private SessionContainer session;

    /**
     * Stub for Ui to capture error messages.
     */
    private static class StubUi extends Ui {
        public List<String> errors = new ArrayList<>();

        @Override
        public void showError(String message) {
            errors.add(message);
        }
    }

    /**
     * Stub for Storage to verify if save was triggered.
     */
    private static class StubStorage extends Storage {
        public boolean saveCalled = false;

        public StubStorage() {
            super("dummy.txt");
        }

        @Override
        public void save(KnowledgeBase kb) {
            saveCalled = true;
        }
    }

    @BeforeEach
    void setUp() {
        kb = new KnowledgeBase();
        ui = new StubUi();
        storage = new StubStorage();
        session = new SessionContainer();
    }

    @Test
    void execute_setNameDoesNotExist_showsError() throws CardNotFoundException {
        RemoveCommand command = new RemoveCommand(null, "MissingSet");
        command.execute(kb, ui, storage, session);

        assertEquals(1, ui.errors.size());
        assertTrue(ui.errors.get(0).contains("does not exist"));
        assertFalse(storage.saveCalled);
    }

    @Test
    void execute_cardIdsNull_clearsEntireSet() throws CardNotFoundException {
        String setName = "SetA";
        kb.addTestSet(setName, new ArrayList<>(Arrays.asList(1, 2, 3)));

        RemoveCommand command = new RemoveCommand(null, setName);
        command.execute(kb, ui, storage, session);

        assertTrue(kb.getAllTestSets().get(setName).isEmpty());
        assertTrue(storage.saveCalled);
    }

    @Test
    void execute_validIds_removesSelectedCards() throws CardNotFoundException {
        String setName = "SetB";
        kb.addTestSet(setName, new ArrayList<>(Arrays.asList(10, 20, 30)));

        RemoveCommand command = new RemoveCommand(Arrays.asList(10, 30), setName);
        command.execute(kb, ui, storage, session);

        List<Integer> remaining = kb.getAllTestSets().get(setName);
        assertEquals(1, remaining.size());
        assertEquals(20, remaining.get(0));
        assertTrue(storage.saveCalled);
    }

    @Test
    void execute_someIdsNotFound_handlesExceptionAndSavesOthers() throws CardNotFoundException {
        String setName = "SetC";
        kb.addTestSet(setName, new ArrayList<>(Arrays.asList(1)));

        // 1 exists, 99 does not
        RemoveCommand command = new RemoveCommand(Arrays.asList(1, 99), setName);
        command.execute(kb, ui, storage, session);

        assertTrue(kb.getAllTestSets().get(setName).isEmpty());
        assertEquals(1, ui.errors.size());
        assertTrue(ui.errors.get(0).contains("#99"));
        assertTrue(storage.saveCalled);
    }

    @Test
    void execute_allIdsInvalid_doesNotSave() throws CardNotFoundException {
        String setName = "SetD";
        kb.addTestSet(setName, new ArrayList<>());

        RemoveCommand command = new RemoveCommand(Arrays.asList(404), setName);
        command.execute(kb, ui, storage, session);

        assertEquals(1, ui.errors.size());
        assertFalse(storage.saveCalled);
    }

    @Test
    void isExit_returnsFalse() {
        RemoveCommand command = new RemoveCommand(null, "test");
        assertFalse(command.isExit());
    }
}
