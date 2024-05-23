# Conor Dowdall | Java Developer

## Words Embeddings Text Interface

terminal-based application to interact with vector representations for words, e.g. from [GloVe](https://nlp.stanford.edu/projects/glove/)

- openjdk 17.0.10 2024-01-16
- robust error handling
- persistent settings storage, using java.util.prefs.Preferences

### Console

**ConsoleColor** enum for terminal color codes

**ConsolePrint** static methods for consistent message printing

**ConsoleProgressMeter** static method for displaying a text-based progress meter

### Words-Embeddings

**WordsEmbeddings** load-and-interact-with a words-embeddings file

- csv format detection
- word vector operations (search, add, subtract, multiply, divide)
- similarity algorithms to find similar/dissimilar words

### Menus

**MainMenuItem** enum for main menu options with auto-generated shortcuts

**MainMenu** handles user interactions and application launches; prompts for a words-embeddings file, if not loaded
1. **Find Similar Words** finds top matching words for a given input
2. **Find Dissimilar Words** finds least matching words for a given input
3. **Word Calculator** performs vector operations and finds top matching words
4. **Settings** customizes application preferences

**SettingsMenuItem** enum for settings menu options with auto-generated shortcuts

**SettingsMenu** store user preferences with persistent values; default settings included
1. **Load Word-Embeddings File**
2. **Number of Similarities to Find** (0 < n <= #words)
3. **Similarity Algorithm to Use**
4. **Toggle Similarity Score in Output**
5. **Specify Data-Output File**
6. **Toggle Append/Overwrite Data-Output File**
7. **Empty the Output File**
8. **Reset Settings to Defaults**
9. **Print Current Settings**

**SimilarityAlgorithmMenuItem** turn utility class into a menu

**WordCalculatorMenuItem** enum for calculator options

**WordCalculatorMenu** perform vector operations on words' embeddings
1. **Add**
2. **Subtract**
3. **Multiply**
4. **Divide**

### Utilities

**Array** static methods to find min-or-max values

**SimilarityAlgorithm** enum with abstract calculate method
1. **Dot Product**
2. **Euclidean Distance (No Square Root)**
3. **Euclidean Distance**
4. **Cosine Similarity**

**Vector** static methods for vector operations like add, subtract, dot product