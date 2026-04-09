package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of flashcards, either from the entire collection or a
 * specific set.
 */
public class ListCommand extends Command {
    private final String setName;

    /**
     * Prepares a command to list cards.
     *
     * @param setName The name of the specific set to list, or null to list all
     *                cards.
     */
    public ListCommand(String setName) {
        this.setName = setName;
    }

    /**
     * Gathers the requested cards, updates the session's last search results,
     * and tells the UI to display them.
     *
     * @param hb      The knowledge base containing all cards and sets.
     * @param ui      The interface used to show the list to the user.
     * @param storage The system for data persistence.
     * @param session The current session, updated with the cards displayed.
     */
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