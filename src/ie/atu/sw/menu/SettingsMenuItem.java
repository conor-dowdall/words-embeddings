package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

/**
 * enum for settings-menu options with auto-generated shortcuts
 */
public enum SettingsMenuItem {

    EMBEDDINGS_FILE("Load Word-Embeddings File", null),
    SIMILARITIES_NUMBER("Number of Similarities to Find", null),
    SIMILARITIES_ALGORITHM("Similarity Algorithm to Use", null),
    TOGGLE_SIMILARITY_SCORE("Toggle Similarity Score in Output", null),
    OUTPUT_FILE("Specify Data-Output File", null),
    TOGGLE_APPEND("Toggle Append/Overwrite Data-Output File", null),
    EMPTY_OUTPUT_FILE("Empty the Output File", null),
    RESET("Reset Settings to Defaults", null),
    PRINT("Print Current Settings", null),
    QUIT("Close Settings", "q");

    public final String key;
    public final String title;

    /**
     * create a menu item/option
     * 
     * @param title - the title of the menu option to be displayed in terminal
     * @param key   - use 'null' to auto-generate a shortcut based on position in
     *              list, or provide a custom key, like 'q' for quit
     */
    private SettingsMenuItem(String title, String key) {
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
    public static SettingsMenuItem valueOfKey(String key) throws Exception {
        for (SettingsMenuItem item : SettingsMenuItem.values())
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
        ConsolePrint.printTitle("Settings");
    }

    /**
     * print all of the menu options with their shortcuts, to the terminal
     */
    public static void printOptions() {
        String[] options = new String[SettingsMenuItem.values().length];
        int index = 0;

        for (SettingsMenuItem item : SettingsMenuItem.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
