import java.io.*;

/**
 * main
 */
public class QuoteServer {

    private static String[] savedArgs;
    public static String[] getArgs() {
        return savedArgs;
    }
    public static void main(String[] args) throws IOException {

        savedArgs = args;
        new QuoteServerThread().start();

    }
}