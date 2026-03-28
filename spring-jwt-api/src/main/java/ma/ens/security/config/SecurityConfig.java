package ma.ens.security.config;

import ma.ens.security.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Le gestionnaire d'authentification (utilisé dans AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Les règles de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF car on est en stateless (pas de sessions)
                .csrf(csrf -> csrf.disable())

                // IMPORTANT: Pas de sessions! Chaque requête est indépendante
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Les règles d'accès aux URLs
                .authorizeHttpRequests(auth -> auth
                        // /api/auth/** est accessible à tous (login, etc.)
                        .requestMatchers("/api/auth/**").permitAll()
                        // /api/admin/** nécessite le rôle ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // /api/user/** nécessite USER ou ADMIN
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        // Tout le reste nécessite d'être authentifié
                        .anyRequest().authenticated()
                )
                // Ajoute notre filtre JWT avant le filtre standard
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}