package flashycard.model;

import java.util.Collection;
import java.util.HashMap;

import flashycard.exceptions.CardNotFoundException;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class KnowledgeBase {
    private HashMap<Integer, Card> cards;

    public KnowledgeBase() {
        this.cards = new HashMap<>();
    }

    public void addCard(Card card) {
        assert hasCard(card.getId()) == false : "Should not have duplicated ids";
        cards.put(card.getId(), card);
    }

    public Card getCardById(int id) throws CardNotFoundException {

        if (!hasCard(id)) {
            throw new CardNotFoundException("Card with given ID cannot be found in the knowledge base");
        }

        return cards.get(id);

    }

    public Card deleteCard(int id) throws CardNotFoundException {

        if (!hasCard(id)) {
            throw new CardNotFoundException(
                    "Cannot delete card:  Card with given ID cannot be found in the knowledge base");
        }

        return cards.remove(id);
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

    public Set<String> getUniqueTags() {
        return cards.values().stream()
                .map(Card::getTag)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
