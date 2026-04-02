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
* `edit 1 q/New question a/New answer`: Edits card ID 1.
* `tag 1 t/science`: Tags card ID 1 with "science".
* `find java`: Finds all cards containing "java".
* `save all s/mySet`: Saves last search results to a test set.
* `test mySet`: Starts a test session for a set.
* `delete 1`: Deletes card ID 1.
* `exit`: Closes the application.

---

## Features

### Command Format Notes

* Words in `UPPER_CASE` are parameters to be supplied by the user.
* Parameters in square brackets are optional e.g. `[q/QUESTION]`.
* At least one optional parameter must be provided where applicable.
* Extraneous parameters for commands that do not require them (like `list` and `exit`) will be ignored.

---

### Adding a Flashcard: `add`

Adds a new flashcard to your knowledge base.

* **Format:** `add q/QUESTION a/ANSWER`
* **Example:** `add q/What is 2+2? a/4`

---

### Listing Flashcards: `list`

Shows all cards in the knowledge base, or all cards in a specific test set.

* **Format:** `list` or `list s/SET_NAME`
* **Example:** `list s/mySet`

---

### Viewing a Question: `view`

Displays the question side of a specific card to test your recall.

* **Format:** `view ID`
* **Example:** `view 2`

---

### Flipping a Flashcard: `flip`

Reveals the answer side of a specific flashcard.

* **Format:** `flip ID`
* **Example:** `flip 2`

---

### Editing a Flashcard: `edit`

Edits the question, answer, or both fields of an existing card. The tag is preserved.

* **Format:** `edit ID [q/QUESTION] [a/ANSWER]`
* At least one of `q/` or `a/` must be provided.
* **Examples:**
    * `edit 1 q/What is Python? a/A scripting language` (edit both)
    * `edit 1 q/What is Python?` (edit question only)
    * `edit 1 a/A scripting language` (edit answer only)

---

### Tagging a Flashcard: `tag`

Assigns a category tag to a card.

* **Format:** `tag ID t/TAG`
* **Example:** `tag 1 t/science`

---

### Viewing All Tags: `tags`

Lists all unique tags currently used in the knowledge base.

* **Format:** `tags`

---

### Finding Flashcards: `find`

Searches for cards whose question or answer contains the given keyword.

* **Format:** `find [q/|a/]KEYWORD`
* Omit the scope prefix to search both question and answer.
* **Examples:**
    * `find java` (search both fields)
    * `find q/java` (search question only)
    * `find a/language` (search answer only)

---

### Saving to a Test Set: `save`

Saves a card or the last search/list results to a named test set.

* **Format:** `save all s/SET_NAME` or `save ID s/SET_NAME`
* Run `list` or `find` first before using `save all`.
* **Examples:**
    * `save all s/mySet`
    * `save 1 s/mySet`

---

### Starting a Test Session: `test`

Runs an interactive quiz session for all cards in a test set.

* **Format:** `test SET_NAME`
* **Example:** `test mySet`

---

### Removing Cards from a Test Set: `remove`

Removes one or more cards from a test set, or clears the entire set.

* **Format:** `remove all s/SET_NAME` or `remove ID [ID ...] s/SET_NAME`
* **Examples:**
    * `remove all s/mySet`
    * `remove 1 2 3 s/mySet`

---

### Deleting a Flashcard: `delete`

Permanently removes a flashcard from the knowledge base.

* **Format:** `delete ID`
* **Example:** `delete 2`

---

### Exiting the Program: `exit`

Safely closes the application.

* **Format:** `exit`

---

## Data Management

### Saving Data

FlashyCard data is **automatically saved** to your hard disk after every `add`, `edit`, `tag`, `delete`, `save`, and `remove` command. There is no need to save manually.

### Data File Location

Your data is stored in `data/flashcards.txt` within the same folder as the JAR file. Each flashcard is saved as a single line in the format `id|question|answer|tag`. Test sets are stored in the same file with the prefix `SET:`.

### Editing the Data File Directly

> **Warning:** The data file is a plain text file. Advanced users may edit it directly, but follow the format exactly. Incorrect formatting will cause FlashyCard to detect corrupted data and start with an empty knowledge base.

### FAQ

**Q: How do I transfer my data to another computer?**
**A:** Install the app on the new computer, then copy the `data/` folder from your previous computer into the same directory as the JAR file.

### Known Issues

* **Data Corruption:** May occur if the data file is manually edited with invalid formatting, or if the program is closed forcefully. We recommend making regular backups of your `data/` folder.

---

## Command Summary

| Action | Format | Example |
| --- | --- | --- |
| **Add** | `add q/QUESTION a/ANSWER` | `add q/What is 2+2? a/4` |
| **List** | `list [s/SET_NAME]` | `list s/mySet` |
| **View** | `view ID` | `view 1` |
| **Flip** | `flip ID` | `flip 1` |
| **Edit** | `edit ID [q/QUESTION] [a/ANSWER]` | `edit 1 q/New Q a/New A` |
| **Tag** | `tag ID t/TAG` | `tag 1 t/science` |
| **Tags** | `tags` | `tags` |
| **Find** | `find [q/\|a/]KEYWORD` | `find q/java` |
| **Save** | `save all\|ID s/SET_NAME` | `save all s/mySet` |
| **Test** | `test SET_NAME` | `test mySet` |
| **Remove** | `remove all\|ID s/SET_NAME` | `remove 1 2 s/mySet` |
| **Delete** | `delete ID` | `delete 1` |
| **Exit** | `exit` | `exit` |