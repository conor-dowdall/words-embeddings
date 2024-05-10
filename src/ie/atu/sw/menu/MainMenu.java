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

    public void launchMenu() {
        MainMenuItem.printTitle();
        printOptionsAndProcessInput();
    }

    private void printOptionsAndProcessInput() {
        try {

            MainMenuItem.printOptions();

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

    private void launchSimilarWords(boolean similar) throws Exception {
        if (settingsMenu.getWordsEmbeddings() == null)
            settingsMenu.loadNewWordEmbeddings();

        if (similar)
            ConsolePrint.printHeading("Find Similar Words");
        else
            ConsolePrint.printHeading("Find Dissimilar Words");

        System.out.println("Embeddings File:\t" + settingsMenu.getWordsEmbeddings().getFileName());
        System.out.println("Number of Similarities:\t" + settingsMenu.getNumberOfSimilaritiesToFind());
        System.out.println();

        System.out.print("Enter word(s): ");

        String input = this.inputScanner.nextLine();
        String[] words = input.split(" ");

        for (String word : words) {
            try {

                String[] similarWords = this.settingsMenu.getWordsEmbeddings().getSimilarWords(
                        word,
                        settingsMenu.getNumberOfSimilaritiesToFind(),
                        similar);

                if (similar)
                    ConsolePrint.printHeading("Words Similar To '" + word + "'");
                else
                    ConsolePrint.printHeading("Words Dissimilar To '" + word + "'");

                for (String similarWord : similarWords)
                    System.out.println(similarWord);

            } catch (Exception e) {
                ConsolePrint.printError(e.getMessage());
            }
        }
    }

    private void launchSimilarWords() throws Exception {
        launchSimilarWords(true);
    }

    private void launchDissimilarWords() throws Exception {
        launchSimilarWords(false);
    }

    private void launchWordCalculator() {
    }

    private void launchSettingsMenu() throws Exception {
        settingsMenu.launchMenu();
    }

    private void quitProgram() {
        this.inputScanner.close();
        System.out.println();
        System.out.println("Quit");
        System.out.println();
        System.exit(0);
    }
}
