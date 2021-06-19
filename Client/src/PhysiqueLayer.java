import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class PhysiqueLayer extends ProtocolLayer {

    DatagramSocket socket;
    public PhysiqueLayer(){
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Byte> encapsulation(Packet packet) {
        byte[] a = new byte[32];
        a[0] = 4;
        a[1] =5;

        String[] args = Client.getArgs();
        try {
            // get a datagram socket
           socket = new DatagramSocket();

            // send request
            byte[] t = new byte[256];
            String allo = "allo";
            a = allo.getBytes();

            byte[] b = new byte[8];
            b[0] = 2;
            b[1] = 0B0100;
            InetAddress address = InetAddress.getByName(args[0]);

            byte[] messageFinal = new byte[packet.packet.size()];
            for (int i=0;i<messageFinal.length;i++)
            {
                messageFinal[i] = packet.packet.get(i);

            }
            DatagramPacket message = new DatagramPacket(messageFinal,messageFinal.length, address, 4445);
            socket.send(message);
        }
        catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet.packet;
    }

    public void recevoirMessage()
    {
        try {
            byte[] rep = new byte[256];
            DatagramPacket packet = new DatagramPacket(rep, rep.length);
            socket.receive(packet);

            //*display response
            Packet nouveauPacket = new Packet();
            nouveauPacket.setPacket(packet.getData());
            desencapsulation(nouveauPacket);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Serveur:"+ received);

            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void desencapsulation(Packet message) {

        //Reactiver quand le layerDessous (dataLinkLayer) sera configurer
       // layerDessus.desencapsulation(packet);
    }
}
