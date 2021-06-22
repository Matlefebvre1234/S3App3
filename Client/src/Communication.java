import java.util.ArrayList;

/**
 * classe communication
 */
public abstract class Communication {

    ArrayList<ProtocolLayer> listeLayer = new ArrayList<ProtocolLayer>();

    /**
     * initialise les layer
     */
    public abstract void InitialiserLayer();

    /**
     * get list layer
     * @return
     */
    public ArrayList<ProtocolLayer> getListeLayer()
    {
        return listeLayer;
    }

    /**
     * set liste layer
     * @param list
     */
    public void setListeLayer(ArrayList<ProtocolLayer> list)
    {
        listeLayer = list;
    }

    /**
     * demare la communication
     * @param nomFichier
     */
    public abstract void start(String nomFichier);

    /**
     * ecoute des reponse
     */
    public abstract void RecevoirStart();
}
