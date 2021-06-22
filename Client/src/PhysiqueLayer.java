import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * physique layer class
 */
public class PhysiqueLayer extends ProtocolLayer {

     DatagramSocket socket;
    public PhysiqueLayer(){
        try {
            socket = new DatagramSocket(25001);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * encapsule les données
     * @param packet
     */
    @Override
    public void encapsulation(Packet packet) {
        byte[] a = new byte[32];
        a[0] = 4;
        a[1] =5;

        String[] args = Client.getArgs();
        try {

            DatagramSocket socket2 = new DatagramSocket();
            // send request
            InetAddress address = InetAddress.getByName(args[0]);

            byte[] messageFinal = new byte[packet.packet.size()];
            for (int i=0;i<messageFinal.length;i++)
            {
                messageFinal[i] = packet.packet.get(i);

            }
            DatagramPacket message = new DatagramPacket(messageFinal,messageFinal.length, address, 25000);


            socket2.send(message);

            socket.setSoTimeout(3000);
            recevoirMessage();
            recevoirMessage();



        }
        catch (SocketException e) {
            System.out.println("asda");
        } catch (UnknownHostException e) {
            System.out.println("a");
        } catch (IOException e) {
            System.out.println("a");
        }


    }

    /**
     * reccevoir des message par le socket
     * @throws SocketTimeoutException
     */
    public void recevoirMessage() throws SocketTimeoutException
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
            //System.out.println("Serveur:"+ received);

        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * desencapsule les donnees
     * @param message
     */
    @Override
    public void desencapsulation(Packet message){

        //Reactiver quand le layerDessous (dataLinkLayer) sera configurer
        layerDessus.desencapsulation(message);
    }
}
