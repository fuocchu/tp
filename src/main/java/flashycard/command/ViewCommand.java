package flashycard.command;

import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class ViewCommand extends Command {
    private int cardId;

    public ViewCommand(int id) {
        cardId = id;
    }

    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage) throws CardNotFoundException {
        Card selectedCard = cards.getCardById(cardId);
        ui.showQuestion(selectedCard);
    }
}
