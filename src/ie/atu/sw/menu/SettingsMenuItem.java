package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

public enum SettingsMenuItem {

    EMBEDDINGS_FILE("Word-Embeddings File", null),
    OUTPUT_FILE("Data-Output File (default = ./out.txt)", null),
    CLEAR_OUTPUT_FILE("Empty the Output File", null),
    SIMILARITIES_NUMBER("Number of Similarities to Find", null),
    SIMILARITIES_ALGORITHM("Similarity Algorithm to Use", null),
    ADD_WORD_STATS("Include Word Statistics in Output", null),
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
