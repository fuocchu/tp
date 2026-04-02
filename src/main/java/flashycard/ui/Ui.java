package flashycard.ui;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.model.StudySession;
import java.util.Scanner;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Ui {
    private static final String DIVIDER = "------------------------------------------------";
    private static final String PREFIX = "> ";
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println("   FlashyCard - Your Command Line Study Buddy");
        System.out.println(DIVIDER);
        System.out.println("Available commands: add, list, view, flip, tag, find, save, test, delete, exit");
    }

    public void startStudySession(List<Card> cards) {
        StudySession session = new StudySession(cards);
        int correctCount = 0;
        int total = session.getTotalCount();

        showSessionHeader("Test Mode", total);

        while (session.hasNext()) {
            Card current = session.getCurrentCard();
            int currentNum = total - session.getRemainingCount() + 1;

            showSessionProgress(currentNum, total);
            System.out.println("Question: " + current.getQuestion());

            System.out.print("Press Enter to see the answer...");
            scanner.nextLine();

            System.out.println("Answer: " + current.getAnswer());
            System.out.print("Did you get it right? (y/n): ");

            String feedback = scanner.nextLine().trim().toLowerCase();
            if (feedback.equals("y")) {
                correctCount++;
                System.out.println("Great job!");
            } else {
                System.out.println("No worries, keep practicing!");
            }

            session.moveToNext();
            System.out.println(DIVIDER);
        }

        showSessionResult(correctCount, total);
    }

    public String readCommand() {
        System.out.print(PREFIX);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    public void showAddedMessage(Card card) {
        System.out.println("Got it. I've added this card:");
        System.out.println("  [" + card.getId() + "] Q: " + card.getQuestion() + " [" + card.getTag() + "]");
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

    public void showEditedMessage(Card card) {
        System.out.println("Got it. I've updated card #" + card.getId() + ":");
        System.out.println("  Q: " + card.getQuestion() + " [" + card.getTag() + "]");
        System.out.println("  A: " + card.getAnswer());
    }

    public void showList(KnowledgeBase knowledgeBase) {
        Collection<Card> cards = knowledgeBase.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("Your knowledge base is currently empty.");
        } else {
            System.out.println("Here are the cards in your collection:");
            for (Card card : cards) {
                System.out.println(card.getId() + ": " + card.getQuestion() + " [" + card.getTag() + "]");
            }
            System.out.println("Total: " + knowledgeBase.getSize() + " cards.");
        }
    }

    public void showTaggedMessage(Card card) {
        System.out.println("Tag added to card #" + card.getId() + ": [" + card.getTag() + "]");
    }

    public void showTagsList(java.util.Set<String> tags) {
        if (tags.isEmpty()) {
            System.out.println("Your knowledge base has no tags.");
            return;
        }
        boolean hasNone = tags.contains("none");
        int categoryCount = hasNone ? tags.size() - 1 : tags.size();

        if (categoryCount > 0) {
            System.out.println("Here are your current categories:");
            int index = 1;
            for (String tag : tags) {
                if (!tag.equals("none")) {
                    System.out.println(index + ". " + tag);
                    index++;
                }
            }
        } else {
            System.out.println("No custom categories created yet.");
        }

        if (hasNone) {
            System.out.println("------------------------------------------------");
            System.out.println(" * Uncategorized cards [none]");
        }

        System.out.println("Total: " + categoryCount + " categories.");
    }

    public void showSearchResults(java.util.List<Card> results, String keyword) {
        if (results.isEmpty()) {
            System.out.println("No cards found matching: '" + keyword + "'");
            return;
        }

        System.out.println("Found " + results.size() + " card(s) matching '" + keyword + "':");
        for (Card card : results) {
            System.out.println(card.getId() + ": Q: " + card.getQuestion() + " | A: " + card.getAnswer());
        }
    }

    public void showSaveSetSuccess(String setName, int count) {
        System.out.println("Successfully saved " + count + " card(s) to test set: [" + setName + "]");
    }

    public void showTestSets(Map<String, List<Integer>> testSets) {
        if (testSets.isEmpty()) {
            System.out.println("No saved test sets found.");
            return;
        }
        System.out.println("Available Test Sets:");
        testSets.forEach((name, ids) ->
                System.out.println(" - " + name + " (" + ids.size() + " cards)")
        );
    }

    public void showSessionHeader(String sessionName, int total) {
        System.out.println(DIVIDER);
        System.out.println("Starting Session: " + sessionName);
        System.out.println("Total cards to review: " + total);
        System.out.println("Type any key to see the answer, then 'y' for correct or 'n' for incorrect.");
        System.out.println(DIVIDER);
    }

    public void showSessionProgress(int current, int total) {
        System.out.print("[" + current + "/" + total + "] ");
    }

    public void showSessionResult(int correct, int total) {
        System.out.println(DIVIDER);
        System.out.println("Session Complete!");
        System.out.println("Score: " + correct + "/" + total);
        System.out.println(DIVIDER);
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    public void showExitMessage() {
        System.out.println("Bye! Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
