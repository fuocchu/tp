package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Initiates a study session based on a specific named test set.
 */
public class TestCommand extends Command {
    private final String setName;

    /**
     * Creates a TestCommand for a specific set of cards.
     *
     * @param setName The name of the test set to use for the session.
     */
    public TestCommand(String setName) {
        this.setName = setName.trim();
    }

    /**
     * Gets the name of the test set associated with this command.
     *
     * @return The test set name.
     */
    public String getSetName() {
        return this.setName;
    }

    /**
     * Retrieves the cards in the specified set and begins an interactive study
     * session.
     *
     * @param hb      The knowledge base containing the cards and sets.
     * @param ui      The user interface to handle the study interaction.
     * @param storage The storage component.
     * @param session The current session to be updated with the test cards.
     */
    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session) {
        List<Integer> ids = hb.getAllTestSets().get(setName);

        if (ids == null) {
            ui.showError("Test set '" + setName + "' does not exist. Create it first using 'save'.");
            return;
        }

        if (ids.isEmpty()) {
            ui.showError("Test set '" + setName + "' is empty.");
            return;
        }

        List<Card> testCards = new ArrayList<>();
        for (int id : ids) {
            if (hb.hasCard(id)) {
                try {
                    testCards.add(hb.getCardById(id));
                } catch (CardNotFoundException e) {
                    System.err.println("Warning: Card ID " + id + " disappeared during set retrieval.");
                }
            }
        }

        session.setLastSearchResults(testCards);

        if (ui != null) {
            ui.showMessage("Starting test session for set: [" + setName + "]");
            ui.startStudySession(testCards);
        }
    }

}