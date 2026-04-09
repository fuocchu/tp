## Project: FlashyCard

FlashyCard is a CLI-based flashcard app for fast typists to memorize content efficiently.

## Summary of Contributions

### Enhancements Implemented

**Storage Component**
- Implemented full save/load pipeline using pipe-delimited plain text format
- Added escape handling for `|` characters inside field values
- Added auto-directory creation on first launch
- Added support for persisting test sets
- Throws `CorruptedDataException` on malformed data for graceful recovery

**Edit Command**
- Implemented `edit ID [q/QUESTION] [a/ANSWER]` command
- Supports editing question only, answer only, or both fields while preserving existing tag
- Added unit tests covering both-field edit, partial edit, tag preservation, invalid ID, and exit behaviour

### Contributions to the User Guide
- Wrote the Data Management section (auto-save, file format, direct editing warning, FAQ)
- Added documentation for `edit`, `tag`, `tags`, `find`, `save`, `test`, `remove` commands
- Updated Command Summary table to reflect all v2.0 features

### Contributions to the Developer Guide
- Wrote the Storage Component section including file format, save/load operations, and sequence diagrams
- Wrote the Architecture Overview with full component diagram
- Documented all 13 commands with individual sequence diagrams (add, delete, edit, view, flip, find, list, save, remove, test, tag, tags, exit)
- Wrote Parser Component section including class diagram and parse-flow sequence diagram
- Wrote Command Component section including full class hierarchy diagram
- Wrote Model Component section including class diagram covering `Card`, `KnowledgeBase`, and `StudySession`
- Wrote Storage Component section including save and load sequence diagrams
- Wrote `SessionContainer` section with class diagram
- Wrote UI Component section including class diagram and main application loop sequence diagram
- Filled in Product Scope, User Stories, NFRs, Glossary, and Instructions for Manual Testing (19 test cases)
- Updated README with project description, feature list, quick start guide, and full command summary table

### Contributions to Team Based Tasks
- Set up storage layer early so other commands could integrate with it from the start
- Fixed code style issues across the codebase