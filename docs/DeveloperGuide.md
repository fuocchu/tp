# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

## Storage Component

### Overview
The `Storage` component is responsible for persisting and retrieving application data from the local file system. It ensures that all `Card` objects in the `KnowledgeBase` are saved to disk and can be reconstructed when the application restarts.
### Implementation

The `Storage` class uses a simple line-based text format to store data. Each `Card` is serialized into a single line using the following format:
`id | question | answer`
#### Save Operation
- Retrieve all cards from `KnowledgeBase`
- Convert each card into a string format
- Escape special characters (`|`)
- Write each card as a line into the file

#### Load Operation
- Read file line by line
- Skip empty lines
- Split each line into 3 parts (id, question, answer)
- Validate data format
- Reconstruct `Card` objects
- Add cards to `KnowledgeBase`

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
