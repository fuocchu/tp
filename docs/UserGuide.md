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
Follow along step by step — by the end you will have created cards, organised them, and completed a test session.

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

Now check what you have:

```
list
```

You will see all 5 cards with their IDs, questions, and tags (all `none` for now).

---

### Step 3 — Test your recall manually

Pick a card and try to recall the answer before flipping:

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

Suppose you want to improve card 3's answer:

```
edit 3 a/A finite set of well-defined instructions to solve a specific problem
```

Only the answer changes. The question and tag stay as they were. Verify with:

```
flip 3
```

---

### Step 5 — Organise cards with tags

Tag your cards by topic so you can filter them later:

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

---

### Step 6 — Find related cards

Search for all cards related to Java:

```
find q/java
```

This searches only questions. To search everything:

```
find java
```

---

### Step 7 — Build a test set for revision

After the `find` command, your results are remembered. Save them as a named test set:

```
find java
save all s/java-revision
```

Or save a specific card directly without searching first:

```
save 3 s/java-revision
```

Check the set:

```
list s/java-revision
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

If a card no longer belongs in the set:

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
* Extraneous parameters for commands that do not take arguments (such as `list` and `tags`) will be ignored.

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

Edits the question, answer, or both fields of an existing card. The tag is always preserved.

* **Format:** `edit ID [q/QUESTION] [a/ANSWER]`
* At least one of `q/` or `a/` must be provided.
* **Examples:**
  * `edit 1 q/What is Python? a/A scripting language` — edit both
  * `edit 1 q/What is Python?` — edit question only
  * `edit 1 a/A scripting language` — edit answer only

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

Searches for cards whose question or answer contains the given keyword. The search is case-insensitive.

* **Format:** `find [q/|a/]KEYWORD`
* Omit the scope prefix to search both question and answer.
* **Examples:**
  * `find java` — search both fields
  * `find q/java` — search questions only
  * `find a/language` — search answers only

> **Tip:** Results from `find` are remembered. You can immediately follow up with `save all s/SET_NAME` to capture them into a test set.

---

### Saving to a Test Set: `save`

Saves a card or the last search/list results to a named test set. Cards already in the set are not duplicated.

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

FlashyCard data is **automatically saved** to your hard disk after every command that changes your data (`add`, `edit`, `tag`, `delete`, `save`, `remove`). There is no need to save manually.

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
| **Find** | `find [q/\|a/]KEYWORD` | `find q/java` |
| **Save** | `save all\|ID s/SET_NAME` | `save all s/mySet` |
| **Test** | `test SET_NAME` | `test mySet` |
| **Remove** | `remove all\|ID s/SET_NAME` | `remove 1 2 s/mySet` |
| **Delete** | `delete ID` | `delete 1` |
| **Exit** | `exit` | `exit` |