package pl.straszewska.product.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.straszewska.product.api.ProductRequest;
import pl.straszewska.product.api.ProductResponse;
import pl.straszewska.product.app.error.ProductNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductStorage productStorage;

    public Product findById(String id) {
        return productStorage.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> findAll() {
        return productStorage.findAll();
    }

    public List<Product> findByCategory(String category) {
        return productStorage.findByCategory(category);
    }

    public Product create(ProductRequest productRequest) {
        return productStorage.save(Product.fromProductRequest(productRequest));
    }

    public Product update(String id, ProductRequest request) {
        Product product = productStorage.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productStorage.save(product.update(request));
    }

    public void delete(String id) {
        Product product = productStorage.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productStorage.delete(product);
    }
}
