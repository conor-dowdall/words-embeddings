package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

public enum WordCalculatorMenuItem {

    ADD("Add", "+"),
    SUBTRACT("Subtract", "-"),
    MULTIPLY("Multiply", "*"),
    DIVIDE("Divide", "/"),
    QUIT("Close Word Calculator", "q");

    public final String key;
    public final String title;

    private WordCalculatorMenuItem(String title, String key) {
        this.title = title;
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    public static WordCalculatorMenuItem valueOfKey(String key) throws Exception {
        for (WordCalculatorMenuItem item : WordCalculatorMenuItem.values())
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
        String[] options = new String[WordCalculatorMenuItem.values().length];
        int index = 0;

        for (WordCalculatorMenuItem item : WordCalculatorMenuItem.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
