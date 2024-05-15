# Conor Dowdall | Java Developer

## Words Embeddings Text Interface

A terminal-based application to interact with vector representations for words, e.g. from [GloVe](https://nlp.stanford.edu/projects/glove/). Appropriate error checking is done at every stage in the program's execution, and suitable exceptions are thrown, and caught for display to the user.

Built using:
```java
java --version
openjdk 17.0.10 2024-01-16
OpenJDK Runtime Environment (build 17.0.10+7-Ubuntu-123.10.1)
OpenJDK 64-Bit Server VM (build 17.0.10+7-Ubuntu-123.10.1, mixed mode, sharing)
```

## src/ie/atu/sw/console

### ConsoleColour.java
enum that predefines color codes for printing to the terminal

### ConsolePrint.java
class with static methods to print info, warnings, options, etc., in a consistent way

### ConsoleProgressMeter.java
class with static method to display a text-based progress meter

## src/ie/atu/sw/embeddings

### WordsEmbeddings.java
class to load-and-interact-with a words-embeddings file
- auto-detects comma-or-space-based CSV format, and number of features in a word-embeddings file
- includes methods to 
    - search for a word's vector-embedding
    - add, subtract, multiply, and divide words and/or word-vectors
    - use various similarity algorithms to find similar-or-dissimilar words to a given word, or word-vector

## src/ie/atu/sw/menu

### MainMenuItem.java
enum to store the main menu options
- auto-generates a keyboard shortcut, based on position in list, or using a custom key

### MainMenu.java
class to accept user interactions in the terminal and run the available applications

#### [1] Find Similar Words
accepts a word, or sequence of words, and finds the top matching words

#### [2] Find Dissimilar Words
accepts a word, or sequence of words, and finds the bottom matching words

#### [3] Word Calculator
allows user to add, subtract, multiply, and divide words, then finds the top matching words

#### [4] Settings
allows user to customize the above applications

### SettingsMenuItem.java
enum to store the settings menu options
- auto-generates a keyboard shortcut, based on position in list, or using a custom key

### SettingsMenu.java

### SimilarityAlgorithmMenuItem.java

### WordCalculatorMenuItem.java

### WordCalculatorMenu.java

## src/ie/atu/sw/util

### Array.java

### SimilarityAlgorithm.java

### Vector.java