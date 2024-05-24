package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.util.SimilarityAlgorithm;

/**
 * turn the SimilarityAlgorithm utility class into a menu
 */
public class SimilarityAlgorithmMenuItem {

    /**
     * look up a keyboard shortcut, and find the appropriate menu item
     * 
     * @param key - the keyboard shortcut to match
     * @return the matching menu item
     * @throws Exception if no matching menu item is found
     */
    public static SimilarityAlgorithm valueOfKey(String key) throws Exception {
        for (SimilarityAlgorithm item : SimilarityAlgorithm.values())
            if (Integer.toString(item.ordinal() + 1).equals(key))
                return item;

        throw new Exception("'" + key + "' is not a valid similarity algorithm.");
    }

    /**
     * print the menu's title to the terminal
     */
    public static void printTitle() {
        ConsolePrint.printTitle("Similarity Algorithms");
    }

    /**
     * print all of the menu options with their shortcuts, to the terminal
     */
    public static void printOptions() {
        String[] options = new String[SimilarityAlgorithm.values().length];
        int index = 0;

        for (SimilarityAlgorithm item : SimilarityAlgorithm.values())
            options[index++] = "[" + (item.ordinal() + 1) + "] " + item.toString();

        ConsolePrint.printMenuOptions(options);
    }

}
