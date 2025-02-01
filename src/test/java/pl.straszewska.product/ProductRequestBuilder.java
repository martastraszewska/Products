package pl.straszewska.product;

import pl.straszewska.product.api.ProductRequest;

public class ProductRequestBuilder {

    private String name = "product name";
    private String description = "product description";
    private Float price = 10.25f;
    private String category = "Elektronika";

    private ProductRequestBuilder() {
    }

    public ProductRequestBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductRequestBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductRequestBuilder setPrice(Float price) {
        this.price = price;
        return this;
    }

    public ProductRequestBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public static ProductRequestBuilder builder() {
        return new ProductRequestBuilder();
    }

    public ProductRequest build() {
        return new ProductRequest(name, description, price, category);
    }
}
