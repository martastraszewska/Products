package pl.straszewska.product.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.equals(user.getRole(), "admin")) {
            return ROLE_ADMIN;
        } else {
            return ROLE_USER;
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_VIEW_PRODUCTS"));
    private static final List<GrantedAuthority> ROLE_ADMIN = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_VIEW_PRODUCTS", "ROLE_EDIT_PRODUCTS"));
}
