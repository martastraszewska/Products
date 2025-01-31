package pl.straszewska.product.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("users")
public class UserEntity {
    @PrimaryKey
    private String email;
    @Column
    private String password;
    @Column
    private String role;

    public User toUser() {
        return new User(
                this.email,
                this.password,
                this.role);
    }
}
