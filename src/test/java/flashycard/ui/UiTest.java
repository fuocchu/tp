
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
        Card card = new Card(1, "What is abc?", "123");
        ui.showAddedMessage(card);
        String output = outputStream.toString();
        assertTrue(output.contains("1"));
    }

    @Test
    public void testShowQuestion() {
        Card card = new Card(2, "What is abc?", "123");
        ui.showQuestion(card);
        String output = outputStream.toString();
        assertTrue(output.contains("2"));
        assertTrue(output.contains("What is abc?"));
    }

    @Test
    public void testShowAnswer() {
        Card card = new Card(2, "What is abc?", "123");
        ui.showAnswer(card);
        String output = outputStream.toString();
        assertTrue(output.contains("123"));
    }

    @Test
    public void testShowDeletedMessage() {
        Card card = new Card(2, "What is abc?", "123");
        ui.showDeletedMessage(card);
        String output = outputStream.toString();
        assertTrue(output.contains("2"));
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
