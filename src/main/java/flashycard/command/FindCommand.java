package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Searches for flashcards that contain a specific keyword within a chosen
 * scope.
 */
public class FindCommand extends Command {
    private final String keyword;
    private final String scope;

    /**
     * Prepares a search with a keyword and a specific area to look in.
     *
     * @param keyword The text to search for (case-insensitive).
     * @param scope   The area to search: "q" for questions, "a" for answers, or
     *                both.
     */
    public FindCommand(String keyword, String scope) {
        this.keyword = keyword.toLowerCase();
        this.scope = scope;
    }

    /**
     * Filters the cards based on the keyword and scope, then displays the results.
     *
     * @param hb      The knowledge base to search through.
     * @param ui      The interface to display the found cards.
     * @param storage The storage component.
     * @param session The current application session.
     */
    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session) {
        List<Card> results = hb.getAllCards().stream()
                .filter(card -> {
                    boolean inQuestion = card.getQuestion().toLowerCase().contains(keyword);
                    boolean inAnswer = card.getAnswer().toLowerCase().contains(keyword);

                    if ("q".equals(scope)) {
                        return inQuestion;
                    }
                    if ("a".equals(scope)) {
                        return inAnswer;
                    }
                    return inQuestion || inAnswer;
                })
                .collect(Collectors.toList());

        ui.showSearchResults(results, keyword);
    }

    /**
     * Gets the keyword used for the search.
     *
     * @return The lowercase search keyword.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Gets the scope of the search.
     *
     * @return The scope identifier ("q", "a", or null/other for both).
     */
    public String getScope() {
        return scope;
    }
}