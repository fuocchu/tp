package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import java.util.List;
import java.util.ArrayList;

/**
 * Removes one or more cards from a specific test set, or clears the set
 * entirely.
 */
public class RemoveCommand extends Command {
    private final List<Integer> cardIds;
    private final String setName;

    /**
     * Prepares a command to remove specific cards or clear a set.
     *
     * @param cardIds The list of card IDs to remove; if null, the entire set is
     *                cleared.
     * @param setName The name of the test set to modify.
     */
    public RemoveCommand(List<Integer> cardIds, String setName) {
        this.cardIds = cardIds;
        this.setName = setName;
    }

    /**
     * Executes the removal logic by updating the test set in the knowledge base.
     *
     * @param hb      The knowledge base containing the sets and cards.
     * @param ui      The user interface for error reporting.
     * @param storage The storage system to persist the changes.
     * @param session The current application session.
     * @throws CardNotFoundException If a specified card is not found during
     *                               individual removal.
     */
    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {

        if (!hb.getAllTestSets().containsKey(setName)) {
            ui.showError("Test set '" + setName + "' does not exist.");
            return;
        }

        if (cardIds == null) {
            // Case where the user wants to clear the whole set
            int count = hb.getAllTestSets().get(setName).size();
            hb.addTestSet(setName, new ArrayList<>());
            storage.save(hb);
            System.out.println("Cleared all " + count + " card(s) from set [" + setName + "].");
        } else {
            // Case where specific cards are being removed
            int successCount = 0;
            for (int id : cardIds) {
                try {
                    hb.removeCardFromSet(setName, id);
                    successCount++;
                } catch (CardNotFoundException e) {
                    ui.showError("Could not remove #" + id + ": " + e.getMessage());
                }
            }

            if (successCount > 0) {
                storage.save(hb);
                System.out.println("Removed " + successCount + " card(s) from set [" + setName + "].");
            }
        }
    }

}