package pl.straszewska.product.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static pl.straszewska.product.api.ProductController.PRODUCT_API_ROOT_PATH;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request ->
                request.requestMatchers(antMatcher(HttpMethod.GET, PRODUCT_API_ROOT_PATH + "/**")).hasRole("VIEW_PRODUCTS")
                        .requestMatchers(antMatcher(HttpMethod.POST, PRODUCT_API_ROOT_PATH)).hasRole("EDIT_PRODUCTS")
                        .requestMatchers(antMatcher(HttpMethod.PUT, PRODUCT_API_ROOT_PATH + "/**")).hasRole("EDIT_PRODUCTS")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, PRODUCT_API_ROOT_PATH + "/**")).hasRole("EDIT_PRODUCTS")
        ).httpBasic(withDefaults());
        return http.build();
    }
}
