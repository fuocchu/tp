package flashycard.context;

import flashycard.model.Card;
import flashycard.model.StudySession;
import java.util.List;
import java.util.ArrayList;

/**
 * Holds the temporary data and state for the current application run. This
 * includes the active study session and the results from the most recent search
 * or list command.
 */
public class SessionContainer {
    private StudySession currentSession;
    private List<Card> lastSearchResults;

    /**
     * Initializes a new session container with an empty search result list.
     */
    public SessionContainer() {
        this.lastSearchResults = new ArrayList<>();
    }

    /**
     * Updates the currently active study session.
     *
     * @param s The StudySession object to track.
     */
    public void setSession(StudySession s) {
        this.currentSession = s;
    }

    /**
     * Retrieves the current study session.
     *
     * @return The active StudySession, or null if no session is running.
     */
    public StudySession getSession() {
        return currentSession;
    }

    /**
     * Checks if the user is currently in an active study session.
     *
     * @return true if a session exists, false otherwise.
     */
    public boolean hasActiveSession() {
        return currentSession != null;
    }

    /**
     * Stores the results of the latest search or list operation.
     *
     * @param cards The list of cards to remember for subsequent commands.
     */
    public void setLastSearchResults(List<Card> cards) {
        if (cards == null) {
            this.lastSearchResults = new ArrayList<>();
        } else {
            this.lastSearchResults = new ArrayList<>(cards);
        }
    }

    /**
     * Retrieves the list of cards from the last search or list operation.
     *
     * @return A list of cards, or an empty list if no search has been performed.
     */
    public List<Card> getLastSearchResults() {
        return lastSearchResults != null ? lastSearchResults : new ArrayList<>();
    }
}
