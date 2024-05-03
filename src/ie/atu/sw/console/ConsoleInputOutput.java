package ie.atu.sw.console;

public class ConsoleInputOutput {
    public static void printHeader() {
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
        System.out.println("*                                                          *");
        System.out.println("*          Similarity Search with Word Embeddings          *");
        System.out.println("*                    by Conor Dowdall                      *");
        System.out.println("*                      ID: 10024983                        *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
    }

    public static void printOptions() {
        System.out.println("(1) Specify Embedding File");
        System.out.println("(2) Specify an Output File (default: ./out.txt)");
        System.out.println("(3) Enter a Word or Text");
        System.out.println("(4) Configure Options");
        System.out.println("(5) Optional Extras");
        System.out.println("(Q) Quit");
    }

    public static void printProgress(int current, int last) {
        ConsoleProgressMeter.printProgress(current, last);
    }

}
