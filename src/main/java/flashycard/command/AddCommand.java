package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Handles the addition of a new flashcard to the knowledge base.
 */
public class AddCommand extends Command {
    private final String question;
    private final String answer;

    /**
     * Creates a new AddCommand with the specified question and answer.
     *
     * @param question The front side of the flashcard.
     * @param answer   The back side of the flashcard.
     */
    public AddCommand(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Creates a new card, adds it to the list, saves the data to storage,
     * and notifies the user.
     *
     * @param cards   The current list of flashcards.
     * @param ui      The user interface for displaying feedback.
     * @param storage The storage handler to persist the new card.
     * @param session The current application session.
     */
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session) {
        Card card = new Card(question, answer);
        cards.addCard(card);
        storage.save(cards);
        ui.showAddedMessage(card);
    }
}