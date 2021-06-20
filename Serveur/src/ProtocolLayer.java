import java.util.ArrayList;

public abstract class ProtocolLayer {

    ProtocolLayer layerDessus;
    ProtocolLayer layerDessous;

    Packet packet;
    public ProtocolLayer()
    {

    }

     public abstract void encapsulation(Packet packet);


    public abstract void desencapsulation(Packet packet);


    public ProtocolLayer getLayerDessus()
    {
        return layerDessus;
    }
    public ProtocolLayer getlayerDesssous()
    {
        return layerDessous;
    }

    public void setLayerDessus(ProtocolLayer layer)
    {
        layerDessus = layer;
    }
    public void setLayerDessous(ProtocolLayer layer)
    {
        layerDessous = layer;
    }
}
