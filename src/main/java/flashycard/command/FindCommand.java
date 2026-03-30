package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

import java.util.List;
import java.util.stream.Collectors;

public class FindCommand extends Command {
    private final String keyword;
    private final String scope;

    public FindCommand(String keyword, String scope) {
        this.keyword = keyword.toLowerCase();
        this.scope = scope;
    }

    @Override
    public void execute(KnowledgeBase hb, Ui ui, Storage storage, SessionContainer session) {
        List<Card> results = hb.getAllCards().stream()
                .filter(card -> {
                    boolean inQuestion = card.getQuestion().toLowerCase().contains(keyword);
                    boolean inAnswer = card.getAnswer().toLowerCase().contains(keyword);

                    if ("q".equals(scope)){
                        return inQuestion;
                    }
                    if ("a".equals(scope)) {
                        return inAnswer;
                    }
                    return inQuestion || inAnswer;
                }
                )
                .collect(Collectors.toList());

        ui.showSearchResults(results, keyword);
    }
    public String getKeyword() {
        return keyword;
    }
    public String getScope() {
        return scope;
    }
}
