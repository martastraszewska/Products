package pl.straszewska.product.app.error;

public class ProductNotFoundException extends RuntimeException{
    public static final String PRODUCT_NOT_FOUND_MSG = "Product not found %s";
    public ProductNotFoundException(String id){super(String.format(PRODUCT_NOT_FOUND_MSG, id));}
}
