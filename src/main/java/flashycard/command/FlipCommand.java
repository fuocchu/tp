package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Handles the action of revealing the answer side of a specific flashcard.
 */
public class FlipCommand extends Command {
    private int cardId;

    /**
     * Creates a FlipCommand for the card with the specified ID.
     *
     * @param id The ID of the card to flip.
     */
    public FlipCommand(int id) {
        cardId = id;
    }

    /**
     * Retrieves the card from the knowledge base and displays its answer to the
     * user.
     *
     * @param cards   The collection containing the cards.
     * @param ui      The interface to display the card's answer.
     * @param storage The storage component.
     * @param session The current application session.
     * @throws CardNotFoundException If no card exists with the provided ID.
     */
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card selectedCard = cards.getCardById(cardId);
        ui.showAnswer(selectedCard);
    }
}