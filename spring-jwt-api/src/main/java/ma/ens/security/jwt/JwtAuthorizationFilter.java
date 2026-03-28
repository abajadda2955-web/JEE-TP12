package ma.ens.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Je récupère le header Authorization
        String header = request.getHeader("Authorization");

        // Le header doit commencer par "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // J'extrais le token (sans le préfixe "Bearer ")
            String token = header.substring(7);

            // J'extrais le nom d'utilisateur depuis le token
            String username = jwtUtil.extractUsername(token);

            // Si le token est valide et qu'il n'y a pas déjà une authentification
            if (username != null && jwtUtil.validateToken(token) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Je charge l'utilisateur depuis la base
                UserDetails user = userDetailsService.loadUserByUsername(username);

                // Je crée l'objet d'authentification que Spring Security attend
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Je mets l'authentification dans le contexte Spring Security
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Je continue la chaîne des filtres
        filterChain.doFilter(request, response);
    }
}