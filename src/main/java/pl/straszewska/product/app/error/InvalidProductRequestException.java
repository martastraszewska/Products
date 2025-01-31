package pl.straszewska.product.app.error;

public class InvalidProductRequestException extends RuntimeException{
    public static final String INVALID_FIELD_MSG = "%s is incorrect";
    public InvalidProductRequestException(String property){super(String.format(INVALID_FIELD_MSG, property));}
}
