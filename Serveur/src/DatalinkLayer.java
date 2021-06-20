import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class DatalinkLayer extends ProtocolLayer{


    public DatalinkLayer()
    {

    }

    @Override
    public void encapsulation(Packet packet) {

        ArrayList<Byte> packetFrame = new ArrayList<Byte>();
        String[] args = QuoteServerThread.getArgs();


        try{
            //trouver adresse mac
            InetAddress myIp = InetAddress.getLocalHost();
            NetworkInterface iface = NetworkInterface.getByInetAddress(myIp);
            byte[] Mymac = iface.getHardwareAddress();

            InetAddress ipServeur = InetAddress.getLocalHost();
            NetworkInterface ifaceServeur = NetworkInterface.getByInetAddress(myIp);
            byte[] macServeur = ifaceServeur.getHardwareAddress();


            //ajouter adresse mac frame
            for (int i = 0; i < Mymac.length + macServeur.length; ++i)
            {
                if(i<Mymac.length)  packetFrame.add(Mymac[i]);
                else packetFrame.add(macServeur[i - macServeur.length]);
            }

            //ajouter type

            packetFrame.add((byte)0x08);
            packetFrame.add((byte)0x00);
            //ajouter data

            packetFrame.addAll(packet.packet);




            //ajouter CRC
            byte[] temp = new byte[packetFrame.size()];
            for(int i=0;i<packetFrame.size();i++)
            {
                temp[i] = packetFrame.get(i);
            }


            long crc = getCRC32Checksum(temp);
            byte[] crcByte = longToByteArray(crc);
            boolean debut = false;
            for(int i=0;i< crcByte.length;i++)
            {
                if(crcByte[i] != 0) debut = true;
                if(debut) packetFrame.add(crcByte[i]);

            }

            //Envoyer couche physique
            Packet envoyerPacket = new Packet();
            envoyerPacket.setPacket(packetFrame);
            layerDessous.encapsulation(envoyerPacket);



            //affichage
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < packetFrame.size(); i++) {
                sb.append(String.format(
                        "%02X%s", packetFrame.get(i),
                        (i < packetFrame.size() - 1) ? " " : ""));
            }



            // print the final String containing the MAC Address
            System.out.println(sb.toString());



        }
        catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void desencapsulation(Packet packet) {
        //Lorsque on recoit un message

        //Verifier checksum
        Boolean checksum = verifierChecksum(packet);

        //Adresses MAC
        ArrayList<Byte> destination = new ArrayList<>();
        ArrayList<Byte> source = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            destination.add(packet.packet.get(i));
        }

        for(int i = 6; i < 12; i++){
            source.add(packet.packet.get(i));
        }

        ArrayList<Byte> type = new ArrayList<>();

        for(int i = 12; i < 14; i++){
            type.add(packet.packet.get(i));
        }

        ArrayList<Byte> data = new ArrayList<>();

        for(int i = 14; i < packet.packet.size() - 4; i++){
            data.add(packet.packet.get(i));
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < packet.packet.size(); i++) {
            sb.append(String.format(
                    "%02X%s", packet.packet.get(i),
                    (i < packet.packet.size() - 1) ? " " : ""));
        }


        System.out.println("tamere"+sb);
       // System.out.println("donnees : " + data);

    }

    public Boolean verifierChecksum(Packet packet){
        ArrayList<Byte> checksumRecu = new ArrayList<Byte>();

        for(int i = 4; i > 0; i--)
        {
            checksumRecu.add(packet.packet.get(packet.packet.size()-i));
        }

        ArrayList<Byte> restePacquet = new ArrayList<>();

        for(int i = 0; i < packet.packet.size()-4; i++){
            restePacquet.add(packet.packet.get(i));
        }

        byte[] data = packet.arrayToList(restePacquet);
        long checksumCalculeeLong = getCRC32Checksum(data);
        byte[] checksumCalculeeByte = null;
        try{
            checksumCalculeeByte = longToByteArray(checksumCalculeeLong);
        }

        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        ArrayList<Byte>checksumCalculee = new ArrayList<>();
        Boolean debut = false;
        for(int i=0;i< checksumCalculeeByte.length;i++)
        {
            if(checksumCalculeeByte[i] != 0) debut = true;
            if(debut) checksumCalculee.add(checksumCalculeeByte[i]);

        }

        if(checksumRecu.get(0) == checksumCalculee.get(0) && checksumRecu.get(1) == checksumCalculee.get(1) && checksumRecu.get(2) == checksumCalculee.get(2) && checksumRecu.get(3) == checksumCalculee.get(3))
        {
            System.out.println("Bon checksum");
            return true;
        }

        else{
            System.out.println(checksumRecu);
            System.out.println(checksumCalculee);
            System.out.println("Mauvais checksum");
            return false;
        }

    }

    private long getCRC32Checksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    private byte[] longToByteArray ( final long i ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeLong(i);
        dos.flush();
        return bos.toByteArray();
    }

}
