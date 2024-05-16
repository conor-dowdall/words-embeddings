package ie.atu.sw.console;

public class ConsolePrint {
    public static void printTitle(String title) {
        System.out.print(ConsoleColor.BLACK_BACKGROUND);
        System.out.print(ConsoleColor.CYAN_BOLD);
        System.out.println();
        System.out.println();
        System.out.print("[TITLE] " + title);
        System.out.println();
        System.out.print(ConsoleColor.RESET);
        System.out.println();
    }

    public static void printMenuOptions(String[] options) {
        System.out.print(ConsoleColor.BLACK_BACKGROUND);
        System.out.print(ConsoleColor.GREEN_BOLD_BRIGHT);
        System.out.print("[OPTIONS] Please select from the following:");
        System.out.println();
        System.out.println();

        for (String option : options)
            System.out.println(option);

        System.out.println();
        System.out.print("Option: ");
        System.out.print(ConsoleColor.RESET);
    }

    public static void printHeading(String heading) {
        System.out.println();
        printWithUnderline(heading);
        System.out.println();
    }

    public static void printWithUnderline(String text) {
        System.out.print(ConsoleColor.BLACK_BACKGROUND);
        System.out.print(ConsoleColor.WHITE_UNDERLINED);
        System.out.print(text);
        System.out.print(ConsoleColor.RESET);
    }

    public static void printInfo(String info) {
        System.out.println();
        System.out.print(ConsoleColor.BLACK_BACKGROUND);
        System.out.print(ConsoleColor.PURPLE_BOLD_BRIGHT);
        System.out.print("[INFO] " + info);
        System.out.print(ConsoleColor.RESET);
        System.out.println();
    }

    public static void printWarning(String warning) {
        System.out.println();
        System.out.print(ConsoleColor.BLACK_BACKGROUND);
        System.out.print(ConsoleColor.YELLOW_BOLD_BRIGHT);
        System.out.print("[WARNING] " + warning);
        System.out.print(ConsoleColor.RESET);
        System.out.println();
    }

    public static void printError(String error) {
        System.err.println();
        System.err.print(ConsoleColor.BLACK_BACKGROUND);
        System.err.print(ConsoleColor.RED_BOLD_BRIGHT);
        System.err.print("[ERROR] " + error);
        System.err.print(ConsoleColor.RESET);
        System.err.println();
    }
}
