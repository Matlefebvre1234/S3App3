/**
 * Factory communication MAison
 */
public class FactoryCommunicationMaison implements FactoryCommunication{
    @Override
    public Communication creerCommunication() {
        CommunicationMaison maCommunication = new CommunicationMaison();
        return maCommunication;
    }
}
