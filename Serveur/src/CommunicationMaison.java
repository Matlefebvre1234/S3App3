import java.net.SocketTimeoutException;

public class CommunicationMaison extends Communication{

    ApplicationLayer applicationLayer;
    TransportLayer transportLayer;
    DatalinkLayer datalinkLayer;
    PhysiqueLayer physiqueLayer;

    public CommunicationMaison()
    {
        InitialiserLayer();
    }

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

    @Override
    public void start(String nomFichier) {
        applicationLayer.envoyerFichierServeur(nomFichier);
    }

    @Override
    public void RecevoirStart() {
        physiqueLayer.recevoirMessage();
    }
}
