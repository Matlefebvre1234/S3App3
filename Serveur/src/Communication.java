import java.util.ArrayList;

public abstract class Communication {

    ArrayList<ProtocolLayer> listeLayer = new ArrayList<ProtocolLayer>();

    public abstract void InitialiserLayer();

    public ArrayList<ProtocolLayer> getListeLayer()
    {
        return listeLayer;
    }

    public void setListeLayer(ArrayList<ProtocolLayer> list)
    {
        listeLayer = list;
    }

    public abstract void start(String nomFichier);
    public abstract void RecevoirStart();
}
