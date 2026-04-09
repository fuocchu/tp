package flashycard.ui;

import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import flashycard.model.StudySession;
import java.util.Scanner;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Handles all user interactions through the command-line interface.
 * Responsible for formatting output, displaying messages, and managing
 * the interactive study session loop.
 */
public class Ui {
    private static final String DIVIDER = "------------------------------------------------";
    private static final String PREFIX = "> ";
    private Scanner scanner;

    /**
     * Initializes the UI and prepares a Scanner to read standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the application welcome banner and a brief command overview.
     */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println("   FlashyCard - Your Command Line Study Buddy");
        System.out.println(DIVIDER);
        System.out.println("Available commands: add, list, view, flip, tag, find, save, test, delete, exit");
    }

    /**
     * Runs an interactive study session where the user is shown questions,
     * reveals answers, and self-reports correctness to get a final score.
     *
     * @param cards The list of cards to be studied in this session.
     */
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

    /**
     * Prompts the user for input and reads a full line from the console.
     *
     * @return The trimmed command string, or null if no input is available.
     */
    public String readCommand() {
        System.out.print(PREFIX);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Confirms the addition of a new flashcard.
     *
     * @param card The card that was added.
     */
    public void showAddedMessage(Card card) {
        System.out.println("Got it. I've added this card:");
        System.out.println("  [" + card.getId() + "] Q: " + card.getQuestion() + " [" + card.getTag() + "]");
    }

    /**
     * Displays the question of a specific card.
     *
     * @param card The card to display.
     */
    public void showQuestion(Card card) {
        System.out.println("Card #" + card.getId());
        System.out.println("Question: " + card.getQuestion());
    }

    /**
     * Displays the answer of a specific card.
     *
     * @param card The card to display.
     */
    public void showAnswer(Card card) {
        System.out.println("Answer: " + card.getAnswer());
    }

    /**
     * Confirms the deletion of a flashcard.
     *
     * @param card The card that was removed.
     */
    public void showDeletedMessage(Card card) {
        System.out.println("Noted. I've removed card #" + card.getId());
    }

    /**
     * Displays the updated details of a card after an edit.
     *
     * @param card The modified card.
     */
    public void showEditedMessage(Card card) {
        System.out.println("Got it. I've updated card #" + card.getId() + ":");
        System.out.println("  Q: " + card.getQuestion() + " [" + card.getTag() + "]");
        System.out.println("  A: " + card.getAnswer());
    }

    /**
     * Lists all flashcards currently in the knowledge base.
     *
     * @param knowledgeBase The source of the cards.
     */
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

    /**
     * Confirms that a tag has been successfully added to a card.
     *
     * @param card The card that was tagged.
     */
    public void showTaggedMessage(Card card) {
        System.out.println("Tag added to card #" + card.getId() + ": [" + card.getTag() + "]");
    }

    /**
     * Lists all unique tags and categories used across the knowledge base.
     *
     * @param tags A set of unique tag strings.
     */
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

    /**
     * Displays the results of a search operation.
     *
     * @param results The list of cards matching the keyword.
     * @param keyword The keyword used for searching.
     */
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

    /**
     * Confirms that cards have been saved to a named test set.
     *
     * @param setName The name of the test set.
     * @param count   The number of cards added to the set.
     */
    public void showSaveSetSuccess(String setName, int count) {
        System.out.println("Successfully saved " + count + " card(s) to test set: [" + setName + "]");
    }

    /**
     * Lists all available test sets and the number of cards in each.
     *
     * @param testSets A map of test set names to their list of card IDs.
     */
    public void showTestSets(Map<String, List<Integer>> testSets) {
        if (testSets.isEmpty()) {
            System.out.println("No saved test sets found.");
            return;
        }
        System.out.println("Available Test Sets:");
        testSets.forEach((name, ids) -> System.out.println(" - " + name + " (" + ids.size() + " cards)"));
    }

    /**
     * Displays the header for a new study session.
     *
     * @param sessionName The name of the session.
     * @param total       The total number of cards to review.
     */
    public void showSessionHeader(String sessionName, int total) {
        System.out.println(DIVIDER);
        System.out.println("Starting Session: " + sessionName);
        System.out.println("Total cards to review: " + total);
        System.out.println("Type any key to see the answer, then 'y' for correct or 'n' for incorrect.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the current progress within a study session.
     *
     * @param current The index of the current card.
     * @param total   The total number of cards in the session.
     */
    public void showSessionProgress(int current, int total) {
        System.out.print("[" + current + "/" + total + "] ");
    }

    /**
     * Displays the final results and score at the end of a study session.
     *
     * @param correct The number of cards the user got right.
     * @param total   The total number of cards reviewed.
     */
    public void showSessionResult(int correct, int total) {
        System.out.println(DIVIDER);
        System.out.println("Session Complete!");
        System.out.println("Score: " + correct + "/" + total);
        System.out.println(DIVIDER);
    }

    /**
     * Displays a generic informational message.
     *
     * @param msg The message string.
     */
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Displays an error message formatted to stand out to the user.
     *
     * @param message The error description.
     */
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Displays the shutdown message when the user exits the application.
     */
    public void showExitMessage() {
        System.out.println("Bye! Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
