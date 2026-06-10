package app.mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Agent extends Joueur {

    private final Random rng = new Random();

    public Agent(int id, String nom) {
        super(id, nom, 0);
    }

  public int choisirColonne(Game game, int idAdversaire) {
        
        int coup = chercherCoupGagnant(game, getId());
        if (coup != -1) return coup;

        coup = chercherCoupGagnant(game, idAdversaire);
        if (coup != -1) return coup;

        int[] priorite = {4, 3, 5, 2, 6, 1, 7};
        for (int col : priorite) {
            if (colonneValide(game, col)) return col;
        }

        List<Integer> valides = colonnesValides(game);
        return valides.get(rng.nextInt(valides.size()));
    }

    private int chercherCoupGagnant(Game game, int id) {
        for (int col = 1; col <= 7; col++) {
            if (!colonneValide(game, col)) continue;
            try {
                int ligne = game.getLigneVideByColonne(col);
                game.setCoup(ligne, col - 1, id);
                boolean gagne = game.estGagnant(new Position(ligne, col - 1), id);
                game.setCoup(ligne, col - 1, 0);
                if (gagne) return col;
            } catch (CoupException ignored) {}
        }
        return -1;
    }

    private boolean colonneValide(Game game, int col) {
        try { game.getLigneVideByColonne(col); return true; }
        catch (CoupException e) { return false; }
    }

    private List<Integer> colonnesValides(Game game) {
        List<Integer> l = new ArrayList<>();
        for (int col = 1; col <= 7; col++)
            if (colonneValide(game, col)) l.add(col);
        return l;
    }
}
