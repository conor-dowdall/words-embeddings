package ie.atu.sw.menu;

import java.nio.file.NoSuchFileException;
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

        } catch (NoSuchFileException e) {
            printError("File not found: '" + e.getMessage() + "'");
        } catch (Exception e) {
            printError(e.getMessage());
        } finally {
            printOptionsAndProcessInput();
        }

    }

    private void printError(String error) {
        System.err.println();
        System.err.print(ConsoleColour.BLACK_BACKGROUND);
        System.err.print(ConsoleColour.RED_BOLD_BRIGHT);
        System.err.print("[ERROR] " + error);
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

        this.wordsEmbeddings = new WordsEmbeddings(wordEmbeddingsFileName);
    }

    private void specifyNewOutputFile() throws Exception {
        printHeading("Specify Output-Data File");

        this.outputFileName = scanFileName();

        System.out.println("You entered: " + this.outputFileName);
    }

    private void launchSimilarWords() throws Exception {
        if (this.wordsEmbeddings == null)
            loadNewWordEmbeddings();

        printHeading("Find Similar Words");
        System.out.println("Using Embeddings File: " + wordsEmbeddings.getFileName());
        System.out.println();
        System.out.print("Enter word(s): ");

        String input = this.inputScanner.nextLine();
        String[] words = input.split(" ");

        for (String word : words) {
            try {

                String[] similarWords = this.wordsEmbeddings.getSimilarWords(word, 10);
                printHeading("Words Similar To '" + word + "'");

                for (String similarWord : similarWords)
                    System.out.println(similarWord);

            } catch (Exception e) {
                printError(e.getMessage());
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
