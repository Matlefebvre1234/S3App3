import java.io.*;
import java.nio.charset.StandardCharsets;

public class ApplicationLayer extends ProtocolLayer{

    FileWriter myWriter;
    boolean nomRecu;
    public ApplicationLayer()
    {
        nomRecu = false;
    }



    public void envoyerFichierServeur(String nomFichier)
    {
        byte[] fichierenByte = lireFichier(nomFichier).toString().getBytes();
        Packet packet = new Packet();
        packet.setPacket(fichierenByte);
        this.encapsulation(packet);
    };


    public  void sauvegarderPacketFichier(Packet packet)
    {
        try {

            byte[] message = packet.getByte();
            myWriter.write(new String(message),0,message.length);
            myWriter.close();
            nomRecu = false;
            System.out.println("Fichier Sauvegarder Avec succes");
        } catch (IOException e) {
            System.out.println("Erreur");
            e.printStackTrace();
        }
    }

    @Override
    public void encapsulation(Packet packet) {

        layerDessous.encapsulation(packet);

    }

    public void encapsulation(String nomFichier) {
        envoyerFichierServeur(nomFichier);

    }

    @Override
    public void desencapsulation(Packet packet) {


        if (!nomRecu)creerFichier(packet);
        else if (nomRecu) sauvegarderPacketFichier(packet);
    }

    @Override
    public void resetLayerDessus() {
        nomRecu = false;
    }


    private void creerFichier(Packet packet)
    {

        byte[] nomFichier = packet.getByte();
       String nomFichierString  = new String(nomFichier,0,nomFichier.length);
        try {
            myWriter = new FileWriter(nomFichierString);
            nomRecu = true;
            System.out.println("Fichier "+nomFichierString+" En modification");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StringBuffer lireFichier(String nomFichier)
    {
        try
        {
            // Le fichier d'entrée
            File file = new File(nomFichier);
            // Créer l'objet File Reader
            FileReader myfilereader = new FileReader(file);
            // Créer l'objet BufferedReader
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
