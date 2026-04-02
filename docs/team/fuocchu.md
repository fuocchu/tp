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
- Wrote the Storage Component section including file format and save/load operations
- Wrote Architecture Overview, Command, Parser, and Model component descriptions
- Filled in Product Scope, User Stories, NFRs, Glossary, and Instructions for Manual Testing

### Contributions to Team-Based Tasks
- Set up storage layer early so other commands could integrate with it from the start
- Fixed code style issues across the codebase