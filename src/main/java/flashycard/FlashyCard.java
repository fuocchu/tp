package flashycard;

import flashycard.command.Command;
import flashycard.context.SessionContainer;
import flashycard.exceptions.CorruptedDataException;
import flashycard.model.KnowledgeBase;
import flashycard.parser.Parser;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

/**
 * The main controller class for the FlashyCard application. It coordinates the
 * initialization of the UI, storage, and data models, and maintains the primary
 * execution loop for processing user commands.
 */
public class FlashyCard {
    private Ui ui;
    private Storage storage;
    private KnowledgeBase knowledgeBase;
    private SessionContainer sessionContainer;

    /**
     * Initializes the application components and loads existing data from the
     * storage file. If the data file is corrupted, it initializes an empty
     * knowledge base.
     *
     * @param filePath The local path to the data storage file.
     */
    public FlashyCard(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        sessionContainer = new SessionContainer();
        try {
            knowledgeBase = storage.load();
        } catch (CorruptedDataException e) {
            ui.showError("Data file corrupted. starting with an empty base.");
            knowledgeBase = new KnowledgeBase();
        }
    }

    /**
     * Starts the main application loop. Continuously reads user input, cleanses it,
     * parses it into commands, and executes those commands until an exit signal is
     * received.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = null;
            try {
                fullCommand = ui.readCommand();

                if (fullCommand == null) {
                    ui.showError("Please enter a command.");
                    break;
                }

                // Remove non-alphanumeric leading characters often introduced by terminal
                // artifacts
                String cleanedCommand = fullCommand.replaceFirst("^[^a-zA-Z0-9]+", "").trim();

                if (cleanedCommand.isEmpty() || cleanedCommand.toLowerCase().contains("redirection")) {
                    continue;
                }

                Command c = Parser.parse(cleanedCommand);
                c.execute(knowledgeBase, ui, storage, sessionContainer);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage() + " [" + fullCommand + "]");
            }
        }
        ui.showExitMessage();
    }

    /**
     * Entry point for the FlashyCard application. Resets default logging to clean
     * up console output and starts the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        java.util.logging.LogManager.getLogManager().reset();
        new FlashyCard("data/flashcards.txt").run();
    }
}
