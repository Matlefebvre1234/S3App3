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




}
