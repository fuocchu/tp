package flashycard.storage;

import flashycard.exceptions.CorruptedDataException;
import flashycard.model.Card;
import flashycard.model.KnowledgeBase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class Storage {

    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing storage: " + e.getMessage(), e);
        }
    }

    public void save(KnowledgeBase knowledgeBase) {
        Collection<Card> cards = knowledgeBase.getAllCards();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Card card : cards) {
                String question = card.getQuestion().replace("|", "\\|");
                String answer = card.getAnswer().replace("|", "\\|");
                String line = card.getId() + "|" + question + "|" + answer;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving data: " + e.getMessage(), e);
        }
    }

    public KnowledgeBase load() throws CorruptedDataException {
        KnowledgeBase kb = new KnowledgeBase();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("(?<!\\\\)\\|", 3);
                if (parts.length != 3) {
                    throw new CorruptedDataException("Invalid line in storage file: " + line);
                }

                int id;
                try {
                    id = Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    throw new CorruptedDataException("Invalid card id: " + parts[0]);
                }

                String question = parts[1].replace("\\|", "|");
                String answer = parts[2].replace("\\|", "|");

                Card card = new Card(id, question, answer);
                kb.addCard(card);
            }
        } catch (IOException e) {
            throw new CorruptedDataException("Failed to load storage file: " + e.getMessage());
        }

        return kb;
    }
}
