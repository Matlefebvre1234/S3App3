import java.lang.reflect.Array;
import java.util.ArrayList;

public class Packet {

    ArrayList<Byte> packet;

    public Packet()
    {
        packet = new ArrayList<Byte>();
    }

    public ArrayList<Byte> getPacket()
    {
        return packet;
    }

    public void setPacket(ArrayList<Byte> array)
    {
        packet = array;
    }

    public void setPacket(byte[] array)
    {
        packet = new ArrayList<Byte>();
        for(int i=0;i<array.length;i++)
        {
            packet.add(array[i]);
        }
    }

    public ArrayList<Byte> listToArray(byte[] list) {
        ArrayList<Byte> array = new ArrayList<Byte>();

        for(int i=0;i<list.length;i++)
        {
            array.add(list[i]);
        }

        return array;
    }

    public  byte[] arrayToList(ArrayList<Byte> array) {

        byte[] list = new byte[array.size()];

        for(int i=0;i<array.size();i++)
        {
            list[i] = array.get(i);
        }

        return list;
    }



    public byte[] getByte(int debut,int fin)
    {
        if(debut != fin)
        {
            int index =0;
            byte[] tableau = new byte[fin-debut];

            for(int i =debut;i<fin;i++)
            {
                tableau[index] = packet.get(i);
            }
            return tableau;
        }
        else return null;

    }
    public byte[] getByte()
    {

        byte[] message = new byte[packet.size()];
        for(int i=0;i<packet.size();i++)
        {
            message[i] = packet.get(i);
        }

        return message;

    }
    public void afficherPacket(Packet packet)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < packet.packet.size(); i++) {
            sb.append(String.format(
                    "%02X%s", packet.packet.get(i),
                    (i < packet.packet.size() - 1) ? " " : ""));
        }
    }

    public static void afficherPacket(ArrayList<Byte> packet)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < packet.size(); i++) {
            sb.append(String.format(
                    "%02X%s", packet.get(i),
                    (i < packet.size() - 1) ? " " : ""));
        }
        System.out.println(sb.toString());
    }

    public static void afficherPacket(byte[] arraybyte)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arraybyte.length; i++) {
            sb.append(String.format(
                    "%02X%s", arraybyte[i],
                    (i < arraybyte.length - 1) ? " " : ""));
        }
        System.out.println(sb.toString());
    }


}
