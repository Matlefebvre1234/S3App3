import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.net.NetworkInterface;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Client {


    private static String[] savedArgs;
    public static String[] getArgs() {
        return savedArgs;
    }

    public static void main( String[] args) throws IOException {
        savedArgs = args;
        if (args.length != 1) {
            System.out.println("Usage: java QuoteClient <hostname>");
            return;
        }



        DatalinkLayer layer = new DatalinkLayer();
        Packet test = new Packet();
        test.packet.add((byte)0x0F);

        //layer.encapsulation(test);


        PhysiqueLayer physique = new PhysiqueLayer();

        physique.encapsulation(test);
        physique.desencapsulation(test);

    }

}