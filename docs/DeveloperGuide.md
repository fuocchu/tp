# FlashyCard — Developer Guide

---

## Table of Contents

1. [Acknowledgements](#acknowledgements)
2. [Design and Implementation](#design-and-implementation)
    - [Architecture Overview](#architecture-overview)
    - [UI Component](#ui-component)
    - [Parser Component](#parser-component)
    - [Command Component](#command-component)
    - [Model Component](#model-component)
    - [Storage Component](#storage-component)
    - [SessionContainer](#sessioncontainer)
3. [Implementation: Feature Deep-Dives](#implementation-feature-deep-dives)
    - [Add a Card (`add`)](#add-a-card)
    - [Delete a Card (`delete`)](#delete-a-card)
    - [Edit a Card (`edit`)](#edit-a-card)
    - [View a Card (`view`)](#view-a-card)
    - [Flip a Card (`flip`)](#flip-a-card)
    - [Find Cards (`find`)](#find-cards)
    - [List Cards (`list`)](#list-cards)
    - [Save to Test Set (`save`)](#save-to-test-set)
    - [Remove from Test Set (`remove`)](#remove-from-test-set)
    - [Test a Set (`test`)](#test-a-set)
    - [Tag a Card (`tag`)](#tag-a-card)
    - [List All Tags (`tags`)](#list-all-tags)
    - [Exit (`exit`)](#exit)
    - [Storage: Save Operation](#storage-save-operation)
    - [Storage: Load Operation](#storage-load-operation)
4. [Product Scope](#product-scope)
5. [User Stories](#user-stories)
6. [Non-Functional Requirements](#non-functional-requirements)
7. [Glossary](#glossary)
8. [Instructions for Manual Testing](#instructions-for-manual-testing)

---

## Acknowledgements

This project is built from scratch without reusing code from other projects.  
The command-parser architecture was inspired by [AddressBook Level-3 (AB3)](https://github.com/se-edu/addressbook-level3).

The following tools and libraries were used during development:

- [PlantUML](https://plantuml.com/) — for UML diagram generation.
- The NUS CS2113 teaching team for instructional guidance.

---

## Design and Implementation

### Architecture Overview

FlashyCard follows a layered architecture with clear separation of concerns. The diagram below shows all major components and how they interact.

**Architecture Component Diagram:**
![Architecture Componet Diagram.png](diagram/Architecture%20Componet%20Diagram.png)

**Component Relationships:**

The diagram above shows the main components and their relationships:

- **`FlashyCard`** is the entry point. It wires all components together and runs the main input loop.
- **`Ui`** handles all terminal input and output (reading commands, printing messages).
- **`Parser` / `CommandParser`** transforms raw user input strings into executable `Command` objects.
- **`Command`** (abstract base + concrete subclasses) encapsulates executable logic for each user action.
- **`KnowledgeBase`** is the in-memory model holding all `Card` objects and test sets in `HashMap` structures.
- **`Storage`** persists and loads `KnowledgeBase` data to and from disk.
- **`SessionContainer`** holds transient per-session state such as the last search results and the active study session.

**Key Design Principles:**

- **Single Responsibility:** Each component owns exactly one concern. `Ui` never mutates data; `KnowledgeBase` never does I/O.
- **Command Pattern:** All user actions are encapsulated as `Command` objects. `FlashyCard` only calls `c.execute(...)` and never inspects the type of command.
- **Immutable Model Objects:** `Card` is intentionally immutable. Mutations (edit, tag) delete the old card and add a new one.
- **Layered Architecture:** `UI → Logic (Command) → Model (KnowledgeBase) → Storage`.

---

### UI Component

The `Ui` component is responsible for reading user input from `System.in` and displaying all output to `System.out`. It does not contain any business logic.

#### Class Diagram

```plantuml
@startuml UI_ClassDiagram
skinparam classBackgroundColor #D6EAF8
skinparam classBorderColor #2E86C1

class Ui {
  - scanner: Scanner
  + Ui()
  + showWelcome(): void
  + readCommand(): String
  + showAddedMessage(card: Card): void
  + showDeletedMessage(card: Card): void
  + showEditedMessage(card: Card): void
  + showTaggedMessage(card: Card): void
  + showQuestion(card: Card): void
  + showAnswer(card: Card): void
  + showSearchResults(cards: List<Card>, label: String): void
  + showSaveSetSuccess(setName: String, count: int): void
  + showTagsList(tags: Set<String>): void
  + showMessage(msg: String): void
  + showError(msg: String): void
  + showWelcome(): void
  + showExitMessage(): void
  + startStudySession(cards: List<Card>): void
}
@enduml
```

#### How the UI Component Works

The `Ui` class wraps a `java.util.Scanner` that reads from `System.in`. The `FlashyCard.run()` loop calls `ui.readCommand()` to block and wait for each line of input. After a `Command` executes, individual `show*` methods are called to display results. All output ultimately goes to `System.out.println()`.

The `startStudySession()` method is a special interactive loop that drives the `test` command, it walks the user card-by-card through a `List<Card>`, asking them to self evaluate.

#### Sequence Diagram: Main Application Loop

```plantuml
@startuml UI_MainLoop
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":Ui" as UI
participant ":Parser" as P
participant ":Command" as CMD
participant ":KnowledgeBase" as KB
participant ":Storage" as S

User -> FC : launch app
activate FC
FC -> UI : showWelcome()
FC -> S  : load()
S --> FC : knowledgeBase

loop until isExit
  FC -> UI : readCommand()
  UI -> User : prompt ">"
  User -> UI : types command string
  UI --> FC : fullCommand

  FC -> P : parse(fullCommand)
  P --> FC : command : Command

  FC -> CMD : execute(knowledgeBase, ui, storage, session)
  activate CMD
  CMD -> KB : [mutate if needed]
  CMD -> S  : [save if needed]
  CMD -> UI : show*Message(...)
  deactivate CMD

  FC -> CMD : isExit()
  CMD --> FC : boolean
end

FC -> UI : showExitMessage()
deactivate FC
@enduml
```
---

### Parser Component

**API:** `Parser.java`, `CommandParser.java`, and all `XxxCommandParser.java` classes.

The `Parser` component transforms a raw user input string into a concrete `Command` object ready for execution.

#### Architecture

`Parser` holds a static array of `CommandParser` instances, one per supported command. Each `CommandParser` subclass defines:

1. A **command prefix** (e.g., `"add"`, `"edit"`) stored in `MATCH_PREFIX`.
2. A **compiled regex pattern** (`ARGS_REGEX`) built from the prefix and an argument pattern.

`Parser.parse()` extracts the first word of the input, iterates through all parsers until one matches the prefix, then delegates to `CommandParser.parse()` which applies the full regex and extracts named groups.

#### Class Diagram

```plantuml
@startuml Parser_ClassDiagram
skinparam classBackgroundColor #D6EAF8
skinparam classBorderColor #2E86C1
skinparam backgroundColor #FEFEFE

abstract class CommandParser {
  + MATCH_PREFIX: String
  - ARGS_REGEX: Pattern
  + CommandParser(prefix: String, argsRegex: String)
  # match(fullCommand: String): Matcher
  + {abstract} parse(fullCommand: String): Command
}

class Parser {
  - parsers: CommandParser[]
  + {static} parse(fullCommand: String): Command
}

class AddCommandParser
class DeleteCommandParser
class EditCommandParser
class ExitCommandParser
class FindCommandParser
class FlipCommandParser
class ListCommandParser
class RemoveCommandParser
class SaveCommandParser
class TagCommandParser
class TagsCommandParser
class TestCommandParser
class ViewCommandParser

CommandParser <|-- AddCommandParser
CommandParser <|-- DeleteCommandParser
CommandParser <|-- EditCommandParser
CommandParser <|-- ExitCommandParser
CommandParser <|-- FindCommandParser
CommandParser <|-- FlipCommandParser
CommandParser <|-- ListCommandParser
CommandParser <|-- RemoveCommandParser
CommandParser <|-- SaveCommandParser
CommandParser <|-- TagCommandParser
CommandParser <|-- TagsCommandParser
CommandParser <|-- TestCommandParser
CommandParser <|-- ViewCommandParser

Parser o-- CommandParser : "parsers[13]"
@enduml
```

#### Sequence Diagram: Parsing a Command

```plantuml
@startuml Parser_ParseSequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

participant ":FlashyCard" as FC
participant ":Parser" as P
participant ":CommandParser\n(concrete)" as CP
participant ":Command" as CMD

FC -> P : parse("add q/What is Java? a/A language")
activate P

P -> P : extract commandWord = "add"

loop for each parser in parsers[]
  P -> CP : commandWord.equals(MATCH_PREFIX) ?
  alt match found
    P -> CP : parse(fullCommand)
    activate CP
    CP -> CP : match(fullCommand) → Matcher
    CP -> CP : extract named groups
    CP -> CMD : new AddCommand(question, answer)
    CMD --> CP : command
    CP --> P : command
    deactivate CP
  end
end

P --> FC : command : AddCommand

alt no parser matched
  P --> FC : throw InvalidCommandException
end

deactivate P
@enduml
```
---

### Command Component

`Command.java` and all `XxxCommand.java` classes.

All commands extend the abstract `Command` class and override:

```java
public void execute(KnowledgeBase cards, Ui ui, Storage storage, SessionContainer session)
```

The `isExit()` method defaults to `false` and is overridden only by `ExitCommand`.

#### Command Class Hierarchy Diagram

```plantuml
@startuml Command_ClassDiagram
skinparam classBackgroundColor #D6EAF8
skinparam classBorderColor #2E86C1
skinparam backgroundColor #FEFEFE

abstract class Command {
  + {abstract} execute(cards, ui, storage, session): void
  + isExit(): boolean
}

class AddCommand {
  - question: String
  - answer: String
  + execute(...): void
}

class DeleteCommand {
  - cardId: int
  + execute(...): void
}

class EditCommand {
  - id: int
  - newQuestion: String
  - newAnswer: String
  + execute(...): void
}

class ViewCommand {
  - cardId: int
  + execute(...): void
}

class FlipCommand {
  - cardId: int
  + execute(...): void
}

class FindCommand {
  - keyword: String
  - scope: String
  + execute(...): void
  + getKeyword(): String
  + getScope(): String
}

class ListCommand {
  - setName: String
  + execute(...): void
}

class SaveCommand {
  - target: String
  - setName: String
  + execute(...): void
}

class RemoveCommand {
  - cardIds: List<Integer>
  - setName: String
  + execute(...): void
}

class TestCommand {
  - setName: String
  + execute(...): void
  + getSetName(): String
}

class TagCommand {
  - id: int
  - tag: String
  + execute(...): void
}

class TagsCommand {
  + execute(...): void
}

class ExitCommand {
  + execute(...): void
  + isExit(): boolean
}

Command <|-- AddCommand
Command <|-- DeleteCommand
Command <|-- EditCommand
Command <|-- ViewCommand
Command <|-- FlipCommand
Command <|-- FindCommand
Command <|-- ListCommand
Command <|-- SaveCommand
Command <|-- RemoveCommand
Command <|-- TestCommand
Command <|-- TagCommand
Command <|-- TagsCommand
Command <|-- ExitCommand
@enduml
```
---

### Model Component

`Card.java`, `KnowledgeBase.java`, `StudySession.java`

The Model component stores all application data in memory.

#### Class Diagram 

```plantuml
@startuml Model_ClassDiagram
skinparam classBackgroundColor #FCF3CF
skinparam classBorderColor #D4AC0D
skinparam backgroundColor #FEFEFE

class Card {
  - {static} idCounter: int
  - id: int
  - question: String
  - answer: String
  - tag: String
  + Card(question: String, answer: String)
  + Card(id: int, question: String, answer: String, tag: String)
  + getId(): int
  + getQuestion(): String
  + getAnswer(): String
  + getTag(): String
}

note right of Card
  Immutable class.
  First constructor auto-assigns id
  from static counter.
  Second constructor syncs idCounter
  when loading from disk.
end note

class KnowledgeBase {
  - cards: HashMap<Integer, Card>
  - testSets: HashMap<String, List<Integer>>
  + KnowledgeBase()
  + addCard(card: Card): void
  + getCardById(id: int): Card
  + deleteCard(id: int): Card
  + hasCard(id: int): boolean
  + getSize(): int
  + getAllCards(): Collection<Card>
  + getUniqueTags(): Set<String>
  + saveToTestSet(setName: String, ids: List<Integer>): void
  + getAllTestSets(): Map<String, List<Integer>>
  + addTestSet(name: String, ids: List<Integer>): void
  + removeCardFromSet(setName: String, cardId: int): void
}

class StudySession {
  - loadedCards: List<Card>
  - currentIndex: int
  + StudySession(cards: List<Card>)
  + hasNext(): boolean
  + getCurrentCard(): Card
  + moveToNext(): void
  + getRemainingCount(): int
  + getTotalCount(): int
}

KnowledgeBase "1" *-- "*" Card : stores
StudySession "1" o-- "*" Card : iterates over
@enduml
```

#### Design Notes

- **`Card` is immutable.** All fields are `final`. To "edit" a card, the old one is deleted from `KnowledgeBase` and a new one is added in its place (preserving the same `id`).
- **`KnowledgeBase` uses two `HashMap`s.** `cards` maps `Integer id → Card` for O(1) lookup. `testSets` maps `String setName → List<Integer>` for O(1) set retrieval.
- **`StudySession` is stateful.** It wraps a defensive copy of the card list and tracks a `currentIndex` pointer that advances as the user steps through cards.

---

### Storage Component

`Storage.java`

The `Storage` component is responsible for persisting all `Card` objects and test sets to disk, and reloading them on application startup.

#### Class Diagram 

```plantuml
@startuml Storage_ClassDiagram
skinparam classBackgroundColor #D5F5E3
skinparam classBorderColor #1E8449
skinparam backgroundColor #FEFEFE

class Storage {
  - filePath: String
  - logger: Logger
  + Storage(filePath: String)
  + save(knowledgeBase: KnowledgeBase): void
  + load(): KnowledgeBase
  - parseAndAddTestSet(line: String, kb: KnowledgeBase): void
}

class KnowledgeBase

Storage --> KnowledgeBase : reads / produces
@enduml
```

#### File Format

Each `Card` is serialized as one line:

```
id|question|answer|tag
```

Test sets are serialized as:

```
SET:setName|id1,id2,id3
```

The pipe character `|` inside any field value is escaped as `\|` to prevent incorrect splitting.

**Example data file (`data/flashcards.txt`):**

```
1|What is Java?|A programming language|none
2|What is OOP?|Object Oriented Programming|science
3|What is 2+2?|4|none
SET:mySet|1,2
```

---

### SessionContainer

`SessionContainer.java`

`SessionContainer` holds **transient** state that lives for one application run only, it is never persisted to disk.

#### Class Diagram 

```plantuml
@startuml SessionContainer_ClassDiagram
skinparam classBackgroundColor #FADBD8
skinparam classBorderColor #C0392B
skinparam backgroundColor #FEFEFE

class SessionContainer {
  - currentSession: StudySession
  - lastSearchResults: List<Card>
  + SessionContainer()
  + setSession(s: StudySession): void
  + getSession(): StudySession
  + hasActiveSession(): boolean
  + setLastSearchResults(cards: List<Card>): void
  + getLastSearchResults(): List<Card>
}

class StudySession
class Card

SessionContainer o-- StudySession : currentSession
SessionContainer o-- Card : lastSearchResults "*"
@enduml
```

`lastSearchResults` is populated by `FindCommand`, `ListCommand`, and `TestCommand`. It is consumed by `SaveCommand` when the user types `save all s/mySet`.

---

## Implementation: Feature Deep Dives

This section explains each command in detail, including step-by-step walkthrough and sequence diagrams.

---

### Add a Card

**Command syntax:** `add q/QUESTION a/ANSWER`

**Parsed by:** `AddCommandParser` using regex `q/(?<question>.+?)\ba/(?<answer>.+)`

**Executed by:** `AddCommand`

#### Step-by-Step

1. User types `add q/What is Java? a/A programming language`.
2. `Parser.parse()` extracts the command word `"add"` and delegates to `AddCommandParser`.
3. `AddCommandParser` applies the regex, extracts named groups `question` and `answer`, and returns `new AddCommand(question, answer)`.
4. `FlashyCard.run()` calls `c.execute(knowledgeBase, ui, storage, session)`.
5. `AddCommand.execute()` creates a new `Card(question, answer)` — the `Card` constructor auto-assigns the next available `id` from the static counter.
6. The card is added to `KnowledgeBase` via `cards.addCard(card)`.
7. `storage.save(knowledgeBase)` writes the updated state to disk.
8. `ui.showAddedMessage(card)` prints a confirmation to the terminal.

#### Sequence Diagram 

```plantuml
@startuml AddCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":AddCommandParser" as ACP
participant ":AddCommand" as AC
participant ":KnowledgeBase" as KB
participant ":Storage" as S
participant ":Ui" as UI

User -> FC : "add q/What is Java? a/A language"
FC -> ACP : parse(fullCommand)
ACP -> ACP : match regex → extract question, answer
ACP --> FC : new AddCommand(question, answer)

FC -> AC : execute(kb, ui, storage, session)
activate AC
AC -> AC : new Card(question, answer)\n[id = idCounter++]
AC -> KB : addCard(card)
AC -> S  : save(knowledgeBase)
AC -> UI : showAddedMessage(card)
deactivate AC

UI --> User : "Added card #1: ..."
@enduml
```

#### Design Note

`AddCommand` does **not** check for duplicate content, FlashyCard allows multiple cards with the same question. The only uniqueness constraint is the numeric `id`, which is guaranteed by the auto incrementing static counter in `Card`.

---

### Delete a Card

**Command syntax:** `delete ID`

**Parsed by:** `DeleteCommandParser`

**Executed by:** `DeleteCommand`

#### Step-by-Step

1. User types `delete 2`.
2. `DeleteCommandParser` parses the `id` field and creates `new DeleteCommand(2)`.
3. `DeleteCommand.execute()` calls `cards.deleteCard(cardId)`.
4. `KnowledgeBase.deleteCard()` checks that the card exists; if not, throws `CardNotFoundException` which propagates to `FlashyCard.run()` and is shown as an error.
5. If found, the card is removed from the `HashMap` and returned.
6. **Note:** `DeleteCommand` does **not** call `storage.save()` this is a known behaviour where deletions are not immediately persisted unless `storage.save()` is triggered by a subsequent write command.
7. `ui.showDeletedMessage(deletedCard)` prints the confirmation.

#### Sequence Diagram

```plantuml
@startuml DeleteCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":DeleteCommandParser" as DCP
participant ":DeleteCommand" as DC
participant ":KnowledgeBase" as KB
participant ":Ui" as UI

User -> FC : "delete 2"
FC -> DCP : parse("delete 2")
DCP -> DCP : parse id = 2
DCP --> FC : new DeleteCommand(2)

FC -> DC : execute(kb, ui, storage, session)
activate DC

DC -> KB : deleteCard(2)
activate KB
alt card not found
  KB --> DC : throw CardNotFoundException
  DC --> FC : throw CardNotFoundException
  FC -> UI : showError(e.getMessage())
else card found
  KB --> DC : deletedCard : Card
end
deactivate KB

DC -> UI : showDeletedMessage(deletedCard)
deactivate DC

UI --> User : "Deleted card #2: ..."
@enduml
```
---

### Edit a Card

**Command syntax:** `edit ID [q/NEW_QUESTION] [a/NEW_ANSWER]`

**Parsed by:** `EditCommandParser` using regex `(?<id>\d+)(?:\s+q/(?<question>.+?)(?=\s+a/|$))?(?:\s+a/(?<answer>.+))?`

**Executed by:** `EditCommand`

At least one of `q/` or `a/` must be present; omitted fields are preserved from the existing card.

#### Step-by-Step

1. User types `edit 1 q/What is Go? a/A compiled language`.
2. `EditCommandParser` extracts `id=1`, `question="What is Go?"`, `answer="A compiled language"`.
3. `EditCommand.execute()`:
    - Calls `cards.getCardById(1)` to retrieve the existing card.
    - Merges: if `newQuestion != null` use it, else keep `old.getQuestion()`. Same for answer. Tag is **always** preserved.
    - Creates a new immutable `Card(old.getId(), updatedQuestion, updatedAnswer, old.getTag())`.
    - Deletes the old card, adds the new card.
4. Calls `storage.save()` and `ui.showEditedMessage(edited)`.

#### Sequence Diagram 

```plantuml
@startuml EditCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":EditCommandParser" as ECP
participant ":EditCommand" as EC
participant ":KnowledgeBase" as KB
participant ":Storage" as S
participant ":Ui" as UI

User -> FC : "edit 1 q/What is Go? a/A compiled language"
FC -> ECP : parse(fullCommand)
ECP -> ECP : extract id=1, question, answer
ECP --> FC : new EditCommand(1, "What is Go?", "A compiled language")

FC -> EC : execute(kb, ui, storage, session)
activate EC

EC -> KB : getCardById(1)
KB --> EC : old : Card

EC -> EC : updatedQ = newQuestion != null ? newQuestion : old.getQuestion()\nupdatedA = newAnswer != null ? newAnswer : old.getAnswer()
EC -> EC : new Card(old.getId(), updatedQ, updatedA, old.getTag())

EC -> KB : deleteCard(1)
EC -> KB : addCard(edited)
EC -> S  : save(knowledgeBase)
EC -> UI : showEditedMessage(edited)
deactivate EC

UI --> User : "Edited card #1: ..."
@enduml
```

#### Design Note Immutability via Delete and Recreate

Because `Card` is immutable, editing requires deleting the old object and inserting a new one with the same `id`. This pattern ensures that there is never a partially mutated card in memory. The same pattern is used by `TagCommand`.

---

### View a Card

**Command syntax:** `view ID`

**Parsed by:** `ViewCommandParser`

**Executed by:** `ViewCommand`

#### Step-by-Step

1. User types `view 1`.
2. `ViewCommand.execute()` calls `cards.getCardById(1)`.
3. Calls `ui.showQuestion(selectedCard)`  only the question is shown, not the answer. This simulates the "front" of a flashcard.

#### Sequence Diagram

```plantuml
@startuml ViewCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":ViewCommand" as VC
participant ":KnowledgeBase" as KB
participant ":Ui" as UI

User -> FC : "view 1"
FC -> VC : execute(kb, ui, storage, session)
activate VC
VC -> KB : getCardById(1)
alt card not found
  KB --> VC : throw CardNotFoundException
  VC --> FC : exception propagates
  FC -> UI : showError(...)
else card found
  KB --> VC : card : Card
  VC -> UI : showQuestion(card)
  UI --> User : "Q: What is Java?"
end
deactivate VC
@enduml
```
---

### Flip a Card

**Command syntax:** `flip ID`

**Parsed by:** `FlipCommandParser`

**Executed by:** `FlipCommand`

Identical flow to `view`, but calls `ui.showAnswer(selectedCard)` to reveal the answer simulating the "back" of a flashcard.

#### Sequence Diagram 

```plantuml
@startuml FlipCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":FlipCommand" as FC2
participant ":KnowledgeBase" as KB
participant ":Ui" as UI

User -> FC : "flip 1"
FC -> FC2 : execute(kb, ui, storage, session)
activate FC2
FC2 -> KB : getCardById(1)
alt card not found
  KB --> FC2 : throw CardNotFoundException
  FC2 --> FC : exception propagates
  FC -> UI : showError(...)
else card found
  KB --> FC2 : card : Card
  FC2 -> UI : showAnswer(card)
  UI --> User : "A: A programming language"
end
deactivate FC2
@enduml
```
---

### Find Cards

**Command syntax:** `find [q/|a/]KEYWORD`

**Parsed by:** `FindCommandParser` using regex `(?:(?<scope>[qa])/)?(?<keyword>.+)`

**Executed by:** `FindCommand`

The optional `q/` or `a/` prefix restricts the search to questions or answers respectively. Without a prefix, both are searched.

#### Step-by-Step

1. User types `find q/Java`.
2. `FindCommandParser` extracts `scope="q"`, `keyword="java"` (lowercased in `FindCommand`).
3. `FindCommand.execute()` streams all cards from `hb.getAllCards()`.
4. Each card is filtered: if `scope == "q"`, only `card.getQuestion().toLowerCase().contains(keyword)` is checked.
5. Matching cards are collected into a `List<Card>` and passed to `ui.showSearchResults(results, keyword)`.
6. Results are **not** stored in `SessionContainer` by `FindCommand`  the caller may later `save all s/setName` using whatever last results were stored.

#### Sequence Diagram 

```plantuml
@startuml FindCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":FindCommandParser" as FCP
participant ":FindCommand" as FCMD
participant ":KnowledgeBase" as KB
participant ":Ui" as UI

User -> FC : "find q/Java"
FC -> FCP : parse("find q/Java")
FCP -> FCP : scope="q", keyword="java"
FCP --> FC : new FindCommand("java", "q")

FC -> FCMD : execute(kb, ui, storage, session)
activate FCMD

FCMD -> KB : getAllCards()
KB --> FCMD : Collection<Card>

FCMD -> FCMD : stream().filter(card ->\n  "q".equals(scope) ?\n    question.contains(keyword)\n  : question||answer.contains(keyword)\n).collect(...)

FCMD -> UI : showSearchResults(results, "java")
deactivate FCMD

UI --> User : lists matching cards
@enduml
```
---

### List Cards

**Command syntax:** `list` or `list s/SET_NAME`

**Parsed by:** `ListCommandParser`

**Executed by:** `ListCommand`

#### Step-by-Step

1. User types `list` (no set name).
2. `ListCommand.execute()`:
    - `setName == null`: adds all cards from `hb.getAllCards()` to `cardsToShow`.
    - `setName != null`: looks up `hb.getAllTestSets().get(setName)`, retrieves each card by id.
3. `session.setLastSearchResults(cardsToShow)` stores results for potential `save all`.
4. `ui.showSearchResults(cardsToShow, label)` prints the list.

#### Sequence Diagram 

```plantuml
@startuml ListCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":ListCommand" as LC
participant ":KnowledgeBase" as KB
participant ":SessionContainer" as SC
participant ":Ui" as UI

User -> FC : "list"
FC -> LC : execute(kb, ui, storage, session)
activate LC

alt setName == null (list all)
  LC -> KB : getAllCards()
  KB --> LC : Collection<Card>
  LC -> LC : cardsToShow = all cards

else setName != null (list set)
  LC -> KB : getAllTestSets().get(setName)
  KB --> LC : List<Integer> ids
  alt set not found
    LC -> UI : showError("Test set does not exist")
    LC --> FC : return
  end
  loop for each id in ids
    LC -> KB : getCardById(id)
    KB --> LC : card
    LC -> LC : cardsToShow.add(card)
  end
end

LC -> SC : setLastSearchResults(cardsToShow)
LC -> UI : showSearchResults(cardsToShow, label)
deactivate LC

UI --> User : lists cards
@enduml
```
---

### Save to Test Set

**Command syntax:** `save all s/SET_NAME` or `save ID s/SET_NAME`

**Parsed by:** `SaveCommandParser`

**Executed by:** `SaveCommand`

#### Step-by-Step

1. User types `save all s/revision` after a `list` or `find`.
2. `SaveCommand.execute()`:
    - If `target == "all"`: retrieves `session.getLastSearchResults()`. If empty, shows error.
    - If `target` is a number: checks `hb.hasCard(id)`.
3. Calls `hb.saveToTestSet(setName, idsToSave)` this appends to the set, skipping duplicates.
4. Calls `storage.save(hb)` and `ui.showSaveSetSuccess(setName, count)`.

#### Sequence Diagram 

```plantuml
@startuml SaveCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":SaveCommand" as SC2
participant ":SessionContainer" as SC
participant ":KnowledgeBase" as KB
participant ":Storage" as S
participant ":Ui" as UI

User -> FC : "save all s/revision"
FC -> SC2 : execute(kb, ui, storage, session)
activate SC2

alt target == "all"
  SC2 -> SC : getLastSearchResults()
  SC --> SC2 : List<Card>
  alt results empty
    SC2 -> UI : showError("No previous search results...")
    SC2 --> FC : return
  end
  SC2 -> SC2 : idsToSave = card ids from results

else target is numeric id
  SC2 -> KB : hasCard(id)
  alt card not found
    SC2 -> UI : showError("Card ID does not exist")
    SC2 --> FC : return
  end
  SC2 -> SC2 : idsToSave = [id]
end

SC2 -> KB : saveToTestSet("revision", idsToSave)
SC2 -> S  : save(knowledgeBase)
SC2 -> UI : showSaveSetSuccess("revision", count)
deactivate SC2

UI --> User : "Saved N cards to set [revision]"
@enduml
```

---

### Remove from Test Set

**Command syntax:** `remove ID... s/SET_NAME` or `remove all s/SET_NAME`

**Parsed by:** `RemoveCommandParser`

**Executed by:** `RemoveCommand`

#### Step-by-Step

1. User types `remove 1 2 s/revision`.
2. `RemoveCommandParser` splits `target = "1 2"` into `[1, 2]`, creates `new RemoveCommand([1, 2], "revision")`.
3. `RemoveCommand.execute()`:
    - Checks set exists in `hb.getAllTestSets()`.
    - If `cardIds == null` (remove all): replaces the set with an empty `ArrayList`.
    - Otherwise: calls `hb.removeCardFromSet(setName, id)` for each id.
4. Calls `storage.save(hb)` after any successful removal.

#### Sequence Diagram 

```plantuml
@startuml RemoveCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":RemoveCommand" as RC
participant ":KnowledgeBase" as KB
participant ":Storage" as S
participant ":Ui" as UI

User -> FC : "remove 1 s/revision"
FC -> RC : execute(kb, ui, storage, session)
activate RC

RC -> KB : getAllTestSets().containsKey("revision")
alt set not found
  RC -> UI : showError("Test set does not exist")
  RC --> FC : return
end

alt cardIds == null (remove all)
  RC -> KB : addTestSet("revision", new ArrayList<>())
  RC -> S  : save(knowledgeBase)
  RC -> RC : print "Cleared all N cards from set"

else specific ids provided
  loop for each id in cardIds
    RC -> KB : removeCardFromSet("revision", id)
    alt card not in set
      KB --> RC : throw CardNotFoundException
      RC -> UI : showError("Could not remove #id...")
    else removed
      RC -> RC : successCount++
    end
  end
  alt successCount > 0
    RC -> S : save(knowledgeBase)
    RC -> RC : print "Removed N cards from set"
  end
end

deactivate RC
@enduml
```
---

### Test a Set

**Command syntax:** `test SET_NAME`

**Parsed by:** `TestCommandParser`

**Executed by:** `TestCommand`

`TestCommand` starts an interactive study session driven entirely by `Ui.startStudySession()`.

#### Step-by-Step

1. User types `test revision`.
2. `TestCommand.execute()`:
    - Retrieves the id list from `hb.getAllTestSets().get(setName)`.
    - Validates the set exists and is not empty.
    - Collects the `Card` objects into `testCards`.
    - Stores cards in `session.setLastSearchResults(testCards)`.
    - Calls `ui.startStudySession(testCards)` which runs the interactive quiz loop.

#### Sequence Diagram 

```plantuml
@startuml TestCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":TestCommand" as TC
participant ":KnowledgeBase" as KB
participant ":SessionContainer" as SC
participant ":Ui" as UI

User -> FC : "test revision"
FC -> TC : execute(kb, ui, storage, session)
activate TC

TC -> KB : getAllTestSets().get("revision")
alt set not found or null
  TC -> UI : showError("Test set does not exist...")
  TC --> FC : return
end

alt set is empty
  TC -> UI : showError("Test set is empty")
  TC --> FC : return
end

loop for each id in ids
  TC -> KB : hasCard(id) + getCardById(id)
  KB --> TC : card
  TC -> TC : testCards.add(card)
end

TC -> SC : setLastSearchResults(testCards)
TC -> UI : showMessage("Starting test session for [revision]")
TC -> UI : startStudySession(testCards)
activate UI
  UI -> User : show card questions one by one\nwait for input between each\nshow score at end
deactivate UI

deactivate TC
@enduml
```
---

### Tag a Card

**Command syntax:** `tag ID t/TAG`

**Parsed by:** `TagCommandParser`

**Executed by:** `TagCommand`

#### Step-by-Step

1. User types `tag 1 t/programming`.
2. `TagCommand.execute()`:
    - Retrieves the old card via `cards.getCardById(id)`.
    - Creates a new `Card(oldCard.getId(), oldCard.getQuestion(), oldCard.getAnswer(), tag)`.
    - Deletes old, adds new (same delete-and-recreate pattern as `EditCommand`).
    - Saves and shows confirmation.

#### Sequence Diagram 

```plantuml
@startuml TagCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":TagCommand" as TAGC
participant ":KnowledgeBase" as KB
participant ":Storage" as S
participant ":Ui" as UI

User -> FC : "tag 1 t/programming"
FC -> TAGC : execute(kb, ui, storage, session)
activate TAGC

TAGC -> KB : getCardById(1)
KB --> TAGC : oldCard : Card

TAGC -> TAGC : new Card(oldCard.getId(), oldCard.getQuestion(),\n  oldCard.getAnswer(), "programming")

TAGC -> KB : deleteCard(1)
TAGC -> KB : addCard(taggedCard)
TAGC -> S  : save(knowledgeBase)
TAGC -> UI : showTaggedMessage(taggedCard)
deactivate TAGC

UI --> User : "Tagged card #1 with [programming]"
@enduml
```
---

### List All Tags

**Command syntax:** `tags`

**Parsed by:** `TagsCommandParser`

**Executed by:** `TagsCommand`

`TagsCommand` calls `hb.getUniqueTags()` which streams all cards, maps each to its `tag` field, and collects into a `TreeSet` (alphabetically sorted, no duplicates). The set is passed to `ui.showTagsList(tags)`.

#### Sequence Diagram 

```plantuml
@startuml TagsCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":TagsCommand" as TAGSC
participant ":KnowledgeBase" as KB
participant ":Ui" as UI

User -> FC : "tags"
FC -> TAGSC : execute(kb, ui, storage, session)
activate TAGSC

TAGSC -> KB : getUniqueTags()
activate KB
KB -> KB : cards.values().stream()\n  .map(Card::getTag)\n  .collect(toTreeSet())
KB --> TAGSC : Set<String> tags
deactivate KB

TAGSC -> UI : showTagsList(tags)
deactivate TAGSC

UI --> User : lists all unique tags
@enduml
```
---

### Exit

**Command syntax:** `exit`

**Parsed by:** `ExitCommandParser`

**Executed by:** `ExitCommand`

`ExitCommand.execute()` does nothing. Its `isExit()` returns `true`, which causes the `FlashyCard.run()` loop to terminate. `ui.showExitMessage()` is called after the loop ends.

#### Sequence Diagram 

```plantuml
@startuml ExitCommand_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

actor User
participant ":FlashyCard" as FC
participant ":ExitCommand" as EC
participant ":Ui" as UI

User -> FC : "exit"
FC -> EC : execute(kb, ui, storage, session)
note right: does nothing

FC -> EC : isExit()
EC --> FC : true

FC -> UI : showExitMessage()
UI --> User : "Goodbye!"
@enduml
```
---

### Storage: Save Operation

#### Step-by-Step

1. `storage.save(knowledgeBase)` is called.
2. A `BufferedWriter` is opened on the file path (overwriting the file entirely).
3. For each `Card` in `knowledgeBase.getAllCards()`:
    - `|` characters in `question`, `answer`, and `tag` are escaped to `\|`.
    - Writes line: `id|question|answer|tag`.
4. For each test set entry in `knowledgeBase.getAllTestSets()`:
    - Set name `|` chars are escaped.
    - Card IDs are joined by `,`.
    - Writes line: `SET:setName|id1,id2,...`.
5. Writer is closed.

#### Sequence Diagram 

```plantuml
@startuml Storage_Save_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

participant ":Command" as CMD
participant ":Storage" as S
participant ":KnowledgeBase" as KB
participant "BufferedWriter" as BW

CMD -> S : save(knowledgeBase)
activate S

S -> KB : getAllCards()
KB --> S : Collection<Card>

S -> BW : open(filePath, overwrite)

loop for each card
  S -> S : escape | in question, answer, tag
  S -> BW : write("id|question|answer|tag\n")
end

S -> KB : getAllTestSets()
KB --> S : Map<String, List<Integer>>

loop for each test set entry
  S -> S : escape | in setName
  S -> S : join ids by ","
  S -> BW : write("SET:setName|id1,id2,...\n")
end

S -> BW : close()
deactivate S
@enduml
```
---

### Storage: Load Operation

#### Step-by-Step

1. `storage.load()` is called at startup.
2. A `BufferedReader` reads the file line by line.
3. Blank lines are skipped.
4. Lines starting with `SET:` are parsed by `parseAndAddTestSet()`:
    - Strips `SET:` prefix, splits on unescaped `|`, reads set name and comma-separated IDs.
5. Other lines are split on unescaped `|` into 3–4 parts.
    - Part 0 → `id` (must parse as integer, else `CorruptedDataException`).
    - Part 1 → `question` (unescape `\|`).
    - Part 2 → `answer` (unescape `\|`).
    - Part 3 → `tag` (or `"none"` if absent).
6. A `Card(id, question, answer, tag)` is created — this also syncs `idCounter` so new cards get higher ids.
7. Card is added to the `KnowledgeBase`.
8. Populated `KnowledgeBase` is returned.

#### Sequence Diagram 

```plantuml
@startuml Storage_Load_Sequence
skinparam sequenceArrowThickness 2
skinparam backgroundColor #FEFEFE

participant ":FlashyCard" as FC
participant ":Storage" as S
participant "BufferedReader" as BR
participant ":KnowledgeBase" as KB

FC -> S : load()
activate S
S -> S : new KnowledgeBase()
S -> BR : open(filePath)

loop while line = reader.readLine() != null
  alt line is blank
    S -> S : skip
  else line starts with "SET:"
    S -> S : parseAndAddTestSet(line, kb)
    S -> KB : addTestSet(setName, ids)
  else card line
    S -> S : split on (?<!\\\\)\\| → parts[]
    alt parts.length < 3
      S --> FC : throw CorruptedDataException
    end
    S -> S : parseInt(parts[0]) → id
    alt NumberFormatException
      S --> FC : throw CorruptedDataException
    end
    S -> S : unescape \\| in question, answer, tag
    S -> S : new Card(id, question, answer, tag)
    S -> KB : addCard(card)
  end
end

S -> BR : close()
S --> FC : knowledgeBase
deactivate S
@enduml
```

---

## Product Scope

### Target User Profile

- University or secondary school students who need to memorise large amounts of content.
- Users who prefer or are comfortable with CLI tools.
- Fast typists who find GUI applications slower for repetitive data entry.

### Value Proposition

FlashyCard lets students create, organise, and self-test with flashcards entirely from the terminal faster than GUI apps for users who type quickly. Test sets allow targeted revision of specific topics without manually filtering cards each time.

---

## User Stories

| Version | As a …      | I want to …                              | So that I can …                             |
|---------|-------------|------------------------------------------|---------------------------------------------|
| v1.0    | new user    | add flashcards with a question/answer    | build my knowledge base                     |
| v1.0    | user        | list all my flashcards                   | see what I have stored                      |
| v1.0    | user        | view a card's question                   | test my recall before flipping              |
| v1.0    | user        | flip a card to see the answer            | verify whether I recalled correctly         |
| v1.0    | user        | delete a card                            | remove outdated or incorrect cards          |
| v2.0    | user        | edit an existing card                    | fix mistakes without deleting and re-adding |
| v2.0    | user        | tag cards with a category                | organise cards by topic                     |
| v2.0    | user        | find cards by keyword                    | locate specific cards quickly               |
| v2.0    | user        | find cards only in questions or answers  | perform scoped search efficiently           |
| v2.0    | user        | see all unique tags in my knowledge base | understand what categories I have           |
| v2.0    | user        | save search results to a test set        | create targeted revision sets               |
| v2.0    | user        | save a specific single card to a set     | build sets card by card                     |
| v2.0    | user        | run a test session on a set              | quiz myself on a specific topic             |
| v2.0    | user        | remove a card from a test set            | keep sets relevant                          |
| v2.0    | user        | clear all cards from a test set          | reset a revision set quickly                |
| v2.0    | user        | list cards in a specific test set        | review what a set contains                  |

---

## Non-Functional Requirements

1. **Portability:** Should work on Windows, macOS, and Linux with Java 17 installed.
2. **Performance:** All commands should respond within 1 second for a knowledge base of up to 10,000 cards.
3. **Persistence:** Data must persist across sessions without requiring manual saving by the user.
4. **Human-Readability:** The data file (`data/flashcards.txt`) should be human readable for manual inspection or backup.
5. **Robustness:** The application must not crash on invalid user input; all errors must display descriptive messages.
6. **Correctness:** Corrupted storage files must not silently produce wrong data  `CorruptedDataException` must be thrown and handled gracefully, starting with an empty knowledge base.

---

## Glossary

| Term | Definition                                                                                                                   |
|------|------------------------------------------------------------------------------------------------------------------------------|
| **Card** | A flashcard consisting of an auto assigned integer ID, a question string, an answer string, and a tag string.                |
| **KnowledgeBase** | The in-memory store of all cards and test sets for one application run.                                                      |
| **Test Set** | A named collection of card IDs used for focused study sessions.                                                              |
| **Tag** | A category label assigned to a card. Default value is `"none"`.                                                              |
| **SessionContainer** | Holds transient per session state (last search results, active study session) that is not persisted.                         |
| **StudySession** | A stateful iterator over a list of cards used during a `test` command session.                                               |
| **CommandParser** | An abstract class whose concrete subclasses each know how to parse one command type.                                         |
| **Command** | An abstract class whose concrete subclasses each know how to execute one command type.                                       |
| **idCounter** | A static field in `Card` that auto-increments to assign unique IDs. It is synced upward whenever cards are loaded from disk. |
| **Escaped pipe** | The sequence `\|` used in the storage file to represent a literal `                                                          |` character within a field value. |
| **CorruptedDataException** | A checked exception thrown by `Storage.load()` when the file format is invalid.                                              |

---

## Instructions for Manual Testing

### Prerequisites

Ensure Java 17 is installed. Compile and build the project with your preferred tool (e.g., Gradle or Maven), then run:

```
java -jar flashycard.jar
```

Or run `FlashyCard.main()` directly from your IDE.

---

### Loading Sample Data

To test with pre-existing data, create `data/flashcards.txt` in the same folder as the JAR (or project root) with this content:

```
1|What is Java?|A programming language|none
2|What is OOP?|Object Oriented Programming|science
3|What is 2+2?|4|none
SET:mySet|1,2
```

Then launch the app it will load 3 cards and 1 test set automatically.

---

### Test Case 1: Adding a Card

**Input:**
```
add q/What is Python? a/A scripting language
```

**Expected:** Confirmation message showing the new card's assigned ID (e.g., `#4` if 3 cards were preloaded).

---

### Test Case 2: Adding a Card with Missing Fields

**Input:**
```
add q/Only a question
```

**Expected:** `ERROR` message the regex requires both `q/` and `a/` flags.

---

### Test Case 3: Listing All Cards

**Input:**
```
list
```

**Expected:** All cards shown with their IDs, questions, and tags.

---

### Test Case 4: Viewing and Flipping

**Input:**
```
view 1
flip 1
```

**Expected:** First command shows only the question. Second shows only the answer.

---

### Test Case 5: Viewing a Non-Existent Card

**Input:**
```
view 999
```

**Expected:** `ERROR`  "Card with given ID cannot be found in the knowledge base".

---

### Test Case 6: Editing a Card

**Input:**
```
edit 1 q/What is Go? a/A compiled language
edit 2 q/What is Abstraction?
edit 3 a/Four
```

**Expected:** Only the specified fields change. Tag is always preserved. Confirming with `view` / `flip` shows updated values.

---

### Test Case 7: Editing with No Fields

**Input:**
```
edit 1
```

**Expected:** `ERROR` "Edit command requires at least q/QUESTION or a/ANSWER."

---

### Test Case 8: Tagging

**Input:**
```
tag 1 t/programming
tags
```

**Expected:** Card 1 now has tag `programming`. The `tags` command lists all unique tags including `programming`.

---

### Test Case 9: Finding Cards

**Input:**
```
find java
find q/What
find a/Four
```

**Expected:** First finds cards with "java" in question or answer. Second finds cards with "What" in question only. Third finds cards with "Four" in answer only.

---

### Test Case 10: Saving to a Test Set

**Input:**
```
list
save all s/revision
save 1 s/singles
```

**Expected:** After `list`, all cards are in `SessionContainer`. `save all` saves them all to `revision`. `save 1` saves only card 1 to `singles`.

---

### Test Case 11: Listing a Test Set

**Input:**
```
list s/revision
list s/nonexistent
```

**Expected:** First shows cards in `revision`. Second shows an error.

---

### Test Case 12: Testing a Set

**Input:**
```
test revision
```

**Expected:** Interactive quiz starts. Cards are shown one by one. Final score is printed at the end.

---

### Test Case 13: Removing from a Set

**Input:**
```
remove 1 s/revision
remove all s/singles
list s/revision
```

**Expected:** Card 1 is no longer in `revision`. `singles` is now empty. `list s/revision` shows the remaining cards.

---

### Test Case 14: Deleting a Card

**Input:**
```
delete 2
list
```

**Expected:** Card 2 no longer appears in `list`.

---

### Test Case 15: Deleting a Non-Existent Card

**Input:**
```
delete 999
```

**Expected:** `ERROR`  descriptive not found message.

---

### Test Case 16: Persistence Check

1. Add some cards and save a test set.
2. Type `exit`.
3. Relaunch the app.
4. Type `list` all previously created cards and sets should still be present.

---

### Test Case 17: Corrupted Data File

1. Open `data/flashcards.txt` in a text editor.
2. Delete part of a line so it has fewer than 3 pipe-separated fields.
3. Relaunch the app.

**Expected:** The app prints a data-corruption warning and starts with an empty knowledge base it does **not** crash.

---

### Test Case 18: Invalid Commands

```
edit 999 q/Test
edit 1
delete 999
flip 999
add q/Missing answer
save all s/rev
```

The last line (`save all`) should error because no `list` or `find` was run first.

**Expected:** Each produces a descriptive `ERROR` message.

---

### Test Case 19: Pipe Character in Card Content

**Input:**
```
add q/What does A|B mean? a/Bitwise OR operation
flip [id]
```

**Expected:** The card is stored and retrieved correctly with the `|` character intact (it is escaped as `\|` in the file, then unescaped on load).

---
