import java.io.*;
import java.net.*;
import java.util.*;

/**
 * thread du serveur
 */
public class QuoteServerThread extends Thread {

    protected boolean stop = false;

    public QuoteServerThread() throws IOException {
        this("QuoteServerThread");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);


    }

    /**
     * loop du serveur
     */
    public void run() {

        FactoryCommunicationMaison factory = new FactoryCommunicationMaison();
        Communication communication = factory.creerCommunication();

         do
        {
            communication.RecevoirStart();

        }while (!stop);

    }


    }

