package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class FlipCommand extends Command {
    private int cardId;

    public FlipCommand(int id) {
        cardId = id;
    }

    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card selectedCard = cards.getCardById(cardId);
        ui.showAnswer(selectedCard);
    }
}
