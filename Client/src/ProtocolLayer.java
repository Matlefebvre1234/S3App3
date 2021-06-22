/**
 * protocol layer abstract class
 */
public abstract class ProtocolLayer {

    ProtocolLayer layerDessus;
    ProtocolLayer layerDessous;
    boolean ReadyForNextPacket;

    Packet packet;
    public ProtocolLayer()
    {
        ReadyForNextPacket =false;
    }

    /**
     * encapsule les donness
     * @param packet
     */
     public abstract void encapsulation(Packet packet);


    /**
     * desencapsule les donnees
     * @param packet
     */
    public abstract void desencapsulation(Packet packet);


    /**
     * get layer du dessus
     * @return
     */
    public ProtocolLayer getLayerDessus()
    {
        return layerDessus;
    }

    /**
     * get layer du dessous
     * @return
     */
    public ProtocolLayer getlayerDesssous()
    {
        return layerDessous;
    }

    /**
     * set layerdessus
     * @param layer
     */
    public void setLayerDessus(ProtocolLayer layer)
    {
        layerDessus = layer;
    }

    /**
     * set layer dessous
     * @param layer
     */
    public void setLayerDessous(ProtocolLayer layer)
    {
        layerDessous = layer;
    }

    /**
     * get pret pour prochaine envoie
     * @return
     */
    public boolean getReadyForNextPacket() {

        return ReadyForNextPacket;
    }


    /**
     * set pret pour prochain envoie
     * @param bool
     */
    public void setReadyForNextPacket(boolean bool){
        ReadyForNextPacket = bool;

    }
}
