package pl.straszewska.product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserRepositoryUserDetailsService implements UserDetailsService {
    @Autowired
    private UserStorage userStorage;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userStorage.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("email " + username + " is not found"));
        return new CustomUserDetails(user);
    }
}
