import java.util.ArrayList;
import java.lang.Integer;
import java.math.*;

public class TransportLayer extends ProtocolLayer{


    int seq;
    int ack;
    byte[] ackByte;
    byte[] seqByte;
    ArrayList<Byte> packetTotal;
    int nombreFragments;
    ArrayList<Integer> fragmentsRecus;

    public TransportLayer(){
        seq = 1;
        ack = 0;
        seqByte = new byte[4];
        ackByte = new byte[4];
        nombreFragments = 1;
        fragmentsRecus = new ArrayList<>();
        packetTotal = new ArrayList<>();
    }

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
        ArrayList<Byte> destination = new ArrayList<>();
        ArrayList<Byte> source = new ArrayList<>();
        ArrayList<Byte> seqRes = new ArrayList<>();
        ArrayList<Byte> ackRes = new ArrayList<>();
        ArrayList<Byte> nbFrag = new ArrayList<>();
        ArrayList<Byte> qteFrag = new ArrayList<>();
        ArrayList<Byte> data = new ArrayList<>();


        for(int i = 0; i < 2; i++){
            source.add(packet.packet.get(i));
        }

        for(int i = 2; i < 4; i++){
            destination.add(packet.packet.get(i));
        }

        System.out.println((short) ((source.get(0) << 8) | (source.get(1) & 0xFF)));
        System.out.println((short) ((destination.get(0) << 8) | (destination.get(1) & 0xFF)));

        for(int i = 4; i < 8; i++){
            seqRes.add(packet.packet.get(i));
        }

        for(int i = 8; i < 12; i++){
            ackRes.add(packet.packet.get(i));
        }

        System.out.println(seqRes);
        System.out.println(ackRes);

        for(int i = 12; i < 16; i++){
            nbFrag.add(packet.packet.get(i));
        }

        for(int i = 16; i < 20; i++){
            qteFrag.add(packet.packet.get(i));
        }

        for (int i = 20; i < packet.packet.size(); i++){
            data.add(packet.packet.get(i));
        }

        int tempFrag = nbFrag.get(0) << 8 | nbFrag.get(1) & 0xFF;
        int tempqte = qteFrag.get(0) << 8 | qteFrag.get(1) & 0xFF;

        desencapsulationFragments(data, tempFrag, tempqte);

        System.out.println(nbFrag);
        System.out.println(qteFrag);

    }

    public ArrayList<Byte> desencapsulationFragments(ArrayList<Byte> data, int nbFrag, int qteFrag) {

        Boolean fragAnterieur = false;
        Boolean fragUlterieur = false;

        if(nbFrag > qteFrag || nbFrag < 1){
            System.out.println("erreur fragment trop loin");
            return null;
        }

        if(nombreFragments == 1){
            nombreFragments = qteFrag;
        }

        else{
            if(nombreFragments != qteFrag){
                System.out.println("erreur fragments");
                return null;
            }
        }

        for(int i = 0; i < fragmentsRecus.size(); i++){
            if(fragmentsRecus.get(i) == nbFrag - 1){
                fragAnterieur = true;
            }

            if(fragmentsRecus.get(i) == nbFrag + 1){
                fragUlterieur = true;
            }

            if(fragmentsRecus.get(i) == nbFrag){
                System.out.println("Erreur duplication fragment");
                return null;
            }
        }

        if(fragAnterieur == true && fragUlterieur == true){
            System.out.println("Erreur ordre fragments");
            return null;
        }

        fragmentsRecus.add(nbFrag);

        packetTotal.addAll(data);

        return null;
    };

    public ArrayList<Byte> getPacketTotal(){
        return packetTotal;
    }


}
