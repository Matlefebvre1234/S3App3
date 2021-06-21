public class TransmissionErrorException extends Throwable {
    public String getMessage(){
        return "Trop d'erreurs de transmission, la connexion est perdue";
    };
}
