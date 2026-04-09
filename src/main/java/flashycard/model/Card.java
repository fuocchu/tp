package flashycard.model;

/**
 * Represents a single flashcard with a question, an answer, and an optional
 * tag.
 * This class is designed to be immutable; once a card is created, its data
 * cannot be changed.
 */
public class Card {
    private static int idCounter = 1;

    private final int id;
    private final String question;
    private final String answer;
    private final String tag;

    /**
     * Creates a new flashcard with a unique ID and a default "none" tag.
     *
     * @param question The question text on the front of the card.
     * @param answer   The answer text on the back of the card.
     */
    public Card(String question, String answer) {
        this.id = idCounter++;
        this.question = question;
        this.answer = answer;
        this.tag = "none";
    }

    /**
     * Reconstructs an existing flashcard, typically used when loading data from
     * storage.
     * It also ensures the internal ID counter stays ahead of the highest loaded ID.
     *
     * @param id       The existing unique identifier.
     * @param question The question text.
     * @param answer   The answer text.
     * @param tag      The tag assigned to the card.
     */
    public Card(int id, String question, String answer, String tag) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tag = tag;

        // Ensure the counter doesn't assign an ID that is already in use
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    /**
     * Gets the unique ID of the card.
     *
     * @return The card's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the question on the card.
     *
     * @return The question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets the answer on the card.
     *
     * @return The answer text.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets the tag associated with the card.
     *
     * @return The tag string.
     */
    public String getTag() {
        return tag;
    }
}
