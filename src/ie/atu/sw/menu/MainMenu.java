package ie.atu.sw.menu;

import java.util.Scanner;

import ie.atu.sw.console.ConsoleColour;
import ie.atu.sw.embeddings.WordsEmbeddings;

public class MainMenu {
    Scanner scanner = new Scanner(System.in);

    WordsEmbeddings wordsEmbeddings;
    String outputFileName = "./out.txt";

    public WordsEmbeddings getWordsEmbeddings() {
        return wordsEmbeddings;
    }

    public void setWordsEmbeddings(WordsEmbeddings wordsEmbeddings) {
        this.wordsEmbeddings = wordsEmbeddings;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void run() {
        MainMenuItem.printHeader();
        printOptionsAndProcessInput();
    }

    public void printOptionsAndProcessInput() {
        MainMenuItem.printOptions();
        String input = scanner.nextLine();

        try {
            MainMenuItem item = MainMenuItem.valueOfKey(input);
            switch (item) {
                case EMBEDDINGS_FILE:
                    loadNewWordEmbeddings();
                    printOptionsAndProcessInput();
                    break;
                case OUTPUT_FILE:
                    loadNewOutputFile();
                    printOptionsAndProcessInput();
                    break;
                case SIMILAR_WORDS:
                    break;
                case SETTINGS:
                    break;
                case QUIT:
                    scanner.close();
                    System.out.println("Exit\n");
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            System.err.println();
            System.err.print(ConsoleColour.BLACK_BACKGROUND);
            System.err.print(ConsoleColour.RED_BOLD_BRIGHT);
            System.err.print("ERROR " + e);
            System.err.println(ConsoleColour.RESET);
            printOptionsAndProcessInput();
        }
    }

    private void loadNewWordEmbeddings() throws Exception {
        String fileName = scanFileName();
        setWordsEmbeddings(new WordsEmbeddings(fileName));
    }

    private void loadNewOutputFile() {
        this.outputFileName = scanFileName();
        System.out.println("You entered: " + this.outputFileName);
    }

    private String scanFileName() {
        System.out.println();
        System.out.println("Enter file name:");
        String fileName = scanner.nextLine();
        return fileName;
    }
}
