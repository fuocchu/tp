package flashycard.ui;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
    }

    public String readCommand() {
        return null;
    }

    public void showAddedMessage(Card card) {
    }

    public void showQuestion(Card card) {
    }

    public void showAnswer(Card card) {
    }

    public void showDeletedMessage(Card card) {
    }

    public void showList(KnowledgeBase knowledgeBase) {
    }

    public void showError(String message) {
    }

    public void showExitMessage() {
    }
}
