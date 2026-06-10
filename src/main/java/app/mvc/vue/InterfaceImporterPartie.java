package app.mvc.vue;

import app.mvc.model.Coup;
import app.mvc.model.GestionFichier;
import app.mvc.model.Partie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class InterfaceImporterPartie {

    
    private static final String DARK_BG = "#0f0f1a";
    private static final String PANEL_BG = "#1a1a2e";
    private static final String BORDER = "#2a2a4a";
    private static final String TEXT_MAIN = "#e8e8f8";
    private static final String TEXT_SUB = "#6b6b8a";
    private static final String ACCENT = "#4a90d9";

    private GestionFichier gestionFichier = new GestionFichier();

    public void afficher() {
        Stage stage = new Stage();
        stage.setTitle("Importer une partie");
        Label titre = new Label("Importer une Partie");
        titre.setStyle( "-fx-font-size: 22px; -fx-font-weight: bold;" +"-fx-text-fill: " + TEXT_MAIN + ";");
        VBox enTete = new VBox(14, titre);
        enTete.setPadding(new Insets(0, 0, 16, 0));

        TableView<String> tableParties = new TableView<>();
        tableParties.setStyle("-fx-background-color: " + PANEL_BG + ";" +"-fx-border-color: " + BORDER + ";" +"-fx-border-radius: 10px; -fx-background-radius: 10px;");

        TableColumn<String, String> colFichier = new TableColumn<>("Fichier de partie");
        colFichier.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        tableParties.getColumns().add(colFichier);
        chargerFichiers(tableParties);

        Label labelInfo = new Label("Sélectionnez une partie...");
        TextArea zoneCoups = new TextArea();
        
        tableParties.getSelectionModel().selectedItemProperty().addListener(
            (obs, ancien, nomFichier) -> {
                if (nomFichier == null) return;
                String chemin = gestionFichier.getDossierSauvegarde() + "/" + nomFichier;
                try {
                    Partie p = gestionFichier.lecture(chemin);
                    labelInfo.setText("J1: " + p.getJ1().getNom() + " (score: " + p.getJ1().getScore() + ")  |  " +"J2: " + p.getJ2().getNom() + " (score: " + p.getJ2().getScore() + ")  |  " + "Date: " + p.getDate());
                    labelInfo.setStyle("-fx-text-fill: " + TEXT_MAIN + "; -fx-font-size: 13px;");
                    StringBuilder sb = new StringBuilder();
                    List<Coup> coups = p.getLisCoupJ();
                    for (int i=0; i<coups.size(); i++) {
                        Coup c = coups.get(i);
                        sb.append("Tour ").append(i+1)
                          .append("  →  J1: col ").append(c.getCoupJ1())
                          .append("   |   J2: col ").append(c.getCoupJ2())
                          .append("\n");
                    }
                    zoneCoups.setText(sb.length() > 0 ? sb.toString() : "(aucun coup enregistré)");
                } catch (IOException e) {
                    zoneCoups.setText("Erreur de lecture : " + e.getMessage());
                }
            });

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + DARK_BG + ";");
        root.setPadding(new Insets(20));
        root.setTop(enTete);
        root.setCenter(new VBox(10, tableParties, labelInfo, zoneCoups));

        stage.setScene(new Scene(root, 550, 560));
        stage.show();
    }

    private void chargerFichiers(TableView<String> table) {
        List<String> fichiers = gestionFichier.listFile(gestionFichier.getDossierSauvegarde());
        table.setItems(FXCollections.observableArrayList(fichiers));
    }
}