package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.util.SimilarityAlgorithm;

public class SimilarityAlgorithmMenuItem {

    public static SimilarityAlgorithm valueOfKey(String key) throws Exception {
        for (SimilarityAlgorithm item : SimilarityAlgorithm.values())
            if (Integer.toString(item.ordinal() + 1).equals(key))
                return item;

        throw new Exception("'" + key + "' is not a valid similarity algorithm.");
    }

    public static void printTitle() {
        ConsolePrint.printTitle("Similarity Algorithms");
    }

    public static void printOptions() {
        String[] options = new String[SimilarityAlgorithm.values().length];
        int index = 0;

        for (SimilarityAlgorithm item : SimilarityAlgorithm.values())
            options[index++] = "[" + (item.ordinal() + 1) + "] " + item.toString();

        ConsolePrint.printMenuOptions(options);
    }

}
