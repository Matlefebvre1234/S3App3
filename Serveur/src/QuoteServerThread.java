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

         do
        {
            byte[] buf = new byte[32];
            // receive request
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());

                for (byte c: packet.getData()
                ) {
                    System.out.print(Integer.toBinaryString(c) + " ");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            // send the response to the client at "address" and "port"
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String reponse = "reponse Serveur";
            byte[] rep = reponse.getBytes();
            packet = new DatagramPacket(rep, rep.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (!stop);

    }


    }

