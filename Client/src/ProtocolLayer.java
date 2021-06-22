public abstract class ProtocolLayer {

    ProtocolLayer layerDessus;
    ProtocolLayer layerDessous;
    boolean ReadyForNextPacket;

    Packet packet;
    public ProtocolLayer()
    {
        ReadyForNextPacket =false;
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

    public boolean getReadyForNextPacket() {

        return ReadyForNextPacket;
    }



    public void setReadyForNextPacket(boolean bool){
        ReadyForNextPacket = bool;

    }
}
