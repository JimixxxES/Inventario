
public class InvalidValueException extends CustomException {
    public InvalidValueException(String fieldName, String expected) {
        super("Valor inválido para '" + fieldName + "'. Se esperaba: " + expected);
    }
}
