package pl.straszewska.product.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private final String email;
    @JsonIgnore
    private final String password;
    private final String role;
}
