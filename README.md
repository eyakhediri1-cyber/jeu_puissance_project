# Puissance 4 — Application Desktop Java/JavaFX

Application desktop complète du jeu **Puissance 4**, avec interface graphique, base de données MySQL, agent IA et persistance fichier.

---

## ✨ Fonctionnalités

| Module | Description |
|--------|-------------|
| **Jeu** | Plateau 6×7, détection de victoire (horizontal, vertical, diagonal) |
| **Agent IA** | Adversaire intelligent : attaque, défense, priorité colonne centrale |
| **Gestion joueurs** | CRUD complet via DAO + MySQL (ajout, modification, suppression, liste) |
| **Historique des parties** | Sauvegarde en base de données avec tous les coups joués |
| **Persistance fichier** | Export/import des parties en `.txt` (format `YYYY_MM_DD_idJ1_idJ2.txt`) |
| **Mode simulation** | Partie automatique Agent vs Agent avec Thread |

---

## 🏗️ Architecture

Pattern **MVC** strict avec couche DAO :

```
vue/          → JavaFX (InterfaceJeuPuissance, InterfaceListeJoueur, InterfaceImporterPartie)
control/      → Controlleur (logique UI, orchestration)
model/        → Game, Joueur, Partie, Coup, Agent
model/dao/    → DAO<T> (interface générique), DAOJoueur, DAOPartie, DAOCoup
model/        → ConnexionBD (Singleton), GestionFichier
```

---

## 🛠️ Stack Technique

`Java 17` · `JavaFX 17` · `MySQL 8` · `JDBC` · `Maven` · `MVC` · `Design Pattern DAO` · `Singleton`

---

## 🗄️ Base de données

```sql
-- 3 tables
joueur   (id, nom, score)
partie   (id, id_joueur1, id_joueur2, score_j1, score_j2, date)
coup     (id, id_partie, coup_j1, coup_j2)
```

---

## 🚀 Lancement

```bash
# Prérequis : Java 17+, Maven, MySQL

# 1. Créer la base de données
mysql -u root -p < sql/puissance4.sql

# 2. Configurer la connexion (ConnexionBD.java)
# URL : jdbc:mysql://localhost:3306/puissance4
# USER / PASSWORD à adapter

# 3. Lancer l'application
mvn javafx:run
```

---

## 🤖 Stratégie de l'Agent IA

L'agent suit 4 priorités ordonnées :
1. **Gagner** — jouer le coup gagnant immédiatement
2. **Bloquer** — empêcher le joueur d'aligner 4
3. **Centre** — préférer la colonne centrale (meilleure couverture)
4. **Aléatoire** — colonne valide au hasard

---

*Projet académique — Atelier Bases de Données · ISET Sousse DSI2*
