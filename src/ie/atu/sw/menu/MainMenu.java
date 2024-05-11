package ie.atu.sw.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
        printOptionsAndProcessInput();
    }

    private void printOptionsAndProcessInput() throws Exception {
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

    private void launchSimilarWords() throws Exception {
        launchSimilarWords(true);
    }

    private void launchDissimilarWords() throws Exception {
        launchSimilarWords(false);
    }

    private void launchSimilarWords(boolean similar) throws Exception {
        if (settingsMenu.getWordsEmbeddings() == null)
            settingsMenu.loadNewWordsEmbeddingsFile();

        if (similar)
            ConsolePrint.printTitle("Find Similar Words");
        else
            ConsolePrint.printTitle("Find Dissimilar Words");

        settingsMenu.printSettings();

        System.out.print("Enter word(s): ");

        String input = this.inputScanner.nextLine();
        String[] words = input.split(" ");

        File dataOutputFile = new File(settingsMenu.getDataOutputFileName());
        FileWriter dataOutputFileWriter = new FileWriter(dataOutputFile, settingsMenu.getAppendDataOutputFile());
        BufferedWriter dataOutputBufferedWriter = new BufferedWriter(dataOutputFileWriter);

        for (String word : words) {

            try {

                String[] similarWords = this.settingsMenu.getWordsEmbeddings().getSimilarWords(
                        word,
                        settingsMenu.getNumberOfSimilaritiesToFind(),
                        similar);

                String heading = settingsMenu.getNumberOfSimilaritiesToFind() + " ";
                heading += similar
                        ? "Words Similar To '"
                        : "Words Dissimilar To '";
                heading += word + "'";

                ConsolePrint.printHeading(heading);
                dataOutputBufferedWriter.write(heading);
                dataOutputBufferedWriter.newLine();

                for (int i = 0; i < similarWords.length; i++) {
                    String similarWordsCount = (i + 1) + ") ";
                    System.out.print(similarWordsCount);
                    dataOutputBufferedWriter.write(similarWordsCount);

                    System.out.print(similarWords[i]);
                    dataOutputBufferedWriter.write(similarWords[i]);

                    if (settingsMenu.getAddSimilarityScore()) {
                        String score = " " + Double
                                .toString(settingsMenu.getWordsEmbeddings().getPreviousSimilarWordsScores()[i]);
                        System.out.println(score);
                        dataOutputBufferedWriter.write(score);
                    }

                    System.out.println();
                    dataOutputBufferedWriter.newLine();
                }
                dataOutputBufferedWriter.newLine();

            } catch (Exception e) {
                ConsolePrint.printError(e.getMessage());
            }

        }

        dataOutputBufferedWriter.close();
    }

    private void launchWordCalculator() {
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
