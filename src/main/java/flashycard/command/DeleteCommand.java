package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Handles the removal of a specific flashcard from the knowledge base using its
 * ID.
 */
public class DeleteCommand extends Command {
    private int cardId;

    /**
     * Creates a DeleteCommand for the card identified by the given ID.
     *
     * @param id The unique identifier of the card to be deleted.
     */
    public DeleteCommand(int id) {
        cardId = id;
    }

    /**
     * Removes the card from the knowledge base and displays a message.
     *
     * @param cards   The collection containing the cards.
     * @param ui      The interface to show the deletion result.
     * @param storage The storage component (unused in this specific operation).
     * @param session The current application session.
     * @throws CardNotFoundException If no card exists with the specified ID.
     */
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card deletedCard = cards.deleteCard(cardId);
        ui.showDeletedMessage(deletedCard);
        storage.save(cards);
    }
}
