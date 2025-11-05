
public class NullOrEmptyException extends CustomException {
    public NullOrEmptyException(String fieldName) {
        super("El campo '" + fieldName + "' no puede estar vacío o ser nulo");
    }
}
//:)