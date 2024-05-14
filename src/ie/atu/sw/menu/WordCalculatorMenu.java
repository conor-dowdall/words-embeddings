package ie.atu.sw.menu;

import java.io.IOException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

public class WordCalculatorMenu {
    private Scanner inputScanner;
    private SettingsMenu settingsMenu;
    private StringBuilder currentWordCalculation = new StringBuilder();
    private double[] currentEmbedding;
    private boolean keepWordCalculatorOpen;

    public WordCalculatorMenu(Scanner inputScanner, SettingsMenu settingsMenu) throws Exception {
        this.inputScanner = inputScanner;
        this.settingsMenu = settingsMenu;

        this.settingsMenu.initWordsEmbeddings();
    }

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

    private void printOptionsAndProcessInput() throws Exception {
        try {

            ConsolePrint.printTitle("Word Calculator\n" + this.currentWordCalculation.toString());
            WordCalculatorMenuItem.printOptions();

            String input = this.inputScanner.nextLine();
            WordCalculatorMenuItem item = WordCalculatorMenuItem.valueOfKey(input);

            switch (item) {
                case ADD -> {
                    input = processWordInput();
                    this.currentWordCalculation.append(" + ");
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().add(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(input);
                }
                case SUBTRACT -> {
                    input = processWordInput();
                    this.currentWordCalculation.append(" - ");
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().subtract(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(input);
                }
                case MULTIPLY -> {
                    input = processWordInput();
                    this.currentWordCalculation.append(" * ");
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().multiply(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(input);
                }
                case DIVIDE -> {
                    input = processWordInput();
                    this.currentWordCalculation.append(" / ");
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().divide(
                            this.currentEmbedding,
                            input);
                    calculateAndPrintOutput(input);
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

    private String processWordInput() {
        System.out.print("Enter a word: ");

        String input = this.inputScanner.nextLine();
        String sanitizedInput = input.replaceAll("[\s]+", " ").toLowerCase().split(" ")[0];
        ConsolePrint.printInfo("Interpreting input as: '" + sanitizedInput + "'");

        return sanitizedInput;
    }

    private void calculateAndPrintOutput(String input) throws Exception {
        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append(";");

        settingsMenu.getWordsEmbeddings().getSimilarWords(
                this.currentEmbedding,
                settingsMenu.getNumberOfSimilaritiesToFind());

        settingsMenu.printDataOutput(this.currentWordCalculation.toString(), true);
    }

    private void quitWordCalculator() throws IOException {
        this.keepWordCalculatorOpen = false;

        ConsolePrint.printInfo("Close Word Calculator");
    }
}
