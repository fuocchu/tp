package flashycard.command;

import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class ExitCommand extends Command {
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage) {
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
