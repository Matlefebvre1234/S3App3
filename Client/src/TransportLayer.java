import java.util.ArrayList;
import java.lang.Integer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * class transport layer
 */
public class TransportLayer extends ProtocolLayer{

    boolean packerVerifier;
    int ack;
    int limiteErreurs;
    int limiteErreurs2;
    byte[] ackByte;
    byte[] seqByte;
    ArrayList<Byte> packetTotal;
    int nombreFragments;
    ArrayList<Integer> fragmentsRecus;
    Packet resendPacket;
    int portSource;
    int portDestination;
    Boolean ackDataLink;
    ScheduledExecutorService executor;
    int indexPacket;
    int testErreur;
    int[] tableauErreur;
    public TransportLayer(){
        ack = 0;
        seqByte = new byte[4];
        ackByte = new byte[4];
        nombreFragments = 1;
        fragmentsRecus = new ArrayList<>();
        packetTotal = new ArrayList<>();
        resendPacket = new Packet();
        limiteErreurs = 0;
        limiteErreurs2 = 0;
        portSource = 25000;
        portDestination = 25001;
        ackDataLink = false;
        indexPacket =0;
        packerVerifier = true;
        setReadyForNextPacket(true);
        tableauErreur = new int[4];
        tableauErreur[0] = 0;
        tableauErreur[1] = 3;
        tableauErreur[2] = 2;
        tableauErreur[3] = 1;
        testErreur = 0;

    }

    /**
     * encapsule les donnes en fragements
     * @param packet
     * @param nbFrag
     * @param qteFrag
     * @return
     */
    public ArrayList<Byte> encapsulationFragments(Packet packet, int nbFrag, double qteFrag) {

        ArrayList<Byte> packetSegment = new ArrayList<Byte>();
        String[] args = Client.getArgs();

        byte[] nbFragByte;
        byte[] qteFragByte;

        ackByte = convertIntToByteArray2(ack);

        nbFragByte = convertIntToByteArray2(nbFrag);

        qteFragByte = convertIntToByteArray2((int)qteFrag);

        packetSegment.addAll(packet.listToArray(convertIntToByteArray2(portSource)));
        packetSegment.addAll(packet.listToArray(convertIntToByteArray2(portDestination)));

        packetSegment.addAll(packet.listToArray(ackByte));

        packetSegment.addAll(packet.listToArray(nbFragByte));
        packetSegment.addAll(packet.listToArray(qteFragByte));


        packetSegment.addAll(packet.packet);

        //Envoyer couche physique
        Packet envoyerPacket = new Packet();
        envoyerPacket.setPacket(packetSegment);


        layerDessous.encapsulation(envoyerPacket);
        resendPacket.setPacket(packetSegment);

        return null;
    }

    /**
     * encapsule les donnes
     * @param packet
     */
    @Override
    public void encapsulation(Packet packet) {

        setReadyForNextPacket(false);
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

        double finalNbFragments = nbFragments;
        Runnable envoyerPacket = new Runnable() {
            public void run() {
                if(layerDessous.getReadyForNextPacket() && packerVerifier)
                {
                    if(testErreur == 0){
                        System.out.println(getIndexPacket());
                        encapsulationFragments(listPackets.get(getIndexPacket()), getIndexPacket(), finalNbFragments);
                        //testErreur =1;
                        //testErreur = 2;
                    }

                    else if(testErreur == 1){
                        System.out.println(tableauErreur[getIndexPacket()]);
                        System.out.println(getIndexPacket());
                        encapsulationFragments(listPackets.get(getIndexPacket()), tableauErreur[getIndexPacket()], 4);
                    }

                    else{
                        encapsulationFragments(listPackets.get(getIndexPacket()), 0, 4);
                    }

                    packerVerifier = false;
                    setReadyForNextPacket(false);
                    System.out.println("Packet: " + getIndexPacket());
                    System.out.println("Nombre a envoyer: " + finalNbFragments);
                    IncrementeIndexPacket();
                    if(getIndexPacket()>= listPackets.size())
                    {
                        executor.shutdown();
                        indexPacket =0;
                    }
                }

            }
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(envoyerPacket, 0, 100, TimeUnit.MILLISECONDS);

    }

    /**
     * get index packet
     * @return
     */
    private int getIndexPacket()
    {
        return indexPacket;
    }

    /**
     * incremente index packet
     */
    private void IncrementeIndexPacket()
    {
        indexPacket++;
    }


    /**
     * desencapsule les donnees
     * @param packet
     */
    @Override
    public void desencapsulation(Packet packet){
        System.out.println("Desencapsulation Client");
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

        if(convertByteArrayToInt2(packet.arrayToList(ackRes)) == 1)
        {
            System.out.println("continuer envoyer fragments");
            packerVerifier = true;
            setReadyForNextPacket(true);
            ackDataLink = true;
        }

        else{
            limiteErreurs2++;
            if(limiteErreurs2 < 4){
                System.out.println("Erreur, renvoyer ");
                packerVerifier =false;
                ackDataLink = false;
                setReadyForNextPacket(false);

                layerDessous.encapsulation(resendPacket);
            }

            else {
                    try {
                        throw new TransmissionErrorException();
                    } catch (TransmissionErrorException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }
    }

    /**
     * convertie un byte en string
     * @param byteValue
     * @return
     */
    public String convertByteToString(byte byteValue)
    {

        // Convert byte value to String value
        // using + operator method
        String stringValue = "" + byteValue;

        return (stringValue);
    }

    /**
     * convertie array en int
     * @param bytes
     * @return
     */
    public static int convertByteArrayToInt2(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF) << 0);
    }

    /**
     * convertie int en byte
     * @param value
     * @return
     */
    public static byte[] convertIntToByteArray2(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value };
    }
}

