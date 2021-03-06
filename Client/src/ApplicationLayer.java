import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Classe Application layer
 */
public class ApplicationLayer extends ProtocolLayer{

    FileWriter myWriter;
    int indexPacket;
    boolean nomRecu;
    ScheduledExecutorService executorapp;
    boolean ReadyForNextPacket;
    public ApplicationLayer()
    {
        nomRecu =false;
        ReadyForNextPacket = false;
        indexPacket =0;

    }

    /**
     * envoyer un fichier au serveur
     * @param nomFichier
     */
    public void envoyerFichierServeur(String nomFichier)
    {

        byte[] fichierenByte = lireFichier(nomFichier).toString().getBytes();
        byte[] nomFichierByte = nomFichier.getBytes();

        ArrayList<byte[]> nomEtFichier = new ArrayList<byte[]>();

        nomEtFichier.add(nomFichierByte);
        nomEtFichier.add(fichierenByte);

        //Envoyer nom et Packet
        Runnable envoyerPacket2 = new Runnable() {
            public void run() {
                if(layerDessous.getReadyForNextPacket() )
                {
                    Packet packet = new Packet();
                    packet.setPacket(nomEtFichier.get(getIndexPacket()));
                    IncrementeIndexPacket();
                    encapsulation(packet);
                }
                if(getIndexPacket() >=2) executorapp.shutdown();

            }
        };
        executorapp = Executors.newScheduledThreadPool(1);
        executorapp.scheduleAtFixedRate(envoyerPacket2, 0, 200, TimeUnit.MILLISECONDS);
    };

    /**
     *
     * @return indexPAcket
     */
    private int getIndexPacket()
    {
        return indexPacket;
    }

    /**
     * incrementeIndexPAcket
     */
    private void IncrementeIndexPacket()
    {
        indexPacket++;
    }

    /**
     * sauvegarde packet en fichier
     * @param packet
     */
    public  void sauvegarderPacketFichier(Packet packet)
    {
        try {
            byte[] message = packet.getByte();
            System.out.println(packet.packet.get(0));
            myWriter.write(new String(message),0,message.length-1);
            myWriter.close();
            System.out.println("Fichier Sauvegarder Avec succes");
        } catch (IOException e) {
            System.out.println("Erreur");
            e.printStackTrace();
        }
    }

    /**
     * Creer un fichuier
     * @param packet
     */
    private void creerFichier(Packet packet)
    {
        byte[] nomFichier = packet.getByte();
        String nomFichierString  = new String(nomFichier,0,nomFichier.length-1);
        try {
            myWriter = new FileWriter(nomFichierString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void encapsulation(Packet packet) {

        layerDessous.encapsulation(packet);

    }

    /**
     * encapsule les data
     * @param nomFichier
     */
    public void encapsulation(String nomFichier) {
        envoyerFichierServeur(nomFichier);

    }

    /**
     * desencapsule les data
     * @param packet
     */
    @Override
    public void desencapsulation(Packet packet) {

        if (!nomRecu)creerFichier(packet);
        if (nomRecu){
            sauvegarderPacketFichier(packet);
            nomRecu = false;
        }

    }

    /**
     * lit un fichier
     * @param nomFichier
     * @return
     */
    private StringBuffer lireFichier(String nomFichier)
    {
        try
        {
            // Le fichier d'entr??e
            File file = new File(nomFichier);
            // Cr??er l'objet File Reader
            FileReader myfilereader = new FileReader(file);
            // Cr??er l'objet BufferedReader
            BufferedReader mybufferedreader = new BufferedReader(myfilereader);
            StringBuffer stringsauvegarder = new StringBuffer();
            String line;
            while((line = mybufferedreader.readLine()) != null)
            {
                // ajoute la ligne au buffer
                stringsauvegarder.append(line);
                stringsauvegarder.append("\n");
            }
            myfilereader.close();
           return stringsauvegarder;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
