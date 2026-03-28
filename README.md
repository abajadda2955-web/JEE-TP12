TP Spring Security - API REST avec Authentification JWT
---
Description du TP

Ce TP a pour objectif de sécuriser une API REST avec Spring Security en utilisant l'authentification par JWT (JSON Web Token).

Contrairement aux TP précédents qui utilisaient des sessions, cette approche est stateless : chaque requête est indépendante et contient son propre token d'authentification.

---

Technologies utilisées :

    Spring Boot 3.x

    Spring Security 6

    Spring Data JPA

    MySQL

    JWT (jjwt)

    Lombok


  ---

Tests avec Postman
1. Authentification - Obtention du token

<img width="797" height="554" alt="2" src="https://github.com/user-attachments/assets/de72544b-c7b3-4a7b-912d-edbf9b621418" />

Réponse contenant le token JWT à utiliser pour les requêtes suivantes

---

2. Accès à l'espace utilisateur

<img width="776" height="439" alt="3" src="https://github.com/user-attachments/assets/c1143fc6-2b9d-4789-8cf5-a2cff7b1ad10" />


Accès autorisé pour les rôles USER et ADMIN
---

3. Accès à l'espace administrateur (avec token admin)


<img width="796" height="462" alt="4" src="https://github.com/user-attachments/assets/dc81922c-e822-4c0c-98b7-41a71ed88fdc" />

Accès autorisé uniquement pour le rôle ADMIN
---
4. Test d'accès refusé (user tente d'accéder à admin)

<img width="790" height="414" alt="7" src="https://github.com/user-attachments/assets/17f4e452-23da-4982-8ec1-0e31a533b8f3" />


Erreur 403 Forbidden car l'utilisateur n'a pas le rôle ADMIN












  
