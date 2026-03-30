package flashycard.command;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import flashycard.context.SessionContainer;

import java.util.ArrayList;
import java.util.List;

public class SaveCommand extends Command {
    private final String target;
    private final String setName;

    public SaveCommand(String target, String setName) {
        this.target = target.trim().toLowerCase();
        this.setName = setName.trim();
    }

    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session) {
        List<Integer> idsToSave = new ArrayList<>();

        if ("all".equals(target)) {
            List<Card> lastResults = session.getLastSearchResults();

            if (lastResults == null || lastResults.isEmpty()) {
                ui.showError("No previous search results found to save. Try 'find' or 'list' first.");
                return;
            }

            for (Card card : lastResults) {
                idsToSave.add(card.getId());
            }
        } else {
            try {
                int id = Integer.parseInt(target);
                if (hb.hasCard(id)) {
                    idsToSave.add(id);
                } else {
                    ui.showError("Card ID " + id + " does not exist in the knowledge base.");
                    return;
                }
            } catch (NumberFormatException e) {
                ui.showError("Invalid target: '" + target + "'. Use 'all' or a valid numeric ID.");
                return;
            }
        }

        if (idsToSave.isEmpty()) {
            ui.showError("No cards selected to save.");
            return;
        }

        hb.saveToTestSet(setName, idsToSave);

        storage.save(hb);

        ui.showSaveSetSuccess(setName, idsToSave.size());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
