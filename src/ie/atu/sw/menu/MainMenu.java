package ie.atu.sw.menu;

import java.util.Scanner;

import ie.atu.sw.console.ConsoleColour;
import ie.atu.sw.embeddings.WordsEmbeddings;

public class MainMenu {
    private Scanner inputScanner = new Scanner(System.in);

    private WordsEmbeddings wordsEmbeddings;
    private String outputFileName = "./out.txt";

    public MainMenu() {
        MainMenuItem.printHeader();
        printOptionsAndProcessInput();
    }

    private WordsEmbeddings getWordsEmbeddings() {
        return wordsEmbeddings;
    }

    private void setWordsEmbeddings(WordsEmbeddings wordsEmbeddings) {
        this.wordsEmbeddings = wordsEmbeddings;
    }

    private String getOutputFileName() {
        return outputFileName;
    }

    private void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    private void printOptionsAndProcessInput() {
        try {

            System.out.println();
            MainMenuItem.printOptions();

            String input = this.inputScanner.nextLine();
            MainMenuItem item = MainMenuItem.valueOfKey(input);

            switch (item) {
                case EMBEDDINGS_FILE -> loadNewWordEmbeddings();
                case OUTPUT_FILE -> specifyNewOutputFile();
                case SIMILAR_WORDS -> launchSimilarWords();
                case DISSIMILAR_WORDS -> launchDissimilarWords();
                case WORD_CALCULATOR -> launchWordCalculator();
                case SETTINGS -> launchSettings();
                case QUIT -> quitProgram();
                default -> throw new Exception("This should never happen!");
            }

        } catch (Exception e) {
            printError(e);
        }

        printOptionsAndProcessInput();
    }

    private void printError(Exception e) {
        System.err.println();
        System.err.print(ConsoleColour.BLACK_BACKGROUND);
        System.err.print(ConsoleColour.RED_BOLD_BRIGHT);
        System.err.print("[ERROR] " + e);
        System.err.print(ConsoleColour.RESET);
        System.err.println();
    }

    private String scanFileName() throws Exception {
        System.out.print("Enter file name: ");
        String fileName = this.inputScanner.nextLine();

        if (fileName.equals(""))
            throw new Exception("No file name provided");

        return fileName;
    }

    private void printWithUnderline(String text) {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.WHITE_UNDERLINED);
        System.out.print(text);
        System.out.print(ConsoleColour.RESET);
    }

    private void printHeading(String heading) {
        System.out.println();
        printWithUnderline(heading);
        System.out.println();
    }

    private void loadNewWordEmbeddings() throws Exception {
        printHeading("Load Word-Embeddings File");

        String wordEmbeddingsFileName = scanFileName();

        setWordsEmbeddings(new WordsEmbeddings(wordEmbeddingsFileName));
    }

    private void specifyNewOutputFile() throws Exception {
        printHeading("Specify Output-Data File");

        setOutputFileName(scanFileName());

        System.out.println("You entered: " + getOutputFileName());
    }

    private void launchSimilarWords() throws Exception {
        if (wordsEmbeddings == null)
            loadNewWordEmbeddings();

        System.out.println();
        System.out.print("Enter word(s): ");
        String input = this.inputScanner.nextLine();
        String[] words = input.split(" ");
        for (String word : words) {
            try {
                String[] similarWords = getWordsEmbeddings().getSimilarWords(word, 10);
                printHeading("Words Similar To '" + word + "'");
                for (String similarWord : similarWords)
                    System.out.println(similarWord);
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private void launchDissimilarWords() {
    }

    private void launchWordCalculator() {
    }

    private void launchSettings() {

    }

    private void quitProgram() {
        this.inputScanner.close();
        System.out.println();
        System.out.println("Quit");
        System.out.println();
        System.exit(0);
    }
}
