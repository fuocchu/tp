package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import flashycard.exceptions.CardNotFoundException;

public class TagCommand extends Command {
    private final int id;
    private final String tag;

    public TagCommand(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card oldCard = cards.getCardById(id);
        Card taggedCard = new Card(oldCard.getId(), oldCard.getQuestion(), oldCard.getAnswer(), tag);

        cards.deleteCard(id);
        cards.addCard(taggedCard);

        storage.save(cards);
        ui.showTaggedMessage(taggedCard);
    }
}
