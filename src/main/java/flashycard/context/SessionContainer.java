package flashycard.context;

import flashycard.model.Card;
import flashycard.model.StudySession;
import java.util.List;
import java.util.ArrayList;

public class SessionContainer {
    private StudySession currentSession;
    private List<Card> lastSearchResults;

    public SessionContainer() {

        this.lastSearchResults = new ArrayList<>();
    }

    public void setSession(StudySession s) {
        this.currentSession = s;
    }
    public StudySession getSession() {
        return currentSession;
    }
    public boolean hasActiveSession() {
        return currentSession != null;
    }

    public void setLastSearchResults(List<Card> cards) {
        if (cards == null) {
            this.lastSearchResults = new ArrayList<>();
        } else {
            this.lastSearchResults = new ArrayList<>(cards);
        }
    }

    public List<Card> getLastSearchResults() {
        return lastSearchResults != null ? lastSearchResults : new ArrayList<>();
    }
}
