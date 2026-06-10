package app.mvc.vue;

import app.mvc.model.DAOPartie;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class InterfaceListePartie {

    private static final String DARK_BG = "#0f0f1a";
    private static final String PANEL_BG = "#1a1a2e";
    private static final String BORDER = "#2a2a4a";
    private static final String TEXT_MAIN = "#e8e8f8";
    private static final String TEXT_SUB = "#6b6b8a";
    private static final String ACCENT = "#4a90d9";

    private BorderPane root;
    private TableView<String[]> tableParties;
    private ObservableList<String[]> listeObservable;
    private Label labelStatut;
    private DAOPartie daoPartie;

    public InterfaceListePartie() {
        this.daoPartie = new DAOPartie();
        this.listeObservable = FXCollections.observableArrayList();
        this.root = new BorderPane();
        construire();
        chargerParties();
    }

   
    private void construire() {
        root.setStyle("-fx-background-color: " + DARK_BG + ";");
        root.setPadding(new Insets(20));
        root.setTop(creerEnTete());
        root.setCenter(creerTable());
    }

    private VBox creerEnTete() {
        Label titre = new Label("Liste des Parties");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;" +"-fx-text-fill: " + TEXT_MAIN + ";");

        VBox enTete = new VBox(14, titre);
        enTete.setPadding(new Insets(0, 0, 16, 0));
        return enTete;
    }

    private VBox creerTable() {
        tableParties = new TableView<>();
        tableParties.setItems(listeObservable);
        tableParties.setStyle("-fx-background-color: " + PANEL_BG + ";" +"-fx-border-color: " + BORDER + ";" +"-fx-border-radius: 10px; -fx-background-radius: 10px;");

        String[] titres = {"ID", "Joueur 1", "Joueur 2", "Score J1", "Score J2", "Date"};
        for (int i=0; i<titres.length; i++) {
            final int index = i;
            TableColumn<String[], String> col = new TableColumn<>(titres[i]);
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[index]));
            tableParties.getColumns().add(col);
        }

        VBox corps = new VBox(tableParties);
        return corps;
    }

    public void chargerParties() {
        List<String[]> parties = daoPartie.getPartiesData();
        listeObservable.setAll(parties);
    }

    public BorderPane getRoot() { 
    	return root; 
    }
}
