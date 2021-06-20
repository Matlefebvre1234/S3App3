import java.io.*;
import java.net.*;
import java.util.*;

public class QuoteServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean stop = false;

    public QuoteServerThread() throws IOException {
        this("QuoteServerThread");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);

    }


    public void run() {

        ApplicationLayer applicationLayer = new ApplicationLayer();
        PhysiqueLayer physique = new PhysiqueLayer();
        DatalinkLayer datalink = new DatalinkLayer();

        applicationLayer.setLayerDessous(datalink);
        datalink.setLayerDessus(applicationLayer);
        physique.setLayerDessus(datalink);
        datalink.setLayerDessous(physique);


         do
        {
            physique.recevoirMessage();

        }while (!stop);

    }


    }

