import java.util.ArrayList;

/**
 * protocol layer class
 */
public abstract class ProtocolLayer {

    ProtocolLayer layerDessus;
    ProtocolLayer layerDessous;
    boolean ReadyForNextPacket;
    Packet packet;
    public ProtocolLayer()
    {
        ReadyForNextPacket = true;
    }

    /**
     * encapsule les donnees
     * @param packet
     */
     public abstract void encapsulation(Packet packet);

    /**
     * desencapsule les donnees
     * @param packet
     */
    public abstract void desencapsulation(Packet packet);

    /**
     * reser le layer du dessus
     */
    public abstract void resetLayerDessus();


    /**
     * get layer du dessus
     * @return
     */
    public ProtocolLayer getLayerDessus()
    {
        return layerDessus;
    }

    /**
     * get layer du dessus
     * @return
     */
    public ProtocolLayer getlayerDesssous()
    {
        return layerDessous;
    }

    /**
     * set layer du dessus
     * @param layer
     */
    public void setLayerDessus(ProtocolLayer layer)
    {
        layerDessus = layer;
    }

    /**
     * set le layer du dessous
     * @param layer
     */
    public void setLayerDessous(ProtocolLayer layer)
    {
        layerDessous = layer;
    }

    /**
     * get si la couche est prete pour le prochain packet
     * @return
     */
    public Boolean getReadyForNextPacket() {
        return layerDessous.ReadyForNextPacket;
    }

    /**
     * set le pret pour prochain packey
     * @param ack
     */
    public void setReadyForNextPacket(Boolean ack){
        ReadyForNextPacket = ack;
    }
}
