import java.io.*;
import java.util.ArrayList;

public class Client {

    private static String[] savedArgs;
    public static String[] getArgs() {
        return savedArgs;
    }


    public static void main( String[] args) throws IOException {
        savedArgs = args;
        if (args.length != 2) {
            System.out.println("nom hote et fichier non fourni");
            return;
        }
        FactoryCommunicationMaison factory = new FactoryCommunicationMaison();
        Communication communication = factory.creerCommunication();

        communication.start(args[1]);






    }

}