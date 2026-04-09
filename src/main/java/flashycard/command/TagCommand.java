package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import flashycard.exceptions.CardNotFoundException;

/**
 * Assigns or updates a tag for a specific flashcard.
 */
public class TagCommand extends Command {
    private final int id;
    private final String tag;

    /**
     * Creates a command to apply a tag to a card.
     *
     * @param id  The ID of the card to be tagged.
     * @param tag The tag string to assign to the card.
     */
    public TagCommand(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    /**
     * Updates the card with the new tag by replacing the old card entry,
     * then saves the changes and confirms with the user.
     *
     * @param cards   The knowledge base containing the cards.
     * @param ui      The interface to show the tagging confirmation.
     * @param storage The storage system to save the updated card.
     * @param session The current application session.
     * @throws CardNotFoundException If the specified card ID does not exist.
     */
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