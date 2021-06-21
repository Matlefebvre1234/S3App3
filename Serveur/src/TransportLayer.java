import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TransportLayer extends ProtocolLayer{


    int ack;
    byte[] ackByte;
    byte[] seqByte;
    ArrayList<Byte> packetTotal;
    int nombreFragments;
    ArrayList<Integer> fragmentsRecus;
    int portSource;
    int portDestination;

    public TransportLayer(){
        ack = 1;
        seqByte = new byte[4];
        ackByte = new byte[4];
        nombreFragments = 1;
        fragmentsRecus = new ArrayList<>();
        packetTotal = new ArrayList<>();
        portSource = 25001;
        portDestination = 25000;
    }

    public ArrayList<Byte> encapsulationFragments(Packet packet, int nbFrag, double qteFrag) {

       /* ArrayList<Byte> packetSegment = new ArrayList<Byte>();
        String[] args = QuoteServer.getArgs();

        byte[] nbFragByte;
        byte[] qteFragByte;

        ackByte = convertIntToByteArray2(ack);

        nbFragByte = convertIntToByteArray2(nbFrag);

        qteFragByte = convertIntToByteArray2((int)qteFrag);

        packetSegment.addAll(packet.listToArray(convertIntToByteArray2(portSource)));
        packetSegment.addAll(packet.listToArray(convertIntToByteArray2(portDestination)));

        packetSegment.addAll(packet.listToArray(seqByte));
        packetSegment.addAll(packet.listToArray(ackByte));

        packetSegment.addAll(packet.listToArray(nbFragByte));
        packetSegment.addAll(packet.listToArray(qteFragByte));

        packetSegment.addAll(packet.packet);


        Packet packetTest = new Packet();
        packetTest.setPacket(packetSegment);

        desencapsulation(packetTest);*/
        return null;
    }

    @Override
    public void encapsulation(Packet packet) {

        /*double nbFragments = 1;
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
        }*/
    }

    @Override
    public void desencapsulation(Packet packet) {
        Boolean OK;
        int accuse;
        ArrayList<Byte> destination = new ArrayList<>();
        ArrayList<Byte> source = new ArrayList<>();
        ArrayList<Byte> ackRes = new ArrayList<>();
        ArrayList<Byte> nbFrag = new ArrayList<>();
        ArrayList<Byte> qteFrag = new ArrayList<>();
        ArrayList<Byte> data = new ArrayList<>();


        for(int i = 0; i < 4; i++){
            source.add(packet.packet.get(i));
        }

        for(int i = 4; i < 8; i++){
            destination.add(packet.packet.get(i));
        }

        for(int i = 8; i < 12; i++){
            ackRes.add(packet.packet.get(i));
        }

        for(int i = 12; i < 16; i++){
            nbFrag.add(packet.packet.get(i));
        }

        for(int i = 16; i < 20; i++){
            qteFrag.add(packet.packet.get(i));
        }

        for (int i = 20; i < packet.packet.size(); i++){
            data.add(packet.packet.get(i));
        }

        int tempFrag = convertByteArrayToInt2(packet.arrayToList(nbFrag));
        int tempqte = convertByteArrayToInt2(packet.arrayToList(qteFrag));

        System.out.println("Numero: " + tempFrag);
        System.out.println("Qte: " + qteFrag);

        OK = desencapsulationFragments(data, tempFrag, tempqte);

        //Creation accuse de reception positif
        ArrayList<Byte> accuseReception = new ArrayList<Byte>();
        accuseReception.addAll(source);
        accuseReception.addAll(destination);

        if(OK == true){

            System.out.println("ca marche, retour");
            accuse = 1;

            //Envoyer data couche supérieur

            /*Packet dataPourCoucheSuperieur = new Packet();
            dataPourCoucheSuperieur.setPacket(data);
            layerDessus.desencapsulation(dataPourCoucheSuperieur);*/

        }

        else{
            System.out.println("ca marche pas");
            accuse = 0;
        }

        ackByte = convertIntToByteArray2(accuse);

        accuseReception.addAll(packet.listToArray(ackByte));
        accuseReception.addAll(nbFrag);
        accuseReception.addAll(qteFrag);
        Packet arkEnvoi = new Packet();
        arkEnvoi.setPacket(accuseReception);


        layerDessous.encapsulation(arkEnvoi);
    }

    public Boolean desencapsulationFragments(ArrayList<Byte> data, int nbFrag, int qteFrag) {

        Boolean fragAnterieur = false;
        Boolean fragUlterieur = false;


        if(nombreFragments == 1){
            nombreFragments = qteFrag;
        }

        else{
            if(nombreFragments != qteFrag){
                System.out.println("erreur fragments");
                return false;
            }
        }

        if(nbFrag >= qteFrag){
            System.out.println("erreur fragment trop loin");
            System.out.println(nbFrag);
            return false;
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
                return false;
            }
        }

        if(fragAnterieur == true && fragUlterieur == true){
            System.out.println("Erreur ordre fragments");
            return false;
        }

        //TODO: Faire l'ajout de données en ordre décroissant
        fragmentsRecus.add(nbFrag);

        packetTotal.addAll(data);


        byte[] array = new byte[packetTotal.size()];
        int i = 0;
        for (Byte current : packetTotal) {
            array[i] = current;
            i++;
        }

        System.out.println(new String(array));

        return true;
    };

    public String convertByteToString(byte byteValue)
    {

        // Convert byte value to String value
        // using + operator method
        String stringValue = "" + byteValue;

        return (stringValue);
    }

    public ArrayList<Byte> getPacketTotal(){
        return packetTotal;
    }

    public static int convertByteArrayToInt2(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF) << 0);
    }

    public static byte[] convertIntToByteArray2(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value };
    }


}
