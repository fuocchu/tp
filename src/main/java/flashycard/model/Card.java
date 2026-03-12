package flashycard.model;

// designed as immutable class 
public class Card {
    private static int idCounter = 1;

    private final int id;
    private final String question;
    private final String answer;

    public Card(String question, String answer) {
        this.id = idCounter++;
        this.question = question;
        this.answer = answer;
    }

    public Card(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;

        // syncing the internal id from loaded from database
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}