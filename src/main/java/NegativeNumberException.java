
public class NegativeNumberException extends CustomException {
    public NegativeNumberException(String fieldName) {
        super("El campo '" + fieldName + "' no puede ser negativo");
    }
}
//:)