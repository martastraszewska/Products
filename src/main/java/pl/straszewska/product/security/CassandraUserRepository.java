package pl.straszewska.product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CassandraUserRepository implements UserStorage {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::toUser);
    }
}
