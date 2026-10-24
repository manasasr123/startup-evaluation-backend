
package in.startupready.config;

import java.util.List;
import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails founder = User.withUsername("founder")
                .password(passwordEncoder.encode("Founder@123"))
                .roles("FOUNDER")
                .build();

        return new InMemoryUserDetailsManager(founder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/health").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        String frontendUrl = System.getenv("FRONTEND_URL");

        if (frontendUrl == null || frontendUrl.isBlank()) {
            frontendUrl = "http://localhost:5173";
        }

        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH",
                    "DELETE", "OPTIONS")
        );
        config.setAllowedHeaders(
            List.of("Authorization", "Content-Type")
        );
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
