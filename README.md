# POO - Grand Devoir 1 - 2024

## Instructions Générales
- **Équipe :** Travaillez en équipes de 2-3 personnes.
- **Soumission :** Uploadez le projet sur Moodle (un seul upload par équipe suffit).
  - En cas de problème, contactez votre assistant sur Teams : _Iulia Stanica_ ou _Alexandru Bratosin_.
- **Deadline :** 8ème semaine (**Lundi, 18 novembre 8:00**) et présentation pendant le TP de cette semaine.
- **Présence :** Vous devez être présent à la séance de TP respective pour recevoir les points. Si le projet est soumis sur Moodle mais non présenté, **la note sera 0**.
- **Questions :** Utilisez le groupe Teams de POO ou envoyez un message sur le chat.

---

## Détails Techniques

### Concepts clés
- Classes, relations, interfaces
- `Comparator` / `Comparable`
- Énumérations
- Exceptions
- Fichiers (lecture & écriture)

### Tâches à implémenter
1. **Jeu de survie :**
   - Le joueur se déplace sur une carte en utilisant les touches `W`, `A`, `S`, `D`.
   - La carte est une matrice carrée où chaque emplacement peut être :
     - Vide (`0`)
     - Contenir un objet
     - Contenir un ennemi
   - Actions possibles :
     - **Case vide :** Créer un objet.
     - **Objet :** Ramasser l’objet et vider la case.
     - **Ennemi :** Démarrer un combat (le joueur frappe en premier).

2. **Objets récupérables :**
   - Types : Arbres, Rochers, Céréales.
   - Chaque type laisse tomber une ressource spécifique :
     - Arbres → Bois
     - Rochers → Pierre
     - Céréales → Nourriture

3. **Ressources & bâtiments :**
   - Utilisez le bois, la pierre et la nourriture pour fabriquer :
     - Objets
     - Bâtiments (occupent une case).

---

## Détails de l'implémentation

### Classes abstraites
1. **Objets récupérables :**
   - Attributs :
     - Quantité
     - Qualité (Enum : _commune_, _rare_, _épique_)
   - Méthodes abstraites :
     - `Gatherable()`
     - `toString()`

2. **Personnages :**
   - Attributs :
     - Nom, Attaque, Défense, Santé
     - Statut (_vivant/mort_)
     - Ensemble d’objets
   - Méthodes abstraites :
     - `damage()`, `takedamage()`, `die()`

### Classes spécifiques
1. **Joueur :**
   - Ressources : Bois, Pierre, Nourriture
   - Méthodes pour collecter, interagir et fabriquer.

2. **Ennemi :**
   - Peut laisser tomber un objet (épée, armure, etc.).

3. **Objets :**
   - Attributs : Nom, Bonus (ex. augmenter attaque, défense, santé).

4. **Bâtiments :**
   - Effets spéciaux (ex. régénérer santé, boost permanent d’attaque).
   - Coût en bois et pierre.

---

## Fichiers & Tests
- Travaillez avec des fichiers externes pour :
  - Charger des listes prédéfinies d’objets ou monstres.
  - Sauvegarder l’état du jeu.
- Exemple : Permettre à l’utilisateur de se déplacer dans la matrice avec les touches `W`, `A`, `S`, `D`.

---

## Points
- **Classes abstraites :** 1p
- **Classes Joueur, Ennemi :** 1.5p
- **Classes Objet, Bâtiment :** 2p
- **Sous-classes objets :** 0.75p
- **Test interactif (WASD) :** 1.5p
- **Lecture/écriture fichier :** 1p
- **Comparator/Comparable :** 1p
- **Utilisation Enum :** 0.25p
- **Vérifications & méthodes supplémentaires :** 1p
