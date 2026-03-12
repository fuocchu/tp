package flashycard.storage;

import flashycard.exceptions.CorruptedDataException;
import flashycard.model.KnowledgeBase;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void save(KnowledgeBase knowledgeBase) {
    }

    public KnowledgeBase load() throws CorruptedDataException {
        return null;
    }
}
