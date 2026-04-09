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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the persistence of flashcard data and test sets to a local file.
 * It manages file initialization, serialization of objects into a text-based
 * format,
 * and restoration of the KnowledgeBase state upon application startup.
 */
public class Storage {

    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private String filePath;

    /**
     * Initializes the storage component. Creates the target file and any
     * necessary parent directories if they do not exist.
     *
     * @param filePath The path to the file where data will be stored.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isBlank() : "File path should not be null or empty";

        this.filePath = filePath;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    logger.info("Creating directories for storage...");
                    parentDir.mkdirs();
                }
                logger.info("Creating storage file at: " + filePath);
                file.createNewFile();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing storage", e);
            throw new RuntimeException("Error initializing storage: " + e.getMessage(), e);
        }
    }

    /**
     * Serializes the current KnowledgeBase into the storage file.
     * Escapes pipe characters within card data to prevent parsing errors.
     *
     * @param knowledgeBase The knowledge base containing cards and test sets to
     *                      save.
     */
    public void save(KnowledgeBase knowledgeBase) {
        assert knowledgeBase != null : "KnowledgeBase should not be null";

        Collection<Card> cards = knowledgeBase.getAllCards();
        var testSets = knowledgeBase.getAllTestSets();

        logger.info("Saving " + cards.size() + " cards and " + testSets.size() + " test sets to storage...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Card card : cards) {
                assert card != null : "Card should not be null";

                String question = card.getQuestion().replace("|", "\\|");
                String answer = card.getAnswer().replace("|", "\\|");
                String tag = card.getTag().replace("|", "\\|");
                String line = card.getId() + "|" + question + "|" + answer + "|" + tag;

                writer.write(line);
                writer.newLine();
            }

            for (java.util.Map.Entry<String, java.util.List<Integer>> entry : testSets.entrySet()) {
                String setName = entry.getKey().replace("|", "\\|");

                String ids = entry.getValue().stream()
                        .map(String::valueOf)
                        .collect(java.util.stream.Collectors.joining(","));

                writer.write("SET:" + setName + "|" + ids);
                writer.newLine();
            }

            logger.info("Save completed successfully.");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving data to file: " + filePath, e);
            throw new RuntimeException("Error saving data: " + e.getMessage(), e);
        }
    }

    /**
     * Reads the storage file and reconstructs the KnowledgeBase.
     *
     * @return A KnowledgeBase populated with the data from the file.
     * @throws CorruptedDataException If the file contains malformed data or cannot
     *                                be read.
     */
    public KnowledgeBase load() throws CorruptedDataException {
        KnowledgeBase kb = new KnowledgeBase();

        logger.info("Loading data from storage...");

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                if (line.startsWith("SET:")) {
                    parseAndAddTestSet(line, kb);
                    continue;
                }

                String[] parts = line.split("(?<!\\\\)\\|", 4);

                if (parts.length < 3) {
                    logger.warning("Corrupted line at " + lineNumber + ": " + line);
                    throw new CorruptedDataException("Invalid line in storage file: " + line);
                }

                int id;
                try {
                    id = Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid ID at line " + lineNumber);
                    throw new CorruptedDataException("Invalid card id: " + parts[0]);
                }

                String question = parts[1].replace("\\|", "|");
                String answer = parts[2].replace("\\|", "|");
                String tag = (parts.length == 4) ? parts[3].replace("\\|", "|") : "none";

                Card card = new Card(id, question, answer, tag);
                kb.addCard(card);
            }

            logger.info("Loaded " + kb.getSize() + " cards successfully.");

        } catch (IOException | NumberFormatException e) {
            logger.log(Level.SEVERE, "Failed to load storage file", e);
            throw new CorruptedDataException("Failed to load storage file: " + e.getMessage());
        }

        return kb;
    }

    /**
     * Parses a line representing a test set and adds it to the KnowledgeBase.
     *
     * @param line The raw line from the storage file starting with "SET:".
     * @param kb   The KnowledgeBase to populate.
     */
    private void parseAndAddTestSet(String line, KnowledgeBase kb) {
        String content = line.substring(4);
        String[] parts = content.split("(?<!\\\\)\\|");
        String setName = parts[0].replace("\\|", "|");

        java.util.List<Integer> ids = new java.util.ArrayList<>();

        if (parts.length > 1 && !parts[1].isBlank()) {
            ids = java.util.Arrays.stream(parts[1].split(","))
                    .map(Integer::parseInt)
                    .collect(java.util.stream.Collectors.toList());
        }

        kb.addTestSet(setName, ids);
    }

}