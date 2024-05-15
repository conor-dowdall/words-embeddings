package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

public enum MainMenuItem {

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

    public static void printTitle() {
        ConsolePrint.printTitle("Words Embeddings Text Interface");
    }

    public static void printOptions() {
        String[] options = new String[MainMenuItem.values().length];
        int index = 0;

        for (MainMenuItem item : MainMenuItem.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
