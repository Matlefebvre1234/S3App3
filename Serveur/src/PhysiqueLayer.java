import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class PhysiqueLayer extends ProtocolLayer {

    DatagramSocket socket;
    int compteurRecevoir;
    public PhysiqueLayer(){
        compteurRecevoir=0;
        try {
            socket = new DatagramSocket(25000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void encapsulation(Packet packet) {
        byte[] a = new byte[32];
        a[0] = 4;
        a[1] =5;
        String[] args = QuoteServer.getArgs();

        try {

            DatagramSocket socket2 = new DatagramSocket();
            // send request
            InetAddress address = InetAddress.getByName(args[0]);

            byte[] messageFinal = new byte[packet.packet.size()];
            for (int i=0;i<messageFinal.length;i++)
            {
                messageFinal[i] = packet.packet.get(i);

            }
            DatagramPacket message = new DatagramPacket(messageFinal,messageFinal.length, address, 25001);
            System.out.println("ENVOI");

            socket2.send(message);

            compteurRecevoir++;
            if(compteurRecevoir ==2)
            {
                recevoirMessage();
                compteurRecevoir=0;
            }
    }
        catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void recevoirMessage()
    {
        try {
            byte[] rep = new byte[256];
            DatagramPacket packet = new DatagramPacket(rep, rep.length);
            socket.receive(packet);

            byte[] repCorrige = new byte[packet.getLength()];

            for(int i =0;i<repCorrige.length;i++)
            {
                repCorrige[i] = rep[i];
            }
            Packet nouveauPacket = new Packet();
            nouveauPacket.setPacket(repCorrige);
            desencapsulation(nouveauPacket);
            String received = new String(packet.getData(), 0, packet.getLength());


        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void desencapsulation(Packet message) {

        //Reactiver quand le layerDessous (dataLinkLayer) sera configurer
        layerDessus.desencapsulation(message);
    }
}
