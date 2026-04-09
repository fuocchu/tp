package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Displays the question side of a specific flashcard.
 */
public class ViewCommand extends Command {
    private int cardId;

    /**
     * Prepares a command to view the card with the given ID.
     *
     * @param id The ID of the card to be displayed.
     */
    public ViewCommand(int id) {
        cardId = id;
    }

    /**
     * Retrieves the card from the knowledge base and shows its question via the UI.
     *
     * @param cards   The knowledge base containing the cards.
     * @param ui      The user interface used to display the card.
     * @param storage The storage component.
     * @param session The current application session.
     * @throws CardNotFoundException If no card exists with the specified ID.
     */
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card selectedCard = cards.getCardById(cardId);
        ui.showQuestion(selectedCard);
    }
}