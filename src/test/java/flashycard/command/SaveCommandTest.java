package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveCommandTest {
    private KnowledgeBase hb;
    private Ui ui;
    private Storage storage;
    private SessionContainer session;

    @BeforeEach
    void setUp() {
        hb = new KnowledgeBase();
        ui = new Ui();
        storage = new Storage("data/test.txt");
        session = new SessionContainer();

        hb.addCard(new Card(1, "What is Java?", "A language", "tech"));
        hb.addCard(new Card(2, "What is Maven?", "Build tool", "tech"));
    }

    @Test
    void execute_saveSingleId_success() {
        SaveCommand command = new SaveCommand("1", "mySet");
        command.execute(hb, ui, storage, session);

        assertTrue(hb.getAllTestSets().containsKey("mySet"));
        assertEquals(1, hb.getAllTestSets().get("mySet").size());
        assertEquals(1, hb.getAllTestSets().get("mySet").get(0));
    }

    @Test
    void execute_saveAllFromSession_success() {
        Card card1 = new Card(1, "Q1", "A1", "none");
        Card card2 = new Card(2, "Q2", "A2", "none");
        session.setLastSearchResults(List.of(card1, card2));

        SaveCommand command = new SaveCommand("all", "searchSet");
        command.execute(hb, ui, storage, session);

        List<Integer> savedIds = hb.getAllTestSets().get("searchSet");
        assertEquals(2, savedIds.size());
        assertTrue(savedIds.contains(1));
        assertTrue(savedIds.contains(2));
    }

    @Test
    void execute_invalidId_showsError() {
        SaveCommand command = new SaveCommand("99", "failSet");
        command.execute(hb, ui, storage, session);

        assertTrue(!hb.getAllTestSets().containsKey("failSet"));
    }

    @Test
    void execute_allWithEmptySession_showsError() {
        session.setLastSearchResults(new java.util.ArrayList<>());

        SaveCommand command = new SaveCommand("all", "emptySet");
        command.execute(hb, ui, storage, session);

        assertTrue(!hb.getAllTestSets().containsKey("emptySet"),
                "Should not create a set if there are no search results to save.");
    }
}
