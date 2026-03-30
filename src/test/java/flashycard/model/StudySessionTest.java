package flashycard.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudySessionTest {
    private List<Card> testCards;
    private StudySession session;

    @BeforeEach
    void setUp() {
        testCards = new ArrayList<>();
        testCards.add(new Card(1, "Question 1", "Answer 1", "tag1"));
        testCards.add(new Card(2, "Question 2", "Answer 2", "tag2"));
        session = new StudySession(testCards);
    }

    @Test
    void constructor_initializesCorrectly() {
        assertEquals(2, session.getTotalCount());
        assertEquals(2, session.getRemainingCount());
        assertTrue(session.hasNext());
        assertEquals(1, session.getCurrentCard().getId());
    }

    @Test
    void moveToNext_updatesPointerAndCounts() {
        session.moveToNext();

        assertEquals(1, session.getRemainingCount(), "Remaining count should decrease after moving.");
        assertTrue(session.hasNext(), "Should still have one card left.");
        assertEquals(2, session.getCurrentCard().getId(), "Should now point to the second card.");
    }

    @Test
    void hasNext_returnsFalseAtEndOfSession() {
        session.moveToNext();
        session.moveToNext();

        assertFalse(session.hasNext(), "hasNext should be false when pointer reaches the list size.");
        assertEquals(0, session.getRemainingCount());
    }

    @Test
    void constructor_withEmptyList_handlesGracefully() {
        StudySession emptySession = new StudySession(new ArrayList<>());

        assertFalse(emptySession.hasNext());
        assertEquals(0, emptySession.getTotalCount());
        assertEquals(0, emptySession.getRemainingCount());
    }

    @Test
    void immutability_sessionDoesNotChangeIfOriginalListChanges() {
        testCards.add(new Card(3, "Q3", "A3", "none"));

        assertEquals(2, session.getTotalCount(), "Session count should not change after original list is modified.");
    }
}
