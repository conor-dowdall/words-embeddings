package ie.atu.sw.console;

public class ConsolePrint {
    public static void printTitle(String title) {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.YELLOW_BOLD);
        System.out.println();
        System.out.println();
        System.out.print(title);
        System.out.println();
        System.out.print(ConsoleColour.RESET);
        System.out.println();
    }

    public static void printMenuOptions(String[] options) {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.GREEN_BOLD_BRIGHT);
        System.out.print("Please select from the following options:");
        System.out.println();
        System.out.println();

        for (String option : options)
            System.out.println(option);

        System.out.println();
        System.out.print("Option: ");
        System.out.print(ConsoleColour.RESET);
    }

    public static void printHeading(String heading) {
        System.out.println();
        printWithUnderline(heading);
        System.out.println();
    }

    public static void printWithUnderline(String text) {
        System.out.print(ConsoleColour.BLACK_BACKGROUND);
        System.out.print(ConsoleColour.WHITE_UNDERLINED);
        System.out.print(text);
        System.out.print(ConsoleColour.RESET);
    }

    public static void printError(String error) {
        System.err.println();
        System.err.print(ConsoleColour.BLACK_BACKGROUND);
        System.err.print(ConsoleColour.RED_BOLD_BRIGHT);
        System.err.print("[ERROR] " + error);
        System.err.print(ConsoleColour.RESET);
        System.err.println();
    }
}
