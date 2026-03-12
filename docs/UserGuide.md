# FlashyCard User Guide

FlashyCard is a high-efficiency memorization application optimized for the **Command Line Interface (CLI)**. If you are a fast typer who prefers terminal-based workflows, FlashyCard is designed for you.

---

## Quick Start

1. **Prerequisites:** Ensure you have **Java 17** or above installed on your computer.
2. **Download:** Get the latest `.jar` file from our release page.
3. **Setup:** Copy the file to the folder you wish to use as the "home" directory for your data.
4. **Launch:** Open a terminal, `cd` into that folder, and run:
`java -jar FlashyCard.jar`
5. **Execute:** Type your command in the command box and press **Enter**.

### Example Commands

* `list`: Lists all flashcards.
* `add q/What is the capital of France? a/Paris`: Adds a new card.
* `view 1`: Displays the question for card ID 1.
* `flip 1`: Reveals the answer for card ID 1.
* `delete 1`: Deletes card ID 1.
* `exit`: Closes the application.

---

## Features

### Command Format Notes

* Words in `UPPER_CASE` are parameters to be supplied by the user.
* Extraneous parameters for commands that do not require them (like `list` and `exit`) will be ignored.

### Adding a Flashcard: `add`

Adds a new flashcard to your knowledge base.

* **Format:** `add q/QUESTION a/ANSWER`
* **Example:** `add q/What is 2+2? a/4`

### Listing All Flashcards: `list`

Shows a complete list of all cards currently in the knowledge base.

* **Format:** `list`

### Viewing a Question: `view`

Displays the question side of a specific card to test your recall.

* **Format:** `view ID`
* **Example:** `view 2` (Shows the question of the flashcard with ID 2).

### Flipping a Flashcard: `flip`

Reveals the answer side of a specific flashcard to verify your recall.

* **Format:** `flip ID`
* **Example:** `flip 2` (Shows the answer of the flashcard with ID 2).

### Deleting a Flashcard: `delete`

Removes the specified flashcard from the knowledge base.

* **Format:** `delete ID`
* **Example:** `delete 2`

### Exiting the Program: `exit`

Safely closes the application.

* **Format:** `exit`

---

## Data Management

### Saving Data

FlashyCard are **automatically saved** to your hard disk after any `add`, `edit`, or `delete` command.

### FAQ

**Q: How do I transfer my data to another computer?**
**A:** Install the app on the new computer, then copy the folder containing your data from the previous computer to the new one.

### Known Issues

* **Data Corruption:** May occur if the underlying data file is manually edited with invalid formatting or if the program is closed improperly. We recommend making regular backups of your data folder.

---

## Command Summary

| Action | Format | Examples |
| --- | --- | --- |
| **Add** | `add q/QUESTION a/ANSWER` | `add q/2312*113112312=? a/idk` |
| **List** | `list` | `list` |
| **View** | `view ID` | `view 1` |
| **Flip** | `flip ID` | `flip 1` |
| **Delete** | `delete ID` | `delete 1` |
| **Exit** | `exit` | `exit` |
