package ie.atu.sw.menu;

import java.io.IOException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

/**
 * handle user interactions which perform vector operations on words, and
 * return word-search results
 */
public class WordCalculatorMenu {
    private Scanner inputScanner;
    private SettingsMenu settingsMenu;
    private StringBuilder currentWordCalculation = new StringBuilder();
    private double[] currentEmbedding;
    private boolean keepWordCalculatorOpen;

    /**
     * initialize the class and ensure that a words-embeddings file has been
     * initialized
     * 
     * @param inputScanner - scanner to accept user input from the terminal
     * @param settingsMenu - the settings menu where all user preferences are stored
     * @throws Exception
     */
    public WordCalculatorMenu(Scanner inputScanner, SettingsMenu settingsMenu) throws Exception {
        this.inputScanner = inputScanner;
        this.settingsMenu = settingsMenu;

        this.settingsMenu.initWordsEmbeddings();
    }

    /**
     * when launched, prompt user for a word to begin with - all calculations will
     * start from the vector representation of this word
     * 
     * @throws Exception
     */
    public void launchMenu() throws Exception {
        this.keepWordCalculatorOpen = true;

        ConsolePrint.printTitle("Word Calculator");
        this.settingsMenu.printSettings();

        String input = processWordInput();

        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append(";");

        this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().getWordEmbedding(input);
        settingsMenu.getWordsEmbeddings().getSimilarWords(
                this.currentEmbedding,
                settingsMenu.getNumberOfSimilaritiesToFind());

        settingsMenu.printDataOutput(this.currentWordCalculation.toString(), true);

        printOptionsAndProcessInput();
    }

    /**
     * show a nice heading, including all previous inputs from user; and offer
     * options for further calculations, then launch appropriate action, and print
     * data-output, based on user settings
     * 
     * @throws Exception
     */
    private void printOptionsAndProcessInput() throws Exception {
        try {

            ConsolePrint.printTitle("Word Calculator\n" + this.currentWordCalculation.toString());
            WordCalculatorMenuItem.printOptions();

            String input = this.inputScanner.nextLine();
            WordCalculatorMenuItem item = WordCalculatorMenuItem.valueOfKey(input);

            switch (item) {
                case ADD -> {
                    input = processWordInput();
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().add(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(" + " + input);
                }
                case SUBTRACT -> {
                    input = processWordInput();
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().subtract(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(" - " + input);
                }
                case MULTIPLY -> {
                    input = processWordInput();
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().multiply(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(" * " + input);
                }
                case DIVIDE -> {
                    input = processWordInput();
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().divide(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(" / " + input);
                }
                case QUIT -> quitWordCalculator();
            }

        } catch (IOException e) {
            ConsolePrint.printError("File I/O Problem: " + e.getMessage());
        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            if (this.keepWordCalculatorOpen)
                printOptionsAndProcessInput();
        }

    }

    /**
     * a consistent way to prompt user for a word input
     * 
     * @return a sanitized version of the word input (a single lowercase word)
     */
    private String processWordInput() {
        System.out.print("Enter a word: ");

        String input = this.inputScanner.nextLine();
        String sanitizedInput = input.replaceAll("[\s]+", " ").toLowerCase().split(" ")[0];
        ConsolePrint.printInfo("Interpreting input as: '" + sanitizedInput + "'");

        return sanitizedInput;
    }

    /**
     * find similar-or-dissimilar words based on user input and settings, and print
     * data output
     * 
     * @param input - the calculation-type and word that were input by the user
     *              (used for user feedback)
     * @throws Exception
     */
    private void calculateAndPrintOutput(String input) throws Exception {
        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append(";");

        settingsMenu.getWordsEmbeddings().getSimilarWords(
                this.currentEmbedding,
                settingsMenu.getNumberOfSimilaritiesToFind());

        settingsMenu.printDataOutput(this.currentWordCalculation.toString(), true);
    }

    /**
     * allow settings loop to stop running by toggling 'keepWordCalculatorOpen'
     * boolean
     * 
     */
    private void quitWordCalculator() {
        this.keepWordCalculatorOpen = false;

        ConsolePrint.printInfo("Close Word Calculator");
    }
}
