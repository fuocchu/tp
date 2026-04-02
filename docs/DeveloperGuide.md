# Developer Guide

## Acknowledgements

This project is built from scratch without reusing code from other projects.
The command-parser architecture was inspired by [AddressBook Level-3 (AB3)](https://github.com/se-edu/addressbook-level3).

---

## Design and Implementation

### Architecture Overview

FlashyCard follows a layered architecture with these main components:

- `FlashyCard` — entry point, wires all components together
- `Parser` / `CommandParser` — parses user input into `Command` objects
- `Command` — executes actions on the model
- `KnowledgeBase` — in-memory model holding all cards and test sets
- `Storage` — persists and loads data from disk
- `Ui` — handles all user-facing output
- `SessionContainer` — holds transient session state (last search results, active study session)

---

### Storage Component

#### Overview

The `Storage` component is responsible for persisting and retrieving application data from the local file system. It ensures that all `Card` objects and test sets in the `KnowledgeBase` are saved to disk and can be reconstructed when the application restarts.

#### File Format

Each `Card` is serialized as one line:

```
id|question|answer|tag
```

Test sets are serialized as:

```
SET:setName|id1,id2,id3
```

The `|` character inside field values is escaped as `\|` to prevent parsing errors.

#### Save Operation

1. Retrieve all cards from `KnowledgeBase`
2. Escape `|` characters in each field
3. Write each card as `id|question|answer|tag`
4. Retrieve all test sets and write each as `SET:name|id1,id2,...`

#### Load Operation

1. Read file line by line
2. Skip blank lines
3. Lines starting with `SET:` are parsed as test sets
4. All other lines are split into 4 parts (id, question, answer, tag)
5. Validate format; throw `CorruptedDataException` on invalid data
6. Reconstruct `Card` objects and add to `KnowledgeBase`

---

### Command Component

All commands extend the abstract `Command` class and implement:

```java
public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
```

Key commands and their behaviour:

- `AddCommand` — creates a new `Card` and saves
- `EditCommand` — replaces the card with a new immutable `Card` preserving its tag, then saves
- `DeleteCommand` — removes card from `KnowledgeBase` and saves
- `TagCommand` — replaces card with a new `Card` with updated tag, then saves
- `FindCommand` — filters cards by keyword and scope (`q/`, `a/`, or both); stores results in `SessionContainer`
- `ListCommand` — lists all cards or cards in a named test set; stores results in `SessionContainer`
- `SaveCommand` — saves last `SessionContainer` results or a single card ID to a named test set
- `RemoveCommand` — removes cards from a test set or clears it entirely
- `TestCommand` — starts an interactive `StudySession` from a test set

---

### Parser Component

`Parser` holds an array of `CommandParser` instances. Each `CommandParser` defines:

- A command prefix (e.g. `"edit"`)
- A regex pattern for its arguments

`Parser.parse()` matches the command word to the correct parser and delegates. If no match is found, `InvalidCommandException` is thrown.

---

### Model Component

- `Card` — immutable class holding `id`, `question`, `answer`, `tag`
- `KnowledgeBase` — holds a `HashMap<Integer, Card>` and a `HashMap<String, List<Integer>>` for test sets
- `StudySession` — stateful iterator over a list of cards for use during `test` sessions

---

## Product Scope

### Target User Profile

- University or secondary school students who need to memorize large amounts of content
- Users who prefer or are comfortable with CLI tools
- Fast typists who find GUI apps slower for repetitive data entry

### Value Proposition

FlashyCard lets students create, organize, and self-test with flashcards entirely from the terminal — faster than GUI apps for users who type quickly. Test sets allow targeted revision of specific topics without manually filtering cards each time.

---

## User Stories

| Version | As a ...  | I want to ...                         | So that I can ...                           |
|---------|-----------|---------------------------------------|---------------------------------------------|
| v1.0    | new user  | add flashcards with a question/answer | build my knowledge base                     |
| v1.0    | user      | list all my flashcards                | see what I have stored                      |
| v1.0    | user      | view a card's question                | test my recall before flipping              |
| v1.0    | user      | flip a card to see the answer         | verify whether I recalled correctly         |
| v1.0    | user      | delete a card                         | remove outdated or incorrect cards          |
| v2.0    | user      | edit an existing card                 | fix mistakes without deleting and re-adding |
| v2.0    | user      | tag cards with a category             | organise cards by topic                     |
| v2.0    | user      | find cards by keyword                 | locate specific cards quickly               |
| v2.0    | user      | save search results to a test set     | create targeted revision sets               |
| v2.0    | user      | run a test session on a set           | quiz myself on a specific topic             |

---

## Non-Functional Requirements

- Should work on Windows, macOS, and Linux with Java 17 installed.
- All commands should respond within 1 second for a knowledge base of up to 10,000 cards.
- Data must persist across sessions without requiring manual saving.
- The data file should be human-readable for manual inspection or backup.

---

## Glossary

- **Card** — A flashcard consisting of an ID, question, answer, and tag.
- **KnowledgeBase** — The in-memory store of all cards and test sets.
- **Test Set** — A named collection of card IDs used for focused study sessions.
- **Tag** — A category label assigned to a card (default: `none`).
- **SessionContainer** — Holds transient state such as last search results within a single app run.

---

## Instructions for Manual Testing

### Loading Sample Data

To test with pre-existing data, create `data/flashcards.txt` in the same folder as the JAR with this content:

```
1|What is Java?|A programming language|none
2|What is OOP?|Object Oriented Programming|science
3|What is 2+2?|4|none
SET:mySet|1,2
```

Then launch the app — it will load 3 cards and 1 test set automatically.

### Test Cases

**Adding a card**

```
add q/What is Python? a/A scripting language
```

Expected: confirmation message with new card ID.

**Listing all cards**

```
list
```

Expected: all cards shown with their IDs and tags.

**Viewing and flipping**

```
view 1
flip 1
```

Expected: question shown, then answer shown.

**Editing a card**

```
edit 1 q/What is Go? a/A compiled language
edit 2 q/What is Abstraction?
edit 3 a/Four
```

Expected: only specified fields change; tag is preserved.

**Tagging**

```
tag 1 t/programming
tags
```

Expected: card 1 tagged; `tags` shows all unique tags.

**Finding cards**

```
find java
find q/What
find a/Four
```

Expected: matching cards shown.

**Saving to a test set**

```
list
save all s/revision
save 1 s/singles
```

Expected: cards saved to named sets.

**Testing a set**

```
test revision
```

Expected: interactive quiz starts; score shown at end.

**Removing from a set**

```
remove 1 s/revision
remove all s/singles
list s/revision
```

Expected: specified cards removed; `list s/singles` shows empty set.

**Deleting a card**

```
delete 2
list
```

Expected: card 2 no longer appears.

**Persistence check**

Exit and relaunch the app, then run `list` — all remaining cards and sets should still be present.

**Invalid inputs**

```
edit 999 q/Test
edit 1
delete 999
flip 999
```

Expected: descriptive ERROR messages for each.