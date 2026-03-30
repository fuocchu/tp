package flashycard.model;

import java.util.Collection;
import java.util.HashMap;

import flashycard.exceptions.CardNotFoundException;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.List;

public class KnowledgeBase {
    private HashMap<Integer, Card> cards;
    private final HashMap<String, java.util.List<Integer>> testSets = new HashMap<>();

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

    public void saveToTestSet(String setName, java.util.List<Integer> ids) {
        List<Integer> currentSet = testSets.computeIfAbsent(setName, k -> new java.util.ArrayList<>());

        for (Integer id : ids) {
            if (!currentSet.contains(id)) {
                currentSet.add(id);
            }
        }
    }

    public java.util.Map<String, java.util.List<Integer>> getAllTestSets() {
        return testSets;
    }

    public void addTestSet(String name, java.util.List<Integer> ids) {
        testSets.put(name, ids);
    }

    public void removeCardFromSet(String setName, int cardId) throws CardNotFoundException {
        if (!testSets.containsKey(setName)) {
            throw new CardNotFoundException("Test set '" + setName + "' not found.");
        }

        List<Integer> ids = testSets.get(setName);

        if (!ids.contains(cardId)) {
            throw new CardNotFoundException("Card ID " + cardId + " is not in set [" + setName + "].");
        }

        ids.remove(Integer.valueOf(cardId));
    }
}
