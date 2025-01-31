package pl.straszewska.product.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.straszewska.product.app.Product;
import pl.straszewska.product.app.ProductStorage;

import java.util.List;
import java.util.Optional;

@Component
public class CassandraProductStorage implements ProductStorage {
    @Autowired
    private ProductRepository repository;

    @Override
    public Optional<Product> findById(String id) {
        return repository.findById(id).map(ProductEntity::toProduct);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<Product> findByCategory(String category) {
        return repository.findByCategory(category).stream().map(ProductEntity::toProduct).toList();
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(ProductEntity::toProduct).toList();
    }


    @Override
    public void delete(Product product) {
        repository.deleteById(product.getId());
    }

    @Override
    public Product save(Product product) {
        ProductEntity save = repository.save(ProductEntity.fromProduct(product));
        return save.toProduct();
    }
}
