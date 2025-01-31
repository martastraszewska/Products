package pl.straszewska.product.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.straszewska.product.api.ProductRequest;
import pl.straszewska.product.app.error.InvalidProductRequestException;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String name;
    private String description;
    private Float price;
    private String category;

    public static Product fromProductRequest(ProductRequest request) {
        validateRequest(request);
        Product product = new Product();
        product.id = UUID.randomUUID().toString();
        product.name = request.getName();
        product.description = request.getDescription();
        product.price = request.getPrice();
        product.category = request.getCategory();
        return product;
    }
    public Product update(ProductRequest request) {
        validateRequest(request);
        Product product = new Product();
        product.id = this.getId();
        product.name = request.getName();
        product.description = request.getDescription();
        product.price = request.getPrice();
        product.category = request.getCategory();
        return product;
    }


    private static void validateRequest(ProductRequest request) {
        if (isBlank(request.getName())) {
            throw new InvalidProductRequestException("name");
        }
        if (isBlank(request.getDescription())) {
            throw new InvalidProductRequestException("description");
        }
        if (isBlank(request.getCategory())) {
            throw new InvalidProductRequestException("category");
        }
        if (request.getPrice() == null || isPriceInvalid(request.getPrice())){
            throw new InvalidProductRequestException("price");
        }
    }

    private static Boolean isBlank(String property) {
        return property == null || property.isEmpty() || property.isBlank();
    }

    private static Boolean isPriceInvalid(Float price) {
        String priceStr = price.toString();
        return price < 0f || (priceStr.contains(".") && priceStr.substring(priceStr.indexOf(".") + 1).length() > 2);
    }

}
