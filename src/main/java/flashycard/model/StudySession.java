package flashycard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the state of an active study session.
 * It keeps track of which cards are being reviewed and the user's progress
 * through the current deck.
 */
public class StudySession {
    private final List<Card> loadedCards;
    private int currentIndex;

    /**
     * Initializes a new session with a specific list of cards.
     *
     * @param cards The cards to be used in this session.
     */
    public StudySession(List<Card> cards) {
        this.loadedCards = new ArrayList<>(cards);
        this.currentIndex = 0;
    }

    /**
     * Checks if there are more cards left to review in this session.
     *
     * @return true if the end of the list hasn't been reached, false otherwise.
     */
    public boolean hasNext() {
        return currentIndex < loadedCards.size();
    }

    /**
     * Retrieves the card that the user is currently viewing.
     *
     * @return The current Card object.
     */
    public Card getCurrentCard() {
        return loadedCards.get(currentIndex);
    }

    /**
     * Advances the session to the next card in the list.
     */
    public void moveToNext() {
        currentIndex++;
    }

    /**
     * Calculates how many cards are left to be reviewed, including the current one.
     *
     * @return The number of remaining cards.
     */
    public int getRemainingCount() {
        return loadedCards.size() - currentIndex;
    }

    /**
     * Returns the total number of cards that were loaded for this session.
     *
     * @return The total card count.
     */
    public int getTotalCount() {
        return loadedCards.size();
    }
}
