package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import java.util.List;
import java.util.ArrayList;

public class RemoveCommand extends Command {
    private final List<Integer> cardIds;
    private final String setName;

    public RemoveCommand(List<Integer> cardIds, String setName) {
        this.cardIds = cardIds;
        this.setName = setName;
    }

    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {

        if (!hb.getAllTestSets().containsKey(setName)) {
            ui.showError("Test set '" + setName + "' does not exist.");
            return;
        }

        if (cardIds == null) {
            int count = hb.getAllTestSets().get(setName).size();
            hb.addTestSet(setName, new ArrayList<>());
            storage.save(hb);
            System.out.println("Cleared all " + count + " card(s) from set [" + setName + "].");
        } else {
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

    @Override
    public boolean isExit() {
        return false;
    }
}
