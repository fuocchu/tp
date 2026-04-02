package flashycard;

import flashycard.command.Command;
import flashycard.context.SessionContainer;
import flashycard.exceptions.CorruptedDataException;
import flashycard.model.KnowledgeBase;
import flashycard.parser.Parser;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class FlashyCard {
    private Ui ui;
    private Storage storage;
    private KnowledgeBase knowledgeBase;
    private SessionContainer sessionContainer;

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

    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = null;
            try {
                fullCommand = ui.readCommand();

                if (fullCommand == null) {
                    break;
                }


                String cleanedCommand = fullCommand.replaceFirst("^[^a-zA-Z0-9]+", "").trim();

                if (cleanedCommand.isEmpty() || cleanedCommand.toLowerCase().contains("redirection")) {
                    continue;
                }

                Command c = Parser.parse(cleanedCommand);
                c.execute(knowledgeBase, ui, storage, sessionContainer);
                isExit = c.isExit();
            } catch (Exception e) {
                String hex = (fullCommand.length() > 0) ? Integer.toHexString(fullCommand.charAt(0)) : "empty";
                ui.showError(e.getMessage() + " (CharHex: " + hex + "): [" + fullCommand + "]");
            }
        }
        ui.showExitMessage();
    }

    public static void main(String[] args) {
        java.util.logging.LogManager.getLogManager().reset();
        new FlashyCard("data/flashcards.txt").run();
    }
}
