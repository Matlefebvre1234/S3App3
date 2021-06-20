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

    public ArrayList<Byte> encapsulationFragments(Packet packet, int nbFrag, double qteFrag) {

        ArrayList<Byte> packetFuckall = new ArrayList<Byte>();
        ArrayList<Byte> packetSegment = new ArrayList<Byte>();
        String[] args = Client.getArgs();

        byte[] nbFragByte = new byte[4];
        byte[] qteFragByte = new byte[4];

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

        nbFragByte[3] = (byte) nbFrag;
        nbFragByte[2] = (byte) (nbFrag >>> 8);
        nbFragByte[1] = (byte) (nbFrag >>> 16);
        nbFragByte[0] = (byte) (nbFrag >>> 24);

        qteFragByte[3] = (byte) qteFrag;
        qteFragByte[2] = (byte) ((int)qteFrag >>> 8);
        qteFragByte[1] = (byte) ((int)qteFrag >>> 16);
        qteFragByte[0] = (byte) ((int)qteFrag >>> 24);

        packetSegment.add(portSource1);
        packetSegment.add(portSource2);

        packetSegment.add(portDestination1);
        packetSegment.add(portDestination2);

        packetSegment.addAll(packet.listToArray(seqByte));
        packetSegment.addAll(packet.listToArray(ackByte));

        packetSegment.addAll(packet.listToArray(nbFragByte));
        packetSegment.addAll(packet.listToArray(qteFragByte));

        packetFuckall.addAll(packet.listToArray(nbFragByte));
        packetFuckall.addAll(packet.listToArray(qteFragByte));


        packetSegment.addAll(packet.packet);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < packetFuckall.size(); i++) {
            sb.append(String.format(
                    "%02X%s", packetFuckall.get(i),
                    (i < packetFuckall.size() - 1) ? " " : ""));
        }

        System.out.println(sb);

        return packetFuckall;
    }

    @Override
    public void encapsulation(Packet packet) {

        double nbFragments = 1;
        ArrayList<Packet> listPackets = new ArrayList<>();

        if(packet.packet.size() > 200)
        {
            nbFragments = Math.ceil(packet.packet.size()/(float)200);
        }


        for(int i = 0; i < nbFragments-1; i++){

            Packet temp = new Packet();
            for(int y = 200*i; y< 200*i + 199; y++){
                temp.packet.add(packet.packet.get(y));
            }
            listPackets.add(temp);
        }

        Packet temp = new Packet();
        for(double i = 200*(nbFragments-1) ; i < packet.packet.size(); i++)
        {
            temp.packet.add(packet.packet.get((int)i));
        }

        listPackets.add(temp);

        for(int i = 0; i < nbFragments; i++){
            encapsulationFragments(listPackets.get(i), i, nbFragments);
        }
        //StringBuilder sb = new StringBuilder();
        //for (int i = 0; i < listPackets.get(0).packet.size(); i++) {
        //    sb.append(String.format(
        //            "%02X%s", listPackets.get(0).packet.get(i),
        //            (i < listPackets.get(0).packet.size() - 1) ? " " : ""));
        //}
//
        //StringBuilder sb2 = new StringBuilder();
        //for (int i = 0; i < listPackets.get(1).packet.size(); i++) {
        //    sb2.append(String.format(
        //            "%02X%s", listPackets.get(1).packet.get(i),
        //            (i < listPackets.get(1).packet.size() - 1) ? " " : ""));
        //}
//
        //System.out.println(sb);
        //System.out.println("Sale pute");
        //System.out.println("alllo" + sb2);
    }

    @Override
    public void desencapsulation(Packet packet) {

    }


}
