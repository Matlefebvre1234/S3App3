import java.io.*;
import java.util.ArrayList;

public class Client {

    private static String[] savedArgs;
    public static String[] getArgs() {
        return savedArgs;
    }


    public static void main( String[] args) throws IOException {
        savedArgs = args;
        if (args.length != 2) {
            System.out.println("nom hote et fichier non fourni");
            return;
        }


        String inputString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ligula arcu, mollis vitae ligula vitae, scelerisque aliquet leo. Phasellus sed neque eget lacus luctus laoreet id non neque. Mauris at velit sed dolor euismod finibus non sit amet dolor. Etiam in interdum dui. Nunc turpis mi, tristique ac laoreet eget, ornare in dui. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquet iaculis quam, in congue turpis ullamcorper non. Interdum et malesuada fames ac ante ipsum primis in faucibus. Mauris ornare cursus mauris, dapibus dapibus ex vulputate vitae. Donec placerat risus vitae accumsan iaculis. Sed bibendum est quis erat placerat, a interdum nulla pretium. Donec sed eleifend ligula.";
        //String inputString = "Hello World";
        byte[] byteArrray = inputString.getBytes();




        Packet test = new Packet();
        test.setPacket(byteArrray);

       TransportLayer transportLayer = new TransportLayer();
        DatalinkLayer layer = new DatalinkLayer();
       ApplicationLayer application = new ApplicationLayer();
       PhysiqueLayer physiqueLayer = new PhysiqueLayer();
       application.setLayerDessous(transportLayer);
       transportLayer.setLayerDessus(application);
       transportLayer.setLayerDessous(layer);
       layer.setLayerDessus(transportLayer);
       layer.setLayerDessous(physiqueLayer);
       physiqueLayer.setLayerDessus(layer);


        application.envoyerFichierServeur(args[1]);






    }

}