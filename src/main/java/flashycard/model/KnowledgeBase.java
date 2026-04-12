package flashycard.model;

import java.util.Collection;
import java.util.HashMap;

import flashycard.exceptions.CardNotFoundException;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Acts as the central storage for all flashcards and organized test sets. It
 * manages the lifecycle of cards, including adding, retrieving, and deleting
 * them, as well as grouping them into named collections for testing.
 */
public class KnowledgeBase {
    private HashMap<Integer, Card> cards;
    private final HashMap<String, java.util.List<Integer>> testSets = new HashMap<>();

    /**
     * Initializes an empty knowledge base.
     */
    public KnowledgeBase() {
        this.cards = new HashMap<>();
    }

    /**
     * Adds a card to the collection.
     *
     * @param card The flashcard object to be stored.
     */
    public void addCard(Card card) {
        assert hasCard(card.getId()) == false : "Should not have duplicated ids";
        cards.put(card.getId(), card);
    }

    /**
     * Finds a card by its unique ID.
     *
     * @param id The ID of the card to look for.
     * @return The found Card object.
     * @throws CardNotFoundException If the ID does not exist in the collection.
     */
    public Card getCardById(int id) throws CardNotFoundException {

        if (!hasCard(id)) {
            throw new CardNotFoundException("Card with given ID cannot be found in the knowledge base");
        }

        return cards.get(id);

    }

    /**
     * Removes a card from the collection by its ID.
     *
     * @param id The ID of the card to delete.
     * @return The Card object that was removed.
     * @throws CardNotFoundException If the card cannot be found to be deleted.
     */
    public Card deleteCard(int id) throws CardNotFoundException {

        if (!hasCard(id)) {
            throw new CardNotFoundException(
                    "Cannot delete card:  Card with given ID cannot be found in the knowledge base");
        }

        // Remove card from all test sets
        for (java.util.List<Integer> cardIds : testSets.values()) {
            cardIds.remove(Integer.valueOf(id));
        }

        return cards.remove(id);
    }

    /**
     * Returns the total number of cards in the knowledge base.
     *
     * @return The count of cards.
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Checks if a card with the given ID exists.
     *
     * @param id The ID to check.
     * @return true if the card exists, false otherwise.
     */
    public boolean hasCard(int id) {
        return cards.containsKey(id);
    }

    /**
     * Provides access to all flashcards currently in the system.
     *
     * @return A collection of all cards.
     */
    public Collection<Card> getAllCards() {
        return cards.values();
    }

    /**
     * Extracts all unique tags assigned to cards and sorts them alphabetically.
     *
     * @return A sorted set of unique tag strings.
     */
    public Set<String> getUniqueTags() {
        return cards.values().stream().map(Card::getTag).collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Adds specific card IDs to a named test set. If the set doesn't exist, it
     * creates it.
     *
     * @param setName The name of the test set.
     * @param ids     The list of card IDs to add to the set.
     */
    public void saveToTestSet(String setName, java.util.List<Integer> ids) {
        List<Integer> currentSet = testSets.computeIfAbsent(setName, k -> new java.util.ArrayList<>());

        for (Integer id : ids) {
            if (!currentSet.contains(id)) {
                currentSet.add(id);
            }
        }
    }

    /**
     * Returns the map of all test sets and their associated card IDs.
     *
     * @return A map where keys are set names and values are lists of card IDs.
     */
    public java.util.Map<String, java.util.List<Integer>> getAllTestSets() {
        return testSets;
    }

    /**
     * Directly sets or updates a test set with a specific list of IDs.
     *
     * @param name The name of the test set.
     * @param ids  The list of IDs to associate with this name.
     */
    public void addTestSet(String name, java.util.List<Integer> ids) {
        testSets.put(name, ids);
    }

    /**
     * Removes a specific card ID from a named test set.
     *
     * @param setName The name of the set to modify.
     * @param cardId  The ID of the card to remove from the set.
     * @throws CardNotFoundException If the set doesn't exist or the card isn't in
     *                               the set.
     */
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
