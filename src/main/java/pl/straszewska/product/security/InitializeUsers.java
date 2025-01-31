package pl.straszewska.product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitializeUsers implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodedPassword = encoder.encode("password");
        userRepository.save(new UserEntity("user@gmail.com", encodedPassword, "user"));
        userRepository.save(new UserEntity("admin@gmail.com", encodedPassword, "admin"));
    }
}
