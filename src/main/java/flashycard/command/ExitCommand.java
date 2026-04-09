package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Signals the application to shut down.
 */
public class ExitCommand extends Command {

    /**
     * Performs no specific action, as the primary purpose of this command
     * is handled by the isExit method.
     *
     * @param cards   The knowledge base.
     * @param ui      The user interface.
     * @param storage The data storage.
     * @param session The current session.
     * @throws CardNotFoundException Not expected to be thrown by this command.
     */
    @Override
    public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException {
        // No action required on execution.
    }

}