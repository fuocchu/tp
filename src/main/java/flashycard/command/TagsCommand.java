package flashycard.command;

import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import java.util.Set;

public class TagsCommand extends Command {

    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage) {
        Set<String> tags = hb.getUniqueTags();
        ui.showTagsList(tags);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
