package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Modifies the content of an existing flashcard in the knowledge base.
 */
public class EditCommand extends Command {
    private final int id;
    private final String newQuestion;
    private final String newAnswer;

    /**
     * Prepares an edit operation for a specific card.
     *
     * @param id          The ID of the card to modify.
     * @param newQuestion The new question text, or null to keep the current one.
     * @param newAnswer   The new answer text, or null to keep the current one.
     */
    public EditCommand(int id, String newQuestion, String newAnswer) {
        this.id = id;
        this.newQuestion = newQuestion;
        this.newAnswer = newAnswer;
    }

    /**
     * Updates the card's details and saves the changes.
     * If a field is not provided, the original value is retained.
     *
     * @param cards   The knowledge base containing the card.
     * @param ui      The interface to show the updated card info.
     * @param storage The system to save the modified knowledge base.
     * @param session The current application session.
     * @throws CardNotFoundException If the card ID does not exist in the
     *                               collection.
     */
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card old = cards.getCardById(id);

        String updatedQuestion = (newQuestion != null) ? newQuestion : old.getQuestion();
        String updatedAnswer = (newAnswer != null) ? newAnswer : old.getAnswer();

        Card edited = new Card(old.getId(), updatedQuestion, updatedAnswer, old.getTag());

        cards.deleteCard(id);
        cards.addCard(edited);
        storage.save(cards);
        ui.showEditedMessage(edited);
    }
}