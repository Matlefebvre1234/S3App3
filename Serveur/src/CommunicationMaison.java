import java.net.SocketTimeoutException;

/**
 * CommunicationMaison
 */
public class CommunicationMaison extends Communication{

    ApplicationLayer applicationLayer;
    TransportLayer transportLayer;
    DatalinkLayer datalinkLayer;
    PhysiqueLayer physiqueLayer;

    public CommunicationMaison()
    {
        InitialiserLayer();
    }

    /**
     * initialise les layers
     */
    @Override
    public void InitialiserLayer() {

        applicationLayer = new ApplicationLayer();
        transportLayer = new TransportLayer();
        datalinkLayer = new DatalinkLayer();
        physiqueLayer = new PhysiqueLayer();

        applicationLayer.setLayerDessous(transportLayer);
        transportLayer.setLayerDessus(applicationLayer);
        transportLayer.setLayerDessous(datalinkLayer);
        datalinkLayer.setLayerDessus(transportLayer);
        datalinkLayer.setLayerDessous(physiqueLayer);
        physiqueLayer.setLayerDessus(datalinkLayer);
    }

    /**
     * start ls communication
     * @param nomFichier
     */
    @Override
    public void start(String nomFichier) {
        applicationLayer.envoyerFichierServeur(nomFichier);
    }

    /**
     * attend les messages
     */
    @Override
    public void RecevoirStart() {
        physiqueLayer.recevoirMessage();
    }
}
