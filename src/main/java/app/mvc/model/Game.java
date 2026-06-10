package app.mvc.model;
public class Game {

    private final int[][] grille;
    private final int nbColonne = 7;
    private final int nbLigne = 6;

    public Game(int id1, int id2) {
        this.grille = new int[nbLigne][nbColonne];
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                grille[i][j] = 0;
            }
        }
    }

    public void initialiser() {
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbColonne; j++) {
                grille[i][j] = 0;
            }
        }
    }

    public boolean estGagnant(Position pos, int idJoueur) {
        return (alignementH(pos.getPosX(), idJoueur) || alignementV(pos.getPosY(), idJoueur) || alignementD(pos, idJoueur));
    }

    private boolean alignementH(int ligne, int id) {
        int compteur = 0;
        for (int j = 0; j < nbColonne; j++) {
            if (grille[ligne][j] == id) {
                compteur++;
                if (compteur == 4) {
                    return true;
                }
            } else {
                compteur = 0;
            }
        }
        return false;
    }

    private boolean alignementV(int colonne, int id) {
        int compteur = 0;
        for (int i = 0; i < nbLigne; i++) {
            if (grille[i][colonne] == id) {
                compteur++;
                if (compteur == 4) {
                    return true;
                }
            } else {
                compteur = 0;
            }
        }
        return false;
    }

    public boolean alignementD(Position pos, int idJoueur) {
        int compA = 1 + compteurJetons(pos.getPosX(), pos.getPosY(), idJoueur, 1, 1) + compteurJetons(pos.getPosX(), pos.getPosY(), idJoueur, -1, -1);
        int compB = 1 + compteurJetons(pos.getPosX(), pos.getPosY(), idJoueur, -1, 1) + compteurJetons(pos.getPosX(), pos.getPosY(), idJoueur, 1, -1);
        return (compA >= 4 || compB >= 4);
    }

    private int compteurJetons(int ligne, int colonne, int id, int directionLigne, int directionColonne) {
        int nb = 0;
        int lig = ligne + directionLigne;
        int col = colonne + directionColonne;

        while (lig >= 0 && lig < nbLigne && col >= 0 && col < nbColonne && grille[lig][col] == id) {
            nb++;
            lig = lig + directionLigne;
            col = col + directionColonne;
        }
        return nb;
    }

    public boolean estFin() {
        for (int j = 0; j < nbColonne; j++) {
            if (grille[0][j] == 0) {
                return false;
            }
        }
        return true;
    }

    public void setCoup(int ligne, int colonne, int idJoueur) {
        if (ligne >= 0 && ligne < nbLigne && colonne >= 0 && colonne < nbColonne) {
            this.grille[ligne][colonne] = idJoueur;
        }
    }

    public int getLigneVideByColonne(int colonne) throws CoupException {
        if (colonne < 1 || colonne > nbColonne) {
            throw new CoupException("La colonne " + colonne + " saisir est invalide (hors de l'intervalle [1,7])");
        }
        int nb = colonne - 1;

        if (grille[0][nb] != 0) {
            throw new CoupException("La colonne " + colonne + " est deja remplie !");
        }
        for (int i = nbLigne - 1; i >= 0; i--) {
            if (grille[i][nb] == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < nbLigne; i++) {
            res = res + " | ";
            for (int j = 0; j < nbColonne; j++) {
                res = res + grille[i][j] + " ";
            }
            res = res + "|\n";
        }
        return res;
    }

    public int[][] getGrille() {
        return grille;
    }
}
