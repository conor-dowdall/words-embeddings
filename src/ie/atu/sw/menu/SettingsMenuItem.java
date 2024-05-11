package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

public enum SettingsMenuItem {

    EMBEDDINGS_FILE("Load Word-Embeddings File", null),
    SIMILARITIES_NUMBER("Number of Similarities to Find", null),
    SIMILARITIES_ALGORITHM("Similarity Algorithm to Use", null),
    TOGGLE_SIMILARITY_SCORE("Toggle Similarity Score in Output", null),
    OUTPUT_FILE("Specify Data-Output File", null),
    TOGGLE_APPEND("Toggle Append/Overwrite Data-Output File", null),
    EMPTY_OUTPUT_FILE("Empty the Output File", null),
    RESET("Reset Settings to Defaults", null),
    QUIT("Close Settings", "q");

    public final String key;
    public final String title;

    private SettingsMenuItem(String title, String key) {
        this.title = title;
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    public static SettingsMenuItem valueOfKey(String key) throws Exception {
        for (SettingsMenuItem item : SettingsMenuItem.values())
            if (item.key.equals(key.toLowerCase()))
                return item;

        throw new Exception("'" + key + "' is not a valid menu option.");
    }

    @Override
    public String toString() {
        return "[" + this.key + "] " + this.title;
    }

    public static void printTitle() {
        ConsolePrint.printTitle("Settings");
    }

    public static void printOptions() {
        String[] options = new String[SettingsMenuItem.values().length];
        int index = 0;

        for (SettingsMenuItem item : SettingsMenuItem.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
