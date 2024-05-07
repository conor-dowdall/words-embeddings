package ie.atu.sw.menu;

import ie.atu.sw.console.ConsoleColour;

public enum MainMenuItem {

    EMBEDDINGS_FILE("1", "Specify Embeddings File"),
    OUTPUT_FILE("2", "Change Output File (default = ./out.txt)"),
    SIMILAR_WORDS("3", "Find Similar Words"),
    SETTINGS("4", "Settings"),
    QUIT("Q", "Quit");

    public final String key;
    public final String title;

    private MainMenuItem(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public static MainMenuItem valueOfKey(String key) throws Exception {
        for (MainMenuItem item : MainMenuItem.values())
            if (item.key.equals(key.toUpperCase()))
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
        System.out.println(ConsoleColour.RESET);
    }

    public static void printOptions() {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.GREEN_BOLD_BRIGHT);
        System.out.println();
        System.out.println("Please select from the following options:");
        System.out.println();

        for (MainMenuItem item : MainMenuItem.values())
            System.out.println(item);

        System.out.println();
        System.out.print("Option: ");
        System.out.print(ConsoleColour.RESET);
    }
}
