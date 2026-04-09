package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.Command;
import flashycard.command.EditCommand;
import flashycard.exceptions.InvalidArgumentException;

/**
 * Parses user input specifically for the "edit" command.
 * It allows users to update the question, the answer, or both for an existing
 * card.
 */
public class EditCommandParser extends CommandParser {
    private static final String EDIT_REGEX = "(?<id>\\d+)(?:\\s+q/(?<question>.+?)(?=\\s+a/|$))?(?:\\s+a/(?<answer>.+))?";

    /**
     * Initializes the parser with the "edit" keyword and a regex that captures
     * the ID and optional question/answer updates.
     */
    public EditCommandParser() {
        super("edit", EDIT_REGEX);
    }

    /**
     * Extracts the ID and the new content from the command string.
     * Validates that at least one field (question or answer) is being changed.
     *
     * @param fullCommand The raw input string from the user.
     * @return A new EditCommand instance with the updated details.
     * @throws InvalidArgumentException If the ID is invalid or no updates are
     *                                  provided.
     */
    @Override
    public Command parse(String fullCommand) throws InvalidArgumentException {
        Matcher matcher = this.match(fullCommand);

        int id;
        try {
            id = Integer.parseInt(matcher.group("id").trim());
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid ID given: ID must be a number");
        }

        String question = matcher.group("question");
        String answer = matcher.group("answer");

        if (question != null) {
            question = question.trim();
        }
        if (answer != null) {
            answer = answer.trim();
        }

        if (question == null && answer == null) {
            throw new InvalidArgumentException(
                    "Edit command requires at least q/QUESTION or a/ANSWER. " +
                            "Usage: edit ID [q/QUESTION] [a/ANSWER]");
        }

        return new EditCommand(id, question, answer);
    }
}
