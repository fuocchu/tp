package flashycard.command;

import flashycard.context.SessionContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

import flashycard.exceptions.CardNotFoundException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class FlipCommandTest {
    private FlipCommand flipCommand;
    private KnowledgeBase knowledgeBase;
    private Ui ui;
    private Storage storage;
    private SessionContainer session;

    @BeforeEach
    public void setUp() {
        knowledgeBase = new KnowledgeBase();
        ui = new Ui();
        storage = null;
    }

    @Test
    public void execute_validCard_success() throws CardNotFoundException {
        Card testCard = new Card(1, "Question", "Answer", "Tag1" );
        knowledgeBase.addCard(testCard);
        flipCommand = new FlipCommand(1);
        flipCommand.execute(knowledgeBase, ui, storage, session);
    }

    @Test
    public void execute_invalidCard_throwsCardNotFoundException() {
        flipCommand = new FlipCommand(999);
        assertThrows(CardNotFoundException.class, () -> flipCommand.execute(knowledgeBase, ui, storage, session));
    }

    @Test
    public void constructor_validId_success() {
        flipCommand = new FlipCommand(5);
        assertNotNull(flipCommand);
    }

    @Test
    public void isExit_returnsFalse() {
        FlipCommand command = new FlipCommand(1);
        assertFalse(command.isExit());
    }
}
