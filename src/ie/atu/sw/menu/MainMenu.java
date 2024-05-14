package ie.atu.sw.menu;

import java.nio.file.NoSuchFileException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

public class MainMenu {
    private Scanner inputScanner = new Scanner(System.in);
    private SettingsMenu settingsMenu;

    public MainMenu() {
        this.settingsMenu = new SettingsMenu(this.inputScanner);
    }

    public void launchMenu() throws Exception {
        MainMenuItem.printTitle();
        MainMenuItem.printOptions();

        processInput();
    }

    private void processInput() throws Exception {
        try {

            String input = this.inputScanner.nextLine();
            MainMenuItem item = MainMenuItem.valueOfKey(input);

            switch (item) {
                case SIMILAR_WORDS -> launchSimilarWords();
                case DISSIMILAR_WORDS -> launchDissimilarWords();
                case WORD_CALCULATOR -> launchWordCalculator();
                case SETTINGS -> launchSettingsMenu();
                case QUIT -> quitProgram();
                default -> throw new Exception("This should never happen!");
            }

        } catch (NoSuchFileException e) {
            ConsolePrint.printError("File not found: '" + e.getMessage() + "'");
        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            launchMenu();
        }

    }

    private void launchSimilarWords() throws Exception {
        launchSimilarWords(true);
    }

    private void launchDissimilarWords() throws Exception {
        launchSimilarWords(false);
    }

    private void launchSimilarWords(boolean similar) throws Exception {
        settingsMenu.initWordsEmbeddings();

        if (similar)
            ConsolePrint.printTitle("Find Similar Words");
        else
            ConsolePrint.printTitle("Find Dissimilar Words");

        settingsMenu.printSettings();

        System.out.print("Enter word(s): ");

        String input = this.inputScanner.nextLine();
        String sanitizedInput = input.replaceAll("[\s]+", " ").toLowerCase();
        ConsolePrint.printInfo("Interpreting input as: '" + sanitizedInput + "'");
        String[] words = sanitizedInput.split(" ");

        for (String word : words) {

            try {

                settingsMenu.getWordsEmbeddings().getSimilarWords(
                        word,
                        settingsMenu.getNumberOfSimilaritiesToFind(),
                        similar);

                settingsMenu.printDataOutput(word, similar);

            } catch (Exception e) {
                ConsolePrint.printError(e.getMessage());
            }

        }

    }

    private void launchWordCalculator() throws Exception {
        new WordCalculatorMenu(this.inputScanner, this.settingsMenu).launchMenu();
    }

    private void launchSettingsMenu() throws Exception {
        settingsMenu.launchMenu();
    }

    private void quitProgram() {
        this.inputScanner.close();
        ConsolePrint.printInfo("Quit");
        System.out.println();
        System.exit(0);
    }
}
