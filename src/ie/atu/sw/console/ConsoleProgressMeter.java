package ie.atu.sw.console;

/*
   * Terminal Progress Meter
   * -----------------------
   * You might find the progress meter below useful. The progress effect
   * works best if you call this method from inside a loop and do not call
   * System.out.println(....) until the progress meter is finished.
   * 
   * Please note the following carefully:
   * 
   * 1) The progress meter will NOT work in the Eclipse console, but will
   * work on Windows (DOS), Mac and Linux terminals.
   * 
   * 2) The meter works by using the line feed character "\r" to return to
   * the start of the current line and writes out the updated progress
   * over the existing information. If you output any text between
   * calling this method, i.e. System.out.println(....), then the next
   * call to the progress meter will output the status to the next line.
   * 
   * 3) If the variable size is greater than the terminal width, a new line
   * escape character "\n" will be automatically added and the meter won't
   * work properly.
   * 
   * 
   */
public class ConsoleProgressMeter {
    public static void printProgress(int index, int total) {
        if (index > total)
            return; // Out of range

        int size = 50; // Must be less than console width
        char done = '█'; // Change to whatever you like.
        char todo = '░'; // Change to whatever you like.

        // Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;

        /*
         * A StringBuilder should be used for string concatenation inside a
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++)
            sb.append(i < completeLen ? done : todo);

        /*
         * The line feed escape character "\r" returns the cursor to the
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        ConsoleColor consoleColor = ConsoleColor.YELLOW;
        if (complete > 80)
            consoleColor = ConsoleColor.GREEN_BOLD_BRIGHT;
        else if (complete > 50)
            consoleColor = ConsoleColor.GREEN_BRIGHT;

        System.out.print(
                "\r" + ConsoleColor.BLACK_BACKGROUND + consoleColor + sb + "] " + complete + "%");

        // Once the meter reaches its max, move to a new line.
        if (index == total)
            System.out.println(ConsoleColor.RESET);
    }
}
