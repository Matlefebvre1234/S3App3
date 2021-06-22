import java.util.ArrayList;

/**
 * class communication
 */
public abstract class Communication {

    ArrayList<ProtocolLayer> listeLayer = new ArrayList<ProtocolLayer>();

    /**
     * initialise les layers
     */
    public abstract void InitialiserLayer();

    /**
     * get la liste des layers
     * @return
     */
    public ArrayList<ProtocolLayer> getListeLayer()
    {
        return listeLayer;
    }

    /**
     * set la liste de layers
     * @param list
     */
    public void setListeLayer(ArrayList<ProtocolLayer> list)
    {
        listeLayer = list;
    }

    /**
     * start la communication
     * @param nomFichier
     */
    public abstract void start(String nomFichier);

    /**
     * attend les messages
     */
    public abstract void RecevoirStart();
}
