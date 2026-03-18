package flashycard.command;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class AddCommand extends Command {
    private final String question;
    private final String answer;

    public AddCommand(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage) {
        Card card = new Card(question, answer);
        cards.addCard(card);
        storage.save(cards);
        ui.showAddedMessage(card);
    }
}
