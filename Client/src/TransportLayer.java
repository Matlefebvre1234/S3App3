import java.util.ArrayList;
import java.lang.Integer;
import java.math.*;

public class TransportLayer extends ProtocolLayer{


    int seq = 1;
    int ack = 0;
    byte[] ackByte = new byte[4];
    byte[] seqByte = new byte[4];

    public String envoyerNomFichier() {
        String nomFichier = "allo";
        return nomFichier;
    };

    @Override
    public ArrayList<Byte> encapsulation(Packet packet) {

        double nbSegments = 1;

        ArrayList<Byte> packetFuckall = new ArrayList<Byte>();
        ArrayList<Byte> packetSegment = new ArrayList<Byte>();
        String[] args = Client.getArgs();

        Byte portSource1 = (byte)0x61;
        Byte portSource2 = (byte)0xa8;
        Byte portDestination1 = (byte)0x61;
        Byte portDestination2 = (byte)0xa9;

        ack = packet.packet.size() + seq;

        ackByte[3] = (byte) ack;
        ackByte[2] = (byte) (ack >>> 8);
        ackByte[1] = (byte) (ack >>> 16);
        ackByte[0] = (byte) (ack >>> 24);

        seqByte[3] = (byte) seq;
        seqByte[2] = (byte) (seq >>> 8);
        seqByte[1] = (byte) (seq >>> 16);
        seqByte[0] = (byte) (seq >>> 24);

        packetSegment.add(portSource1);
        packetSegment.add(portSource2);

        packetSegment.add(portDestination1);
        packetSegment.add(portDestination2);

        packetSegment.addAll(packet.listToArray(seqByte));
        packetSegment.addAll(packet.listToArray(ackByte));

        if(packet.packet.size() > 200)
        {
            nbSegments = Math.ceil(packet.packet.size()/200);
        }

        packetSegment.addAll(packet.packet);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < packetSegment.size(); i++) {
            sb.append(String.format(
                    "%02X%s", packetSegment.get(i),
                    (i < packetSegment.size() - 1) ? " " : ""));
        }

        System.out.println(sb);

        return packetFuckall;
    }

    @Override
    public void desencapsulation(Packet packet) {

    }


}
