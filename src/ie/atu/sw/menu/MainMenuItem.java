package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

/**
 * enum for main-menu options with auto-generated shortcuts
 */
public enum MainMenuItem {

    SIMILAR_WORDS("Launch 'Find Similar Words'", null),
    DISSIMILAR_WORDS("Launch 'Find Dissimilar Words'", null),
    WORD_CALCULATOR("Launch 'Word Calculator'", null),
    SETTINGS("Settings", null),
    QUIT("Quit", "q");

    public final String key;
    public final String title;

    /**
     * create a menu item/option
     * 
     * @param title - the title of the menu option to be displayed in terminal
     * @param key   - use 'null' to auto-generate a shortcut based on position in
     *              list, or provide a custom key, like 'q' for quit
     */
    private MainMenuItem(String title, String key) {
        this.title = title;
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    /**
     * look up a keyboard shortcut, and find the appropriate menu item
     * 
     * @param key - the keyboard shortcut to match
     * @return the matching menu item
     * @throws Exception if no matching menu item is found
     */
    public static MainMenuItem valueOfKey(String key) throws Exception {
        for (MainMenuItem item : MainMenuItem.values())
            if (item.key.equals(key.toLowerCase()))
                return item;

        throw new Exception("'" + key + "' is not a valid menu option.");
    }

    /**
     * print the menu item alongside it's keyboard shortcut, to the terminal
     */
    @Override
    public String toString() {
        return "[" + this.key + "] " + this.title;
    }

    /**
     * print the menu's title to the terminal
     */
    public static void printTitle() {
        ConsolePrint.printTitle("Words Embeddings Text Interface");
    }

    /**
     * print all of the menu options with their shortcuts, to the terminal
     */
    public static void printOptions() {
        String[] options = new String[MainMenuItem.values().length];
        int index = 0;

        for (MainMenuItem item : MainMenuItem.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
