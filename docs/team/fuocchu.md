## Chu Duong Huy Phuoc's Project Portfolio Page

---

## Project: FlashyCard

FlashyCard is a CLI-based flashcard application designed for fast typists, focusing on efficient knowledge retention through quick command-based interactions and persistent study sessions.

---

## Summary of Contributions

### Code Contributed
- [RepoSense Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=fuocchu&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=zoom&zA=fuocchu&zR=AY2526S2-CS2113-T09-3%2Ftp%5Bmaster%5D&zACS=247.74041297935102&zS=2026-02-20T00%3A00%3A00&zFS=&zU=2026-04-14T23%3A59%3A59&zMG=false&zFTF=commit&zFGS=groupByRepos&zFR=false)
---

### Enhancements Implemented

**Storage Component**
- Designed and implemented a full save/load pipeline using a custom pipe-delimited plain text format.
- Implemented escape handling for reserved characters (`|`) to prevent data corruption during parsing and serialization.
- Added automatic directory creation on first launch to improve usability.
- Enabled persistence for test sets, allowing users to retain study progress across sessions.
- Introduced `CorruptedDataException` to handle malformed input gracefully, improving robustness and fault tolerance.

**Edit Command**
- Implemented `edit ID [q/QUESTION] [a/ANSWER]` command for flexible modification of flashcards.
- Supports partial updates (question-only or answer-only) while preserving existing metadata such as tags.
- Designed logic to ensure data consistency without overwriting unchanged fields.
- Added comprehensive unit tests covering:
    - Full-field edits
    - Partial edits
    - Tag preservation
    - Invalid ID handling
    - Exit behaviour

---

### Contributions to the User Guide
- Wrote the **Data Management section**, including:
    - Auto-save behavior
    - File format explanation
    - Warnings for direct file editing
    - FAQ section
- Documented commands: `edit`, `tag`, `tags`, `find`, `save`, `test`, `remove`
- Updated the **Command Summary table** to reflect all v2.0 features

---

### Contributions to the Developer Guide
- Wrote the **Architecture Overview**, including the full component diagram
- Designed and documented the **Storage Component**, including:
    - File format specification
    - Save/load workflow
    - Sequence diagrams
- Documented all 13 commands with individual sequence diagrams:
  (`add`, `delete`, `edit`, `view`, `flip`, `find`, `list`, `save`, `remove`, `test`, `tag`, `tags`, `exit`)
- Wrote the **Parser Component**, including class diagram and parse-flow sequence diagram
- Wrote the **Command Component**, including full class hierarchy diagram
- Wrote the **Model Component**, including class diagram for `Card`, `KnowledgeBase`, and `StudySession`
- Documented the **SessionContainer** with class diagram
- Wrote the **UI Component**, including class diagram and main application loop sequence diagram
- Completed supporting sections:
    - Product Scope
    - User Stories
    - Non-Functional Requirements
    - Glossary
    - Instructions for Manual Testing (19 test cases)
- Updated README with:
    - Project description
    - Feature list
    - Quick start guide
    - Full command summary table

---

### Contributions to Team-Based Tasks
- Set up the storage layer early, enabling other features to integrate smoothly with persistent data from the start
- Helped ensure consistency across components by defining data format and storage conventions
- Fixed code style and formatting issues across the codebase to maintain code quality

---

### Review / Mentoring Contributions
- Reviewed teammates’ pull requests and provided feedback on code structure, naming, and error handling
- Assisted teammates in debugging integration issues between command and storage components
- Helped ensure adherence to project coding standards and architecture design

---

### Contributions Beyond the Project Team
- Reported bugs and provided feedback during practical exam (PE) testing of other teams’ products
- Shared clarifications and helped peers with assignment related issues when needed
