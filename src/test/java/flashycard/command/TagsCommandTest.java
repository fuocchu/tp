package flashycard.command;

import flashycard.context.SessionContainer;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TagsCommandTest {
    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private TagsCommand tagsCommand;
    private SessionContainer session;

    @BeforeEach
    void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        storage = null;
        tagsCommand = new TagsCommand();
    }

    @Test
    void execute_emptyKnowledgeBase_executesWithoutError() {
        tagsCommand.execute(kb, ui, storage, session);
        assertFalse(tagsCommand.isExit());
    }

    @Test
    void execute_withMixedTags_executesWithoutError() {
        kb.addCard(new Card(1, "Q1", "A1", "Java"));
        kb.addCard(new Card(2, "Q2", "A2", "none"));
        kb.addCard(new Card(3, "Q3", "A3", "Python"));

        tagsCommand.execute(kb, ui, storage, session);
        assertFalse(tagsCommand.isExit());
    }

    @Test
    void execute_onlyNoneTag_executesWithoutError() {
        kb.addCard(new Card(1, "Q1", "A1", "none"));

        tagsCommand.execute(kb, ui, storage, session);
        assertFalse(tagsCommand.isExit());
    }

    @Test
    void isExit_returnsFalse() {
        assertFalse(tagsCommand.isExit());
    }
}
