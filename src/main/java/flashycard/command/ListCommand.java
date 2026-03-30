package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import java.util.ArrayList;
import java.util.List;

public class ListCommand extends Command {
    private final String setName;

    public ListCommand(String setName) {
        this.setName = setName;
    }

    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session) {
        List<Card> cardsToShow = new ArrayList<>();

        if (setName == null) {
            cardsToShow.addAll(hb.getAllCards());
        } else {
            List<Integer> ids = hb.getAllTestSets().get(setName);
            if (ids == null) {
                ui.showError("Test set '" + setName + "' does not exist.");
                return;
            }
            for (int id : ids) {
                try {
                    cardsToShow.add(hb.getCardById(id));
                } catch (Exception e) {
                    System.err.println("Note: Card #" + id + " no longer exists.");
                }
            }
        }
        session.setLastSearchResults(cardsToShow);

        if (setName != null) {
            System.out.println("Cards in set [" + setName + "]:");
        }
        ui.showSearchResults(cardsToShow, setName == null ? "all" : setName);
    }
}
