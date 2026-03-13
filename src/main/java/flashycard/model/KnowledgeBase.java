package flashycard.model;

import java.util.Collection;
import java.util.HashMap;

public class KnowledgeBase {
    private HashMap<Integer, Card> cards;

    public KnowledgeBase() {
        this.cards = new HashMap<>();
    }

    public void addCard(Card card) {
        cards.put(card.getId(), card);
    }

    public Card getCardById(int id) {
        return cards.get(id);
    }

    public void deleteCard(int id) {
        cards.remove(id);
    }

    public int getSize() {
        return cards.size();
    }

    public boolean hasCard(int id) {
        return cards.containsKey(id);
    }

    public Collection<Card> getAllCards() {
        return cards.values();
    }
}
