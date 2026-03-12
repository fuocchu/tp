package seedu.duke;

import java.io.IOException;

public class Storage {

    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public FlashcardList load() throws IOException {
        return null;
    }

    public void save(FlashcardList flashcards) throws IOException {

    }
}
