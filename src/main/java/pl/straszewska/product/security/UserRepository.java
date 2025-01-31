package pl.straszewska.product.security;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CassandraRepository<UserEntity, String> {
    public Optional<UserEntity> findByEmail(String email);
}
