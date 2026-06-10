package app.mvc.vue;

import app.mvc.model.DAOJoueur;
import app.mvc.model.Joueur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class InterfaceListeJoueur {

    private static final String DARK_BG = "#0f0f1a";
    private static final String PANEL_BG = "#1a1a2e";
    private static final String BORDER = "#2a2a4a";
    private static final String TEXT_MAIN = "#e8e8f8";
    private static final String TEXT_SUB = "#6b6b8a";
    private static final String ACCENT = "#4a90d9";

    private BorderPane root;
    private TableView<Joueur> tableJoueurs;
    private ObservableList<Joueur> listeObservable;
    private Label labelStatut;
    private DAOJoueur daoJoueur;

    public InterfaceListeJoueur() {
        this.daoJoueur = new DAOJoueur();
        this.listeObservable = FXCollections.observableArrayList();
        this.root = new BorderPane();
        construire();
        chargerJoueurs();
    }

    private void construire() {
        root.setStyle("-fx-background-color: " + DARK_BG + ";");
        root.setPadding(new Insets(20));
        root.setTop(creerEnTete());
        root.setCenter(creerTable());
    }

    private VBox creerEnTete() {
        Label titre = new Label("Liste des Joueurs");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;" +"-fx-text-fill: " + TEXT_MAIN + ";");
        VBox enTete = new VBox(14, titre);
        enTete.setPadding(new Insets(0, 0, 16, 0));
        return enTete;
    }

    private VBox creerTable() {
        tableJoueurs = new TableView<>();
        tableJoueurs.setItems(listeObservable);
        tableJoueurs.setStyle("-fx-background-color: " + PANEL_BG + ";" +"-fx-border-color: " + BORDER + ";" +"-fx-border-radius: 10px; -fx-background-radius: 10px;");

        TableColumn<Joueur, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Joueur, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Joueur, Integer> colScore = new TableColumn<>("Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        //getscore automatiquement
        tableJoueurs.getColumns().addAll(colId, colNom, colScore);

        VBox corps = new VBox(tableJoueurs);
        return corps;
    }

    public void chargerJoueurs() {
        List<Joueur> joueurs = daoJoueur.findAll();
        listeObservable.setAll(joueurs);
    }

    public BorderPane getRoot() {
        return root;
    }
}