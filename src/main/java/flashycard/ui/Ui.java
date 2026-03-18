package flashycard.ui;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import java.util.Scanner;
import java.util.Collection;

public class Ui {
    private Scanner scanner;
    private static final String DIVIDER = "------------------------------------------------";
    private static final String PREFIX = "> ";

    public Ui()
    {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println("   FlashyCard - Your Command Line Study Buddy");
        System.out.println(DIVIDER);
        System.out.println("Available commands: add, list, view, flip, delete, exit");
    }

    public String readCommand() {
        System.out.print(PREFIX);
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    public void showAddedMessage(Card card) {
        System.out.println("Got it. I've added this card:");
        System.out.println("  [" + card.getId() + "] Q: " + card.getQuestion());
    }

    public void showQuestion(Card card) {
        System.out.println("Card #" + card.getId());
        System.out.println("Question: " + card.getQuestion());
    }

    public void showAnswer(Card card) {
        System.out.println("Answer: " + card.getAnswer());
    }

    public void showDeletedMessage(Card card) {
        System.out.println("Noted. I've removed card #" + card.getId());
    }

    public void showList(KnowledgeBase knowledgeBase) {
        Collection<Card> cards = knowledgeBase.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("Your knowledge base is currently empty.");
        } else {
            System.out.println("Here are the cards in your collection:");
            for (Card card : cards) {
                System.out.println(card.getId() + ": " + card.getQuestion());
            }
            System.out.println("Total: " + knowledgeBase.getSize() + " cards.");
        }
    }

    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    public void showExitMessage() {
        System.out.println("Bye! Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
