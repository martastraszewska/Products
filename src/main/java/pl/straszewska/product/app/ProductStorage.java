package pl.straszewska.product.app;

import java.util.List;
import java.util.Optional;

public interface ProductStorage {
    public Optional<Product> findById(String id);

    public boolean existsById(String id);

    public List<Product> findByCategory(String category);

    public List<Product> findAll();


    public Product save(Product product);

    void delete(Product product);
}
