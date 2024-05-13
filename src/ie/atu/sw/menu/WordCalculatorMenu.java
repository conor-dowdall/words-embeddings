package ie.atu.sw.menu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

public class WordCalculatorMenu {
    private Scanner inputScanner;
    private SettingsMenu settingsMenu;
    private BufferedWriter dataOutputBufferedWriter;
    private StringBuilder currentWordCalculation = new StringBuilder();
    private double[] currentEmbedding;
    private boolean keepWordCalculatorOpen;

    public WordCalculatorMenu(Scanner inputScanner, SettingsMenu settingsMenu) throws Exception {
        this.inputScanner = inputScanner;
        this.settingsMenu = settingsMenu;

        this.settingsMenu.initWordsEmbeddings();
        this.dataOutputBufferedWriter = this.settingsMenu.getDataOutputBufferedWriter();

    }

    public void launchMenu() throws Exception {
        this.keepWordCalculatorOpen = true;

        ConsolePrint.printTitle("Word Calculator");
        this.settingsMenu.printSettings();

        String input = processWordInput();

        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append("; ");

        this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().getWordEmbedding(input);

        printOptionsAndProcessInput();
    }

    private void printOptionsAndProcessInput() throws Exception {
        try {

            WordCalculatorMenuItem.printOptions();

            String input = this.inputScanner.nextLine();
            WordCalculatorMenuItem item = WordCalculatorMenuItem.valueOfKey(input);

            switch (item) {
                case ADD -> add();
                case SUBTRACT -> System.out.println("subtract");
                case MULTIPLY -> System.out.println("multiply");
                case DIVIDE -> System.out.println("divide");
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

        return input;
    }

    private void add() throws Exception {
        String input = processWordInput();

        this.currentWordCalculation.append("+ ");
        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append("; ");
        this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().add(input, this.currentEmbedding);

        String[] similarWords = settingsMenu.getWordsEmbeddings().getSimilarWords(
                this.currentEmbedding,
                settingsMenu.getNumberOfSimilaritiesToFind());

        String heading = settingsMenu.getSettingsAsHeading(true, this.currentWordCalculation.toString());

        ConsolePrint.printHeading(heading);
        dataOutputBufferedWriter.write(heading);
        dataOutputBufferedWriter.newLine();

        for (int i = 0; i < similarWords.length; i++) {
            if (settingsMenu.getAddSimilarityScore()) {
                double score = settingsMenu.getWordsEmbeddings().getPreviousSimilarWordsScores()[i];
                String formattedScore = String.format("%24.18f", score);
                System.out.print(formattedScore + " ");
                dataOutputBufferedWriter.write(formattedScore + " ");
            }

            System.out.print(similarWords[i]);
            dataOutputBufferedWriter.write(similarWords[i]);

            System.out.println();
            dataOutputBufferedWriter.newLine();
        }

        dataOutputBufferedWriter.newLine();
    }

    private void quitWordCalculator() throws IOException {
        this.keepWordCalculatorOpen = false;

        this.dataOutputBufferedWriter.close();

        ConsolePrint.printInfo("Close Word Calculator");
    }
}
