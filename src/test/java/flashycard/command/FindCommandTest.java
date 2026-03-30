package flashycard.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import flashycard.context.SessionContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class FindCommandTest {
    private KnowledgeBase kb;
    private Ui ui;
    private Storage storage;
    private ByteArrayOutputStream outputStream;
    private SessionContainer session;

    @BeforeEach
    public void setUp() {
        kb = new KnowledgeBase();
        ui = new Ui();
        storage = new Storage("dummy.txt");
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        kb.addCard(new Card(1, "What is the capital of France?", "Paris", "Geography"));
        kb.addCard(new Card(2, "Who wrote the Odyssey?", "Homer", "History"));
        kb.addCard(new Card(3, "Is Paris in Europe?", "Yes", "Geography"));
    }

    @Test
    public void execute_globalSearch_findsInQuestionAndAnswer() {
        FindCommand command = new FindCommand("Paris", null);
        command.execute(kb, ui, storage, session);

        String output = outputStream.toString();
        assertTrue(output.contains("Found 2 card(s)"), "Should find two matches for 'Paris'");
        assertTrue(output.contains("capital of France"), "Should find Card 1");
        assertTrue(output.contains("Is Paris in Europe"), "Should find Card 3");
    }

    @Test
    public void execute_questionScope_findsOnlyInQuestion() {
        FindCommand command = new FindCommand("Paris", "q");
        command.execute(kb, ui, storage, session);

        String output = outputStream.toString();
        assertTrue(output.contains("Found 1 card(s)"), "Should only find 1 match in question scope");
        assertTrue(output.contains("Is Paris in Europe"), "Card 3 should be present");
        assertTrue(!output.contains("capital of France"), "Card 1 should be filtered out");
    }

    @Test
    public void execute_answerScope_findsOnlyInAnswer() {
        FindCommand command = new FindCommand("Paris", "a");
        command.execute(kb, ui, storage, session);

        String output = outputStream.toString();
        assertTrue(output.contains("Found 1 card(s)"), "Should only find 1 match in answer scope");
        assertTrue(output.contains("capital of France"), "Card 1 should be present");
    }

    @Test
    public void execute_caseInsensitive_findsMatches() {
        FindCommand command = new FindCommand("fRaNcE", null);
        command.execute(kb, ui, storage, session);

        String output = outputStream.toString();
        assertTrue(output.contains("Found 1 card(s)"), "Search should be case-insensitive");
        assertTrue(output.contains("France"));
    }

    @Test
    public void execute_noMatches_showsEmptyMessage() {
        FindCommand command = new FindCommand("nonexistent", null);
        command.execute(kb, ui, storage, session);

        String output = outputStream.toString();
        assertTrue(output.contains("No cards found"), "Should inform user when no matches exist");
    }
}
