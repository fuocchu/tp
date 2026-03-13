package flashycard.command;

import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class DummyCommand extends Command {
    public DummyCommand(Object... args) {
    }

    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage) {

    }
}
