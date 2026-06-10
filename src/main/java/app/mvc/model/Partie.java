package app.mvc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Partie {
    private Game game;
    private Joueur j1;
    private Joueur j2;
    private Joueur joueurCourant;
    private int nbJetonsJ1;
    private int nbJetonsJ2;
    private List<Coup> lisCoupJ;
    private LocalDate date;

    public Partie(Joueur j1, Joueur j2) {
        this.j1 = j1;
        this.j2 = j2;
        this.game = new Game(j1.getId(), j2.getId());
        this.joueurCourant = j1;
        this.nbJetonsJ1 = 21;
        this.nbJetonsJ2 = 21;
        this.lisCoupJ = new ArrayList<>();
        this.date = LocalDate.now();
    }

    public void lancerPartie() {
        boolean fini = false;
        while (!fini) {
            System.out.println("\nTour de : " + joueurCourant.getNom());
            int colonne = joueurCourant.choisirCoup();
            try {
                int ligne = game.getLigneVideByColonne(colonne);
                insertCoup(colonne);
                Position p = new Position(ligne, colonne);
                game.setCoup(ligne, colonne - 1, joueurCourant.getId());
                if (game.estGagnant(p, joueurCourant.getId())) {
                    System.out.println("VICTOIRE ! " + joueurCourant.getNom() + " a gagné !");
                    joueurCourant.incrementerScore();
                    fini = true;
                } else if (game.estFin()) {
                    System.out.println("Match nul !");
                    fini = true;
                } else {
                    modifierRole();
                }
            } catch (CoupException e) {
                System.out.println("Erreur de coup : " + e.getMessage());
            }
        }
    }

    public void modifierRole() {
        if (this.joueurCourant == j1) this.joueurCourant = j2;
        else                          this.joueurCourant = j1;
    }

    public void insertCoup(int numColonne) {
        if (this.joueurCourant.getId() == j1.getId()) {
            lisCoupJ.add(new Coup(numColonne));
            nbJetonsJ1--;
        } else {
            if (!lisCoupJ.isEmpty()) {
                lisCoupJ.get(lisCoupJ.size() - 1).setCoupJ2(numColonne);
            }
            nbJetonsJ2--;
        }
    }

 
    public Joueur getJoueurCourant() {
    	return joueurCourant; 
    }
    public List<Coup> getLisCoupJ()  {
    	return lisCoupJ;
    }
    public LocalDate getDate() {
    	return date; 
    }
    public Joueur getJ1() {
        return j1; 
    }
    public Joueur getJ2() { 
    	return j2; 
    }

   
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date    : ").append(date).append("\n");
        sb.append("Joueur 1: ").append(j1.getNom())
          .append(" (id=").append(j1.getId())
          .append(", score=").append(j1.getScore()).append(")\n");
        sb.append("Joueur 2: ").append(j2.getNom())
          .append(" (id=").append(j2.getId())
          .append(", score=").append(j2.getScore()).append(")\n");
        sb.append("Coups   :\n");
        for (Coup c : lisCoupJ) {
            sb.append("  ").append(c).append("\n");
        }
        return sb.toString();
    }
}
