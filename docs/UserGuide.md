# FlashyCard User Guide

FlashyCard is a high efficiency memorization application optimized for the **Command Line Interface (CLI)**. If you are a fast typer who prefers terminal-based workflows, FlashyCard is designed for you.

---

## Table of Contents

1. [Quick Start](#quick-start)
2. [Tutorial: Your First Study Session](#tutorial-your-first-study-session)
3. [Features](#features)
  - [add](#adding-a-flashcard-add)
  - [list](#listing-flashcards-list)
  - [view](#viewing-a-question-view)
  - [flip](#flipping-a-flashcard-flip)
  - [edit](#editing-a-flashcard-edit)
  - [tag](#tagging-a-flashcard-tag)
  - [tags](#viewing-all-tags-tags)
  - [find](#finding-flashcards-find)
  - [save](#saving-to-a-test-set-save)
  - [test](#starting-a-test-session-test)
  - [remove](#removing-cards-from-a-test-set-remove)
  - [delete](#deleting-a-flashcard-delete)
  - [exit](#exiting-the-program-exit)
4. [Data Management](#data-management)
5. [Command Summary](#command-summary)

---

## Quick Start

1. **Prerequisites:** Ensure you have **Java 17** or above installed on your computer.
2. **Download:** Get the latest `.jar` file from our release page.
3. **Setup:** Copy the file to the folder you wish to use as the home directory for your data.
4. **Launch:** Open a terminal, `cd` into that folder, and run:
   ```
   java -jar FlashyCard.jar
   ```
5. **Execute:** Type any command and press **Enter**.

---

## Tutorial: Your First Study Session

This tutorial walks you through a complete workflow from scratch.
Follow along step by step, by the end you will have created cards, organised them, and completed a test session.

> **Note on card IDs:** Card IDs are assigned by an auto incrementing counter that never resets. If you are a returning user who has previously added or deleted cards, your IDs will not start from 1. **Always run `list` first** to see your actual card IDs, and substitute accordingly when the tutorial references a specific ID like `1`, `2`, or `3`.

---

### Step 1 — Add some flashcards

Let's start by building a small knowledge base. Type each command below and press Enter:

```
add q/What is Java? a/A general-purpose, object-oriented programming language
add q/What is a compiler? a/A program that translates source code into machine code
add q/What is an algorithm? a/A step-by-step procedure to solve a problem
add q/What is RAM? a/Random Access Memory, used for temporary data storage
add q/What does OOP stand for? a/Object-Oriented Programming
```

You should see a confirmation message after each one, with the card's assigned ID.

---

### Step 2 — Browse your cards

Now check what you have and note down the IDs assigned:

```
list
```

You will see all 5 cards with their IDs, questions, and tags (all `none` for now). **Use these IDs** in the steps below instead of assuming they start from 1.

Sample output:

```
Listed all 5 cards:
[ID: 1] Q: What is Java? | Tag: none
[ID: 2] Q: What is a compiler? | Tag: none
[ID: 3] Q: What is an algorithm? | Tag: none
[ID: 4] Q: What is RAM? | Tag: none
[ID: 5] Q: What does OOP stand for? | Tag: none
```

---

### Step 3 — Test your recall manually

Pick a card and try to recall the answer before flipping (replace `1` with your actual first card ID):

```
view 1
```

This shows only the question. Think of the answer, then reveal it:

```
flip 1
```

Try this for a few cards to get a feel for the flow.

---

### Step 4 — Fix a mistake with `edit`

Suppose you want to improve card 3's answer (use your actual ID):

```
edit 3 a/A finite set of well-defined instructions to solve a specific problem
```

Only the answer changes. The question and tag stay as they were. Verify with:

```
flip 3
```

---

### Step 5 — Organise cards with tags

Tag your cards by topic so you can filter them later (use your actual IDs):

```
tag 1 t/java
tag 2 t/java
tag 5 t/java
tag 3 t/general
tag 4 t/hardware
```

Check all the tags you have used:

```
tags
```

Sample output:

```
All tags: [general, hardware, java]
```

---

### Step 6 — Find related cards

Search for all cards related to Java using `list`, then save the results:

```
list
```

Sample output for `list`:

```
Listed all 5 cards:
[ID: 1] Q: What is Java? | Tag: java
[ID: 2] Q: What is a compiler? | Tag: java
...
```

> **Important:** Use `list` (not `find`) before `save all`. The `find` command displays results but does not store them for `save all`. Only `list` and `test` results can be used with `save all`.

To search by keyword and display matching cards:

```
find q/java
```

---

### Step 7 — Build a test set for revision

Run `list` first (to populate the saved results), then save to a named test set:

```
list
save all s/java-revision
```

Or save a specific card directly by ID without needing to list first:

```
save 3 s/java-revision
```

Check the set:

```
list s/java-revision
```

Sample output:

```
Cards in set [java-revision]:
[ID: 1] Q: What is Java? | Tag: java
[ID: 2] Q: What is a compiler? | Tag: java
...
```

---

### Step 8 — Run a test session

Now quiz yourself:

```
test java-revision
```

FlashyCard will walk you through each card in the set, showing questions one at a time.
Press Enter to flip each card and reveal the answer. A score is shown at the end.

---

### Step 9 — Clean up the set

If a card no longer belongs in the set (use your actual ID):

```
remove 3 s/java-revision
```

Or clear everything and start fresh:

```
remove all s/java-revision
```

---

### Step 10 — Exit

When you are done:

```
exit
```

All your cards and test sets are automatically saved. When you relaunch the app, everything will be right where you left it.

---

## Features

### Command Format Notes

* Words in `UPPER_CASE` are parameters to be supplied by the user.
* Parameters in square brackets `[...]` are optional.
* At least one optional parameter must be provided where applicable.
* Commands that do not take any arguments (such as `tags` and `exit`) will return an error if extra arguments are provided.

---

### Adding a Flashcard: `add`

Adds a new flashcard to your knowledge base.

* **Format:** `add q/QUESTION a/ANSWER`
* **Example:** `add q/What is 2+2? a/4`

**Sample output:**

```
Added card #1: Q: What is 2+2? | A: 4 | Tag: none
```

**Error behaviour:**
* If `q/` or `a/` is missing → `ERROR: Invalid argument format given for add command`

---

### Listing Flashcards: `list`

Shows all cards in the knowledge base, or all cards in a specific test set. Also stores the results so they can be used with `save all`.

* **Format:** `list` or `list s/SET_NAME`
* **Example:** `list s/mySet`

**Sample output (`list`):**

```
Listed all 3 cards:
[ID: 1] Q: What is Java? | Tag: none
[ID: 2] Q: What is OOP? | Tag: science
[ID: 3] Q: What is 2+2? | Tag: none
```

**Sample output (`list s/mySet`):**

```
Cards in set [mySet]:
[ID: 1] Q: What is Java? | Tag: none
[ID: 2] Q: What is OOP? | Tag: science
```

**Error behaviour:**
* If the named set does not exist → `ERROR: Test set 'mySet' does not exist.`

---

### Viewing a Question: `view`

Displays the question side of a specific card to test your recall.

* **Format:** `view ID`
* **Example:** `view 2`

**Sample output:**

```
Q: What is OOP?
```

**Error behaviour:**
* If the card ID does not exist → `ERROR: Card with given ID cannot be found in the knowledge base`
* If the ID is not a number → `ERROR: Invalid ID given: ID must be a number`

---

### Flipping a Flashcard: `flip`

Reveals the answer side of a specific flashcard.

* **Format:** `flip ID`
* **Example:** `flip 2`

**Sample output:**

```
A: Object Oriented Programming
```

**Error behaviour:**
* If the card ID does not exist → `ERROR: Card with given ID cannot be found in the knowledge base`
* If the ID is not a number → `ERROR: Invalid ID given: ID must be a number`

---

### Editing a Flashcard: `edit`

Edits the question, answer, or both fields of an existing card. The tag is always preserved.

* **Format:** `edit ID [q/QUESTION] [a/ANSWER]`
* At least one of `q/` or `a/` must be provided.
* **Examples:**
  * `edit 1 q/What is Python? a/A scripting language` — edit both
  * `edit 1 q/What is Python?` — edit question only
  * `edit 1 a/A scripting language` — edit answer only

**Sample output:**

```
Edited card #1: Q: What is Python? | A: A scripting language | Tag: none
```

**Error behaviour:**
* If neither `q/` nor `a/` is provided → `ERROR: Edit command requires at least q/QUESTION or a/ANSWER.`
* If the card ID does not exist → `ERROR: Card with given ID cannot be found in the knowledge base`

---

### Tagging a Flashcard: `tag`

Assigns a category tag to a card.

* **Format:** `tag ID t/TAG`
* **Example:** `tag 1 t/science`

**Sample output:**

```
Tagged card #1 with [science]
```

**Error behaviour:**
* If the card ID does not exist → `ERROR: Card with given ID cannot be found in the knowledge base`

---

### Viewing All Tags: `tags`

Lists all unique tags currently used in the knowledge base, in alphabetical order.

* **Format:** `tags`

**Sample output:**

```
All tags: [general, hardware, java, science]
```

**Error behaviour:**
* If extra arguments are provided → `ERROR: Invalid argument format given for tags command`

---

### Finding Flashcards: `find`

Searches for cards whose question or answer contains the given keyword. The search is case-insensitive. Results are displayed but **are not stored** for use with `save all` — use `list` before `save all` instead.

* **Format:** `find [q/|a/]KEYWORD`
* Omit the scope prefix to search both question and answer.
* **Examples:**
  * `find java` — search both fields
  * `find q/java` — search questions only
  * `find a/language` — search answers only

**Sample output (`find q/java`):**

```
Found 2 card(s) matching "java" in questions:
[ID: 1] Q: What is Java? | Tag: none
[ID: 5] Q: What does OOP stand for? | Tag: none
```

> **Note:** `find` displays results but does not store them for `save all`. To save results to a test set, run `list` first, then `save all s/SET_NAME`.

**Error behaviour:**
* If the keyword is empty → `ERROR: Keyword cannot be empty.`

---

### Saving to a Test Set: `save`

Saves a card or the last `list` results to a named test set. Cards already in the set are not duplicated.

* **Format:** `save all s/SET_NAME` or `save ID s/SET_NAME`
* Run `list` first before using `save all` (not `find` — see note above).
* **Examples:**
  * `save all s/mySet`
  * `save 1 s/mySet`

**Sample output:**

```
Saved 3 card(s) to set [mySet]
```

**Error behaviour:**
* If `save all` is used without a prior `list` or `test` → `ERROR: No previous search results found to save. Try 'find' or 'list' first.`
* If the card ID does not exist → `ERROR: Card ID X does not exist in the knowledge base.`

---

### Starting a Test Session: `test`

Runs an interactive quiz session for all cards in a test set. Shows questions one at a time and reveals answers on Enter. Displays a score at the end.

* **Format:** `test SET_NAME`
* **Example:** `test mySet`

**Sample session:**

```
Starting test session for set: [mySet]
Card 1/3
Q: What is Java?
[Press Enter to flip]
A: A general-purpose, object-oriented programming language
---
Card 2/3
Q: What is OOP?
[Press Enter to flip]
A: Object Oriented Programming
---
...
Session complete! You reviewed 3 card(s).
```

**Error behaviour:**
* If the set does not exist → `ERROR: Test set 'mySet' does not exist. Create it first using 'save'.`
* If the set is empty → `ERROR: Test set 'mySet' is empty.`

---

### Removing Cards from a Test Set: `remove`

Removes one or more cards from a test set, or clears the entire set.

* **Format:** `remove all s/SET_NAME` or `remove ID [ID ...] s/SET_NAME`
* **Examples:**
  * `remove all s/mySet`
  * `remove 1 2 3 s/mySet`

**Sample output:**

```
Removed 1 card(s) from set [mySet].
```

**Error behaviour:**
* If the set does not exist → `ERROR: Test set 'mySet' does not exist.`
* If a card ID is not in the set → `ERROR: Could not remove #X: Card ID X is not in set [mySet].`

---

### Deleting a Flashcard: `delete`

Permanently removes a flashcard from the knowledge base. Changes are automatically saved to disk.

* **Format:** `delete ID`
* **Example:** `delete 2`

**Sample output:**

```
Deleted card #2: Q: What is OOP? | A: Object Oriented Programming | Tag: science
```

**Error behaviour:**
* If the card ID does not exist → `ERROR: Cannot delete card: Card with given ID cannot be found in the knowledge base`
* If the ID is not a number → `ERROR: Invalid ID given: ID must be a number`

---

### Exiting the Program: `exit`

Safely closes the application.

* **Format:** `exit`

---

## Data Management

### Saving Data

FlashyCard data is **automatically saved** to your hard disk after every command that changes your data: `add`, `edit`, `tag`, `delete`, `save`, and `remove`. There is no need to save manually.

### Data File Location

Your data is stored in `data/flashcards.txt` in the same folder as the JAR file. The file is plain text and human-readable.

### Editing the Data File Directly

> **Warning:** Advanced users may edit `data/flashcards.txt` directly, but the format must be followed exactly. Incorrect formatting will cause FlashyCard to detect corrupted data and start with an empty knowledge base on next launch.

### FAQ

**Q: How do I transfer my data to another computer?**  
**A:** Install the app on the new computer, then copy the entire `data/` folder from your previous computer into the same directory as the JAR file.

**Q: I accidentally deleted a card. Can I undo it?**  
**A:** There is no undo feature. We recommend keeping a backup of your `data/` folder periodically.

**Q: Can two cards have the same question?**  
**A:** Yes. FlashyCard does not check for duplicate content — each card is uniquely identified by its numeric ID only.

**Q: My card IDs do not start from 1 — is that normal?**  
**A:** Yes. Card IDs are assigned by a counter that never resets between sessions. If you have previously added and deleted cards, new cards will continue from where the counter left off.

### Known Issues

* **Data Corruption:** May occur if the data file is manually edited with invalid formatting, or if the program is closed forcefully mid-save. We recommend making regular backups of your `data/` folder.

---

## Command Summary

| Action | Format | Example |
|--------|--------|---------|
| **Add** | `add q/QUESTION a/ANSWER` | `add q/What is 2+2? a/4` |
| **List** | `list [s/SET_NAME]` | `list s/mySet` |
| **View** | `view ID` | `view 1` |
| **Flip** | `flip ID` | `flip 1` |
| **Edit** | `edit ID [q/QUESTION] [a/ANSWER]` | `edit 1 q/New Q a/New A` |
| **Tag** | `tag ID t/TAG` | `tag 1 t/science` |
| **Tags** | `tags` | `tags` |
| **Find** | `find [q/ or a/]KEYWORD` | `find q/java` |
| **Save** | `save (all or ID) s/SET_NAME` | `save all s/mySet` |
| **Test** | `test SET_NAME` | `test mySet` |
| **Remove** | `remove (all or ID) s/SET_NAME` | `remove 1 2 s/mySet` |
| **Delete** | `delete ID` | `delete 1` |
| **Exit** | `exit` | `exit` |