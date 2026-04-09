# FlashyCard

FlashyCard is a CLI based flashcard application for students who want to memorise content fast.
Built for keyboard first users, if you type quickly, FlashyCard gets out of your way.

## Features

- **Add, edit, delete** flashcards with a question and answer
- **Tag** cards by topic and browse all tags
- **Find** cards by keyword, scoped to questions or answers
- **Test sets**  save any search result or individual card into a named set for targeted revision
- **Interactive test sessions**  quiz yourself on a set and get a score at the end
- **Auto-saving**  every change is written to disk immediately, no manual save needed

## Quick Start

1. Ensure you have **Java 17** installed.
2. Download the latest `flashycard.jar` from the **Releases** page.
3. Run the app:
   ```
   java -jar flashycard.jar
   ```
4. Try your first command:
   ```
   add q/What is Java? a/A general-purpose programming language
   ```

## Command Summary

| Command | Syntax | Description |
|---------|--------|-------------|
| Add | `add q/QUESTION a/ANSWER` | Add a new flashcard |
| View | `view ID` | Show a card's question |
| Flip | `flip ID` | Reveal a card's answer |
| Edit | `edit ID [q/QUESTION] [a/ANSWER]` | Edit question and/or answer |
| Delete | `delete ID` | Remove a card |
| Tag | `tag ID t/TAG` | Assign a tag to a card |
| Tags | `tags` | List all unique tags |
| Find | `find [q/\|a/]KEYWORD` | Search cards by keyword |
| List | `list [s/SET]` | List all cards or a test set |
| Save | `save all\|ID s/SET` | Save cards to a named test set |
| Remove | `remove all\|ID... s/SET` | Remove cards from a test set |
| Test | `test SET` | Start an interactive quiz on a set |
| Exit | `exit` | Exit the application |

## Useful Links

- [User Guide](UserGuide.md)
- [Developer Guide](DeveloperGuide.md)
- [About Us](AboutUs.md)