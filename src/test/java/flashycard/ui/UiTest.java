
package flashycard.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

import flashycard.model.Card;

public class UiTest {
    private Ui ui;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ui = new Ui();
    }

    @Test
    public void testShowWelcome() {
        ui.showWelcome();
    }

    @Test
    public void testShowAddedMessage() {
        Card card = new Card(1, "What is abc?", "123", "alphabet");
        ui.showAddedMessage(card);
        String output = outputStream.toString();
        assertTrue(output.contains("1"));
    }

    @Test
    public void testShowQuestion() {
        Card card = new Card(2, "What is abc?", "123", "alphabet");
        ui.showQuestion(card);
        String output = outputStream.toString();
        assertTrue(output.contains("2"));
        assertTrue(output.contains("What is abc?"));
    }

    @Test
    public void testShowAnswer() {
        Card card = new Card(2, "What is abc?", "123", "alphabet");
        ui.showAnswer(card);
        String output = outputStream.toString();
        assertTrue(output.contains("123"));
    }

    @Test
    public void testShowDeletedMessage() {
        Card card = new Card(2, "What is abc?", "123", "alphabet");
        ui.showDeletedMessage(card);
        String output = outputStream.toString();
        assertTrue(output.contains("2"));
    }

    @Test
    public void testShowTaggedMessage() {
        Card card = new Card(1, "Question", "Answer", "Science");
        ui.showTaggedMessage(card);

        String output = outputStream.toString();
        assertTrue(output.contains("#1"), "Output should contain the card ID");
        assertTrue(output.contains("Science"), "Output should contain the new tag name");
    }

    @Test
    public void testShowList_showsTags() {
        flashycard.model.KnowledgeBase kb = new flashycard.model.KnowledgeBase();
        kb.addCard(new Card(1, "Java?", "Yes", "Coding"));

        ui.showList(kb);
        String output = outputStream.toString();
        assertTrue(output.contains("Coding"), "The list view should display the card's tag");
    }

    @Test
    public void testShowError() {
        ui.showError("Invalid command");
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid command"));
    }

    @Test
    public void testShowExitMessage() {
        ui.showExitMessage();
        String output = outputStream.toString();
        assertTrue(output.contains("Bye! Hope to see you again soon!"));
    }
}
