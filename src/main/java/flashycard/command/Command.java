package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.exceptions.CardNotFoundException;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * Represents an executable command in the application.
 * All specific command types must inherit from this class and implement the
 * execute method.
 */
public abstract class Command {

    /**
     * Runs the command using the provided application components.
     *
     * @param cards   The collection of flashcards to operate on.
     * @param ui      The interface used to communicate with the user.
     * @param storage The system used to save or load data.
     * @param session The current state and settings of the user session.
     * @throws CardNotFoundException If the command tries to access a card that does
     *                               not exist.
     */
    public abstract void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
            throws CardNotFoundException;

    /**
     * Checks if this command should terminate the application.
     *
     * @return true if the app should close, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}