package flashycard.context;

import flashycard.model.Card;
import flashycard.model.StudySession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionContainerTest {

    private SessionContainer session;

    @BeforeEach
    void setUp() {
        session = new SessionContainer();
    }

    @Test
    void constructor_initializesWithEmptySearchResultsAndNullSession() {
        assertNull(session.getSession());
        assertFalse(session.hasActiveSession());
        assertNotNull(session.getLastSearchResults());
        assertTrue(session.getLastSearchResults().isEmpty());
    }

    @Test
    void setgetSession_manageStateCorrectly() {
        List<Card> dummyCards = new ArrayList<>();
        dummyCards.add(new Card(1, "Test Q", "Test A", "TestTag"));
        StudySession studySession = new StudySession(dummyCards);

        session.setSession(studySession);

        assertTrue(session.hasActiveSession());
        assertEquals(studySession, session.getSession());

        session.setSession(null);

        assertFalse(session.hasActiveSession());
        assertNull(session.getSession());
    }

    @Test
    void setLastSearchResults_withNullInput_storesEmptyList() {
        session.setLastSearchResults(null);

        List<Card> results = session.getLastSearchResults();
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getLastSearchResults_alwaysReturnsNonNullList() {
        assertNotNull(session.getLastSearchResults());

        session.setLastSearchResults(null);
        assertNotNull(session.getLastSearchResults());
    }
}
