package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

/**
 * enum for word-calculator-menu options
 */
public enum WordCalculatorMenuItem {

    ADD("Add", "+"),
    SUBTRACT("Subtract", "-"),
    MULTIPLY("Multiply", "*"),
    DIVIDE("Divide", "/"),
    QUIT("Close Word Calculator", "q");

    public final String key;
    public final String title;

    /**
     * create a menu item/option
     * 
     * @param title - the title of the menu option to be displayed in terminal
     * @param key   - use 'null' to auto-generate a shortcut based on position in
     *              list, or provide a custom key, like 'q' for quit
     */
    private WordCalculatorMenuItem(String title, String key) {
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
    public static WordCalculatorMenuItem valueOfKey(String key) throws Exception {
        for (WordCalculatorMenuItem item : WordCalculatorMenuItem.values())
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
        String[] options = new String[WordCalculatorMenuItem.values().length];
        int index = 0;

        for (WordCalculatorMenuItem item : WordCalculatorMenuItem.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
