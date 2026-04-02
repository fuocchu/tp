package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class EditCommand extends Command {
    private final int id;
    private final String newQuestion;
    private final String newAnswer;

    public EditCommand(int id, String newQuestion, String newAnswer) {
        this.id = id;
        this.newQuestion = newQuestion;
        this.newAnswer = newAnswer;
    }

    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        Card old = cards.getCardById(id);

        String updatedQuestion = (newQuestion != null) ? newQuestion : old.getQuestion();
        String updatedAnswer   = (newAnswer   != null) ? newAnswer   : old.getAnswer();

        Card edited = new Card(old.getId(), updatedQuestion, updatedAnswer, old.getTag());

        cards.deleteCard(id);
        cards.addCard(edited);
        storage.save(cards);
        ui.showEditedMessage(edited);
    }
}
