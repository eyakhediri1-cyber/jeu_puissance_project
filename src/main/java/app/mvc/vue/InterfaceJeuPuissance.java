package app.mvc.vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class InterfaceJeuPuissance {

    private static final String DARK_BG = "#0f0f1a";
    private static final String PANEL_BG = "#1a1a2e";
    private static final String PANEL_BORDER = "2a2a4a";
    private static final String GRID_BG = PANEL_BG;
    private static final String CELL_EMPTY = "#0f0f1a";

    private GridPane grilleJeu;
    private Button[][] tabButton;
    private Label labelNomJ1;
    private Label labelScoreJ1;
    private Label labelNomJ2;
    private Label labelScoreJ2;
    private javafx.scene.control.ListView<String> listeCoupsJ1;
    private javafx.scene.control.ListView<String> listeCoupsJ2;
    

    public InterfaceJeuPuissance() {
        this.grilleJeu = new GridPane();
        this.tabButton = new Button[6][7];
        this.labelNomJ1 = new Label();
        this.labelNomJ2 = new Label();
        this.labelScoreJ1 = new Label();
        this.labelScoreJ2 = new Label();
        this.listeCoupsJ1 = new javafx.scene.control.ListView<>();
        this.listeCoupsJ2 = new javafx.scene.control.ListView<>();
        this.listeCoupsJ1 = new javafx.scene.control.ListView<>();
        this.listeCoupsJ2 = new javafx.scene.control.ListView<>();

        grilleJeu.setStyle("-fx-background-color: " + GRID_BG + ";" + "-fx-background-radius: 12px;" + "-fx-border-color: #2a2a4a;" +"-fx-border-radius: 12px;" +"-fx-border-width: 1px;" +"-fx-padding: 12px;");
        grilleJeu.setHgap(6);
        grilleJeu.setVgap(6);
    }

    public void dessiner() {
        grilleJeu.getChildren().clear();
        listeCoupsJ1.getItems().clear();
        listeCoupsJ2.getItems().clear();
        for (int i=0; i<6; i++) {
            for (int j=0; j<7; j++) {
                Button btn = new Button();
                styliserCelluleVide(btn);
                tabButton[i][j] = btn;
                grilleJeu.add(btn,j,i);
            }
        }
    }

    private void styliserCelluleVide(Button btn) {
        btn.setStyle("-fx-background-radius: 150em;" + "-fx-min-width: 44px; -fx-min-height: 44px;" +"-fx-max-width: 44px; -fx-max-height: 44px;" +"-fx-background-color: " + CELL_EMPTY + ";" +"-fx-border-color: #2a2a4a;" +"-fx-border-radius: 150em;" +"-fx-border-width: 1.5px;" + "-fx-cursor: hand;");
    }

    public void setCouleurButton(int numLigne, int numColonne, String couleur) {
        String borderColor = couleur.equalsIgnoreCase("#e24b4a") ? "#a32d2d" : "#ba7517";
        tabButton[numLigne][numColonne].setStyle("-fx-background-radius: 150em;" +"-fx-min-width: 44px; -fx-min-height: 44px;" +"-fx-max-width: 44px; -fx-max-height: 44px;" +"-fx-background-color: " + couleur + ";" +"-fx-border-color: " + borderColor + ";" +"-fx-border-radius: 150em;" + "-fx-border-width: 1.5px;");
    }

    private void stylePanel(Label nom, Label score, String accentColor) {
        nom.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" +"-fx-text-fill: #c5c5e0; -fx-alignment: center; -fx-text-alignment: center;");
        score.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;" + "-fx-text-fill: #e8e8f8;");
    }

    public VBox getPanneauJoueur1(String nom, int score) {
        labelNomJ1.setText(nom);
        labelScoreJ1.setText(String.valueOf(score));
        stylePanel(labelNomJ1, labelScoreJ1, "#e24b4a");

        Label badge = new Label("J1");
        badge.setStyle("-fx-background-color: #e24b4a;" +"-fx-text-fill: #fcebeb;" +"-fx-background-radius: 150em;" +"-fx-padding: 6 10 6 10;" +"-fx-font-size: 12px; -fx-font-weight: bold;");
        return buildPanel(badge, labelNomJ1, labelScoreJ1, listeCoupsJ1);
    }

    public VBox getPanneauJoueur2(String nom, int score) {
        labelNomJ2.setText(nom);
        labelScoreJ2.setText(String.valueOf(score));
        stylePanel(labelNomJ2, labelScoreJ2, "#ef9f27");

        Label badge = new Label("J2");
        badge.setStyle("-fx-background-color: #ef9f27;" +"-fx-text-fill: #412402;" +"-fx-background-radius: 150em;" +"-fx-padding: 6 10 6 10;" +"-fx-font-size: 12px; -fx-font-weight: bold;"
        );
        return buildPanel(badge, labelNomJ2, labelScoreJ2, listeCoupsJ2);
    }

    private VBox buildPanel(Label badge, Label nom, Label score, javafx.scene.control.ListView<String> listeCoup) {
        Label scoreLabel = new Label("score");
        scoreLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b6b8a;");

        Label coupsLabel = new Label("Historique des coups");
        coupsLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b6b8a;");

        VBox vbox = new VBox(12, badge, nom, scoreLabel, score, coupsLabel, listeCoup);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 16, 20, 16));
        vbox.setStyle("-fx-background-color: " + PANEL_BG + ";" + "-fx-background-radius: 12px;" +"-fx-border-color: #2a2a4a;" +"-fx-border-radius: 12px;" +"-fx-border-width: 1px;" +"-fx-min-width: 140px;");
        return vbox;
    }
    public void ajouterCoupJ1(int colonne) {
        int num = listeCoupsJ1.getItems().size() + 1;
        listeCoupsJ1.getItems().add("Coup " + num + " → Colonne " + colonne);
    }

    public void ajouterCoupJ2(int colonne) {
        int num = listeCoupsJ2.getItems().size() + 1;
        listeCoupsJ2.getItems().add("Coup " + num + " → Colonne " + colonne);
    }

    public void updateScoreJ1(int score) {
    	labelScoreJ1.setText(String.valueOf(score)); 
    }
    
    public void updateScoreJ2(int score) {
    	labelScoreJ2.setText(String.valueOf(score)); 
    }
    

    public GridPane getGrilleJeu() {
    	return grilleJeu; 
    }
    
    public void setGrilleJeu(GridPane grilleJeu) {
    	this.grilleJeu = grilleJeu; 
    }
    
    public Button[][] getTabButton() {
    	return tabButton; 
    }
    
    public void setTabButton(Button[][] tabButton) {
    	this.tabButton = tabButton; 
    }
}