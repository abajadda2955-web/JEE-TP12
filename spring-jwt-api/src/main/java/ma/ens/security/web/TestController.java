package ma.ens.security.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/user/profile")
    public String userProfile(Authentication auth) {
        return "Bonjour " + auth.getName() + " ! Tu as accès à l'espace utilisateur.";
    }

    @GetMapping("/admin/stats")
    public String adminStats(Authentication auth) {
        return "Admin " + auth.getName() + ", voici les statistiques sensibles.";
    }

    @GetMapping("/public/info")
    public String publicInfo() {
        return "Cette page est publique mais elle nécessite un token !";
    }
}