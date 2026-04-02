package flashycard.parser;

import java.util.regex.Matcher;

import flashycard.command.Command;
import flashycard.command.EditCommand;
import flashycard.exceptions.InvalidArgumentException;

public class EditCommandParser extends CommandParser {
    private static final String EDIT_REGEX =
            "(?<id>\\d+)(?:\\s+q/(?<question>.+?)(?=\\s+a/|$))?(?:\\s+a/(?<answer>.+))?";

    public EditCommandParser() {
        super("edit", EDIT_REGEX);
    }

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
        String answer   = matcher.group("answer");

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
