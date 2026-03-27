package flashycard.model;

import org.junit.jupiter.api.Test;

import flashycard.exceptions.CardNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KnowledgeBaseTest {
    private KnowledgeBase knowledgeBase;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        knowledgeBase = new KnowledgeBase();
        card1 = new Card(1, "Question 1", "Answer 1");
        card2 = new Card(2, "Question 2", "Answer 2");
    }

    @Test
    void testAddCard_singleCard_success() {
        knowledgeBase.addCard(card1);
        assertEquals(1, knowledgeBase.getSize());
        assertTrue(knowledgeBase.hasCard(1));
    }

    @Test
    void testAddCard_multipleCards_success() {
        knowledgeBase.addCard(card1);
        knowledgeBase.addCard(card2);
        assertEquals(2, knowledgeBase.getSize());
    }

    @Test
    void testDeleteCard_validId_success() throws CardNotFoundException {
        knowledgeBase.addCard(card1);
        knowledgeBase.deleteCard(1);
        assertFalse(knowledgeBase.hasCard(1));
        assertEquals(0, knowledgeBase.getSize());
    }

    @Test
    void testDeleteCard_invalidId_throwsException() {
        assertThrows(CardNotFoundException.class, () -> knowledgeBase.deleteCard(999));
    }

    @Test
    void testGetAllCards_multipleCards_returnsAll() {
        knowledgeBase.addCard(card1);
        knowledgeBase.addCard(card2);
        assertEquals(2, knowledgeBase.getAllCards().size());
    }

    @Test
    void testGetCardById_validId_returnsCard() throws CardNotFoundException {
        knowledgeBase.addCard(card1);
        Card retrieved = knowledgeBase.getCardById(1);
        assertEquals(card1, retrieved);
    }

    @Test
    void testGetCardById_invalidId_throwsException() {
        assertThrows(CardNotFoundException.class, () -> knowledgeBase.getCardById(999));
    }

    @Test
    void testGetSize_emptyAndPopulated_returnsCorrectSize() {
        assertEquals(0, knowledgeBase.getSize());
        knowledgeBase.addCard(card1);
        assertEquals(1, knowledgeBase.getSize());
    }

    @Test
    void testHasCard_cardExists_returnsTrue() {
        assertFalse(knowledgeBase.hasCard(1));
        knowledgeBase.addCard(card1);
        assertTrue(knowledgeBase.hasCard(1));
    }

    @Test
    void testAddCard_duplicate_throwsException() {
        knowledgeBase.addCard(card1);
        assertThrows(AssertionError.class, () -> knowledgeBase.addCard(card1));
    }
}
