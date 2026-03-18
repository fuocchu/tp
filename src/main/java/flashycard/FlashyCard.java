package flashycard;

import flashycard.command.Command;
import flashycard.exceptions.CorruptedDataException;
import flashycard.model.KnowledgeBase;
import flashycard.parser.Parser;
import flashycard.storage.Storage;
import flashycard.ui.Ui;

public class FlashyCard {
    private Ui ui;
    private Storage storage;
    private KnowledgeBase knowledgeBase;

    public FlashyCard(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
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

                // If readCommand returns null, the input file is finished
                if (fullCommand == null) {
                    break;
                }

                if (fullCommand.contains("Input redirection is not supported")) {
                    continue;
                }

                if (fullCommand.trim().isEmpty()) {
                    continue;
                }

                Command c = Parser.parse(fullCommand);
                c.execute(knowledgeBase, ui, storage);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage() + ": [" + fullCommand + "]");
            }
        }
        ui.showExitMessage();
    }

    public static void main(String[] args) {
        new FlashyCard("data/flashcards.txt").run();
    }
}
