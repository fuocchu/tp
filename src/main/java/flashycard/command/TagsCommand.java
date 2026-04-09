package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import java.util.Set;

/**
 * Lists all unique tags currently assigned to flashcards in the knowledge base.
 */
public class TagsCommand extends Command {

    /**
     * Collects every unique tag from the flashcards and passes the list to the UI
     * for display.
     *
     * @param hb      The knowledge base containing all cards and their tags.
     * @param ui      The user interface used to show the list of tags.
     * @param storage The storage component.
     * @param session The current application session.
     */
    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session) {
        Set<String> tags = hb.getUniqueTags();
        ui.showTagsList(tags);
    }

}