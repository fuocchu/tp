package flashycard.model;

import java.util.ArrayList;
import java.util.List;

public class StudySession {
    private final List<Card> loadedCards;
    private int currentIndex;

    public StudySession(List<Card> cards) {
        this.loadedCards = new ArrayList<>(cards);
        this.currentIndex = 0;
    }

    public boolean hasNext() {
        return currentIndex < loadedCards.size();
    }

    public Card getCurrentCard() {
        return loadedCards.get(currentIndex);
    }

    public void moveToNext() {
        currentIndex++;
    }

    public int getRemainingCount() {
        return loadedCards.size() - currentIndex;
    }

    public int getTotalCount() {
        return loadedCards.size();
    }
}
