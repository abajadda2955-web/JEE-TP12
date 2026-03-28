package ma.ens.security.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // Permet de désactiver un compte sans le supprimer
    private boolean active;

    // EAGER = charge les rôles immédiatement avec l'utilisateur
    // Spring Security en a besoin pour les autorisations
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}