package ie.atu.sw.menu;

import ie.atu.sw.console.ConsoleColour;

public enum MainMenuItem {

    EMBEDDINGS_FILE("Load Word-Embeddings File", null),
    OUTPUT_FILE("Specify Data-Output File (default = ./out.txt)", null),
    SIMILAR_WORDS("Launch 'Find Similar Words'", null),
    DISSIMILAR_WORDS("Launch 'Find Dissimilar Words'", null),
    WORD_CALCULATOR("Launch 'Word Calculator'", null),
    SETTINGS("Settings", null),
    QUIT("Quit", "q");

    public final String key;
    public final String title;

    private MainMenuItem(String title, String key) {
        this.title = title;
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    public static MainMenuItem valueOfKey(String key) throws Exception {
        for (MainMenuItem item : MainMenuItem.values())
            if (item.key.equals(key.toLowerCase()))
                return item;

        throw new Exception("'" + key + "' is not a valid menu option.");
    }

    @Override
    public String toString() {
        return "[" + this.key + "] " + this.title;
    }

    public static void printHeader() {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.GREEN_BOLD_BRIGHT);
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
        System.out.println("*                                                          *");
        System.out.println("*          Similarity Search with Word Embeddings          *");
        System.out.println("*                    by Conor Dowdall                      *");
        System.out.println("*                      ID: 10024983                        *");
        System.out.println("*                                                          *");
        System.out.print("************************************************************");
        System.out.print(ConsoleColour.RESET);
        System.out.println();
    }

    public static void printOptions() {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.GREEN_BOLD_BRIGHT);
        System.out.print("Please select from the following options:");
        System.out.println();

        for (MainMenuItem item : MainMenuItem.values())
            System.out.println(item);

        System.out.println();
        System.out.print("Option: ");
        System.out.print(ConsoleColour.RESET);
    }
}
