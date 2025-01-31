package pl.straszewska.product.infrastructure;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CassandraRepository<ProductEntity, String> {

    @Query(allowFiltering = true)
    public List<ProductEntity> findByCategory(String category);

    public Optional<ProductEntity> findById(String id);
}
