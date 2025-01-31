package pl.straszewska.product.security;

import java.util.Optional;

public interface UserStorage {
    public Optional<User> findByEmail(String email);
}
