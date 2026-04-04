## Project: FlashyCard

FlashyCard is a CLI-based flashcard app for fast typists to memorize content efficiently.

---

## Summary of Contributions

### Enhancements Implemented

**UI & Interactive Study Experience**
* **Core CLI Framework (`Ui.java`):** Developed the primary interaction layer featuring high-performance command prompting, stylized dividers, and branded welcome headers for an improved user experience.
* **Real-Time Study Sessions:** Implemented the `StudySession` model and `startStudySession` flow, including real-time progress tracking (e.g., `[1/5]`), and final score calculation.
* **Dynamic Category Visualization:** Created the `showTagsList` system to organize cards into numbered categories, providing a clear summary of total unique tags and "Uncategorized" cards.

**Command & Logic Architecture**
* **Advanced Search & Filter (`FindCommand`):** Implemented regex-powered searching supporting global keywords or scoped searches specifically for questions (`q/`) or answers (`a/`).
* **Test Set Management:** Developed the `SaveCommand`, `TestCommand`, and `RemoveCommand` suite, allowing users to group cards into persistent, named sub-collections (e.g., "Midterm_Prep") for targeted revision.
* **Contextual Session Handling (`SessionContainer`):** Built a state-tracking mechanism that allows users to save results from a previous `find` or `list` command into a test set without re-entering IDs.
* **Dynamic Tagging System:** Implemented `TagCommand` and `TagsCommand` to allow on-the-fly categorization and modification of card keywords.

**Storage & Persistence Logic**
* **Enhanced Data Pipeline:** Upgraded `Storage.java` to handle saving and loading for both card-level tags and user-defined Test Sets using a pipe-delimited format.
* **Data Integrity & Escaping:** Implemented look-behind regex patterns (`(?<!\\)\|`) to ensure that user content containing the pipe character (`|`) is escaped correctly to prevent database corruption.
* **Legacy Compatibility:** Added logic to ensure v2.0 remains compatible with v1.0 data files by implementing default field handling for missing tags.

**Testing & Quality Assurance**
* **Automated I/O Testing:** Configured the `build.gradle` and `runtest.bat` infrastructure to perform automated regression testing against expected CLI text outputs.
* **Comprehensive Test Suite:** Authored over 50 test files, including:
    * **Parser Validation:** Rigorous testing of `Find`, `Save`, `Tag`, and `Test` parsers for whitespace and malformed input handling.
    * **Command Logic:** Update Unit tests for `Delete`, `View`, `Flip`, and `Add` commands to verify model-storage synchronization.
    * **Model Integrity:** Verified the immutability and state management of `StudySession` and `KnowledgeBase`.

### Summary of enchancements:
Implemented
Find: Searches for cards whose question or answer contains the given keyword.
Tag: Assigns a category tag to a card.
test: Runs an interactive quiz session for all cards in a test set.
save: Saves a card or the last search/list results to a named test set.
add: Adds a new flashcard to your knowledge base.
tags: Lists all unique tags currently used in the knowledge base.
list: Shows all cards in the knowledge base, or all cards in a specific test set.
remove: Removes one or more cards from a test set, or clears the entire set.

### Contributions to Team-Based Tasks
* **Core Logic Integration:** Developed and integrated the `SessionContainer` early to allow team members to build commands that share state.
* **Code Maintenance:** Managed Checkstyle configurations and conducted style fixes across the codebase to ensure consistency and quality.
* **Setup:** Established the storage layer early so other commands could integrate with it from the start of development.