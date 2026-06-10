package app.mvc.control;

import app.mvc.model.*;
import app.mvc.vue.InterfaceImporterPartie;
import app.mvc.vue.InterfaceJeuPuissance;
import app.mvc.vue.InterfaceListeJoueur;
import app.mvc.vue.InterfaceListePartie;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class Controlleur {

    private BorderPane fenetre;
    private InterfaceJeuPuissance interfaceJeu;
    private Joueur j1;
    private Joueur j2;
    private Joueur joueurCourant;
    private Game game;
    private Partie partie;

    private DAOJoueur daoJoueur;
    private DAOPartie daoPartie;
    private GestionFichier gestionFichier;

    private Agent agent;
    private boolean modeSimulation = false;

    private CheckMenuItem checkEnregistrer;

    public Controlleur() {
        this.fenetre = new BorderPane();
        this.daoJoueur  = new DAOJoueur();
        this.daoPartie = new DAOPartie();
        this.gestionFichier = new GestionFichier();
    }

    public MenuBar creerMenu() {
        MenuBar menuBar = new MenuBar();

        Menu menuPartie = new Menu("Partie");

        MenuItem mniLancer = new MenuItem("Lancer une partie");
        mniLancer.setOnAction(e -> lancerPartie());

        MenuItem mniSimuler = new MenuItem("Simuler une partie");
        mniSimuler.setOnAction(e -> simulerPartie());

        checkEnregistrer = new CheckMenuItem("Enregistrer la partie");

        Menu mnuImportExport = new Menu("Importer/Exporter une partie");

        MenuItem mniImporter = new MenuItem("Importer une partie");
        mniImporter.setOnAction(e -> ouvrirFenetreImporter());

        MenuItem mniExporter = new MenuItem("Exporter une partie");
        mniExporter.setOnAction(e -> exporterPartie());

        mnuImportExport.getItems().addAll(mniImporter, mniExporter);

        menuPartie.getItems().addAll(mniLancer, mniSimuler,new SeparatorMenuItem(),checkEnregistrer,new SeparatorMenuItem(), mnuImportExport);

       
        Menu menuClassement = new Menu("Classement");

        MenuItem mniListeJoueurs = new MenuItem("La liste des joueurs");
        mniListeJoueurs.setOnAction(e -> afficherListeJoueurs());

        MenuItem mniListeParties = new MenuItem("La liste des parties"); 
        mniListeParties.setOnAction(e -> afficherListeParties());
        
        MenuItem mniListeParNbParties = new MenuItem("Liste par nombre de parties jouées");
        mniListeParNbParties.setOnAction(e -> afficherListeJoueursParParties());

        menuClassement.getItems().addAll(mniListeJoueurs, mniListeParties, mniListeParNbParties);
       
        
        Menu menuJoueur = new Menu("Joueur");

        MenuItem mniAjouterJoueur = new MenuItem("Ajouter Joueur");
        mniAjouterJoueur.setOnAction(e -> ajouterJoueur());

        MenuItem mniDetails = new MenuItem("Détails joueurs");
        mniDetails.setOnAction(e -> afficherDetailsJoueur());

        MenuItem mniHistorique = new MenuItem("Historique du joueur");
        mniHistorique.setOnAction(e -> afficherHistoriqueJoueur());

        menuJoueur.getItems().addAll(mniAjouterJoueur, mniDetails, mniHistorique);

        
        Menu menuQuitter = new Menu("Quitter");
        MenuItem mniQuitter = new MenuItem("Quitter");
        mniQuitter.setOnAction(e -> javafx.application.Platform.exit());
        menuQuitter.getItems().add(mniQuitter);

        menuBar.getMenus().addAll(menuPartie, menuClassement, menuJoueur, menuQuitter);
        return menuBar;
    }

    public void lancerPartie() {
        modeSimulation = false;
        agent = null;
        List<Joueur> joueurs = daoJoueur.findAll();
        if (joueurs.size() >= 2) {
            j1 = joueurs.get(0);
            j2 = joueurs.get(1);
        } else {
            j1 = new Joueur(1, "Joueur Rouge", 0);
            j2 = new Joueur(2, "Joueur Jaune", 0);
        }
        initialiserGrille();
    }

    private void simulerPartie() {
        modeSimulation = true;
        List<Joueur> joueurs = daoJoueur.findAll();
        if (joueurs.isEmpty()) {
            j1 = new Joueur(1, "Vous", 0);
        } else {
            j1 = joueurs.get(0);
        }
        agent = new Agent(2, "Agent IA");
        j2 = agent;
        initialiserGrille();
    }

    
    private void initialiserGrille() {
        game = new Game(j1.getId(), j2.getId());
        partie = new Partie(j1, j2);
        joueurCourant = j1;

        interfaceJeu = new InterfaceJeuPuissance();
        interfaceJeu.dessiner();

        fenetre.setLeft(interfaceJeu.getPanneauJoueur1(j1.getNom(), j1.getScore()));
        fenetre.setCenter(interfaceJeu.getGrilleJeu());
        String nomJ2 = modeSimulation ? "Agent" + j2.getNom() : j2.getNom();
        fenetre.setRight(interfaceJeu.getPanneauJoueur2(nomJ2, j2.getScore()));
        attacherBoutons();
    }

    
    private void attacherBoutons() {
        Button[][] tabButton = interfaceJeu.getTabButton();
        for (int col=0; col<7; col++) {
            final int colonne = col;
            for (int row=0; row<6; row++) {
                if (modeSimulation) {
                    tabButton[row][col].setOnAction(event -> gererClicSimulation(colonne));
                } else {
                    tabButton[row][col].setOnAction(event -> jouerCoup(colonne));
                }
            }
        }
    }

    public void gameControlleur() {
        fenetre.setTop(creerMenu());
        lancerPartie();
    }

    //Thread
    private void gererClicSimulation(int colonne) {
        if (joueurCourant != j1) return; // bloque 

        boolean fini = jouerCoup(colonne);
        if (!fini) {
            setButtonsDisabled(true);
            Thread threadAgent = new Thread(() -> {
                try {
                	Thread.sleep(600); 
                } catch (InterruptedException e) {
                	Thread.currentThread().interrupt(); 
                }

                int colonneAgent = agent.choisirColonne(game, j1.getId());
                //remet l'exécution sur le thread principal (matkhalich modification tsir or thred principal)
                Platform.runLater(() -> {
                    jouerCoup(colonneAgent-1); 
                    setButtonsDisabled(false);
                });
            });
            threadAgent.setDaemon(true);
            threadAgent.start();
        }
    }

    
    private boolean jouerCoup(int colonne) {
        try {
            int ligne = game.getLigneVideByColonne(colonne+1);
            game.setCoup(ligne, colonne, joueurCourant.getId());
            partie.insertCoup(colonne+1);

            String couleur = (joueurCourant == j1) ? "red" : "yellow";
            interfaceJeu.setCouleurButton(ligne, colonne, couleur);

            if (joueurCourant == j1) {
            	interfaceJeu.ajouterCoupJ1(colonne+1);
            } else {
            	interfaceJeu.ajouterCoupJ2(colonne+1);
            }
            Position pos = new Position(ligne, colonne);

            if (game.estGagnant(pos, joueurCourant.getId())) {
                joueurCourant.incrementerScore();
                (joueurCourant == j1 ? j2 : j1).decrementerScore();
                interfaceJeu.updateScoreJ1(j1.getScore());
                interfaceJeu.updateScoreJ2(j2.getScore());
                daoJoueur.update(j1);
                if (!modeSimulation) {
                    daoJoueur.update(j2);
                    daoPartie.insert(partie);
                }
                sauvegarderSiDemande();
                afficherGagnant(joueurCourant.getNom());
                return true;

                } else if (game.estFin()) {
                if (!modeSimulation) {
                	daoPartie.insert(partie);
                }
                sauvegarderSiDemande();
                afficherMatchNul();
                return true;

            } else {
                partie.modifierRole();
                joueurCourant = partie.getJoueurCourant();
                return false;
            }

        } catch (CoupException e) {
        	return false; 
        }
    }

    private void setButtonsDisabled(boolean disabled) {
        Button[][] tab = interfaceJeu.getTabButton();
        for (int r=0;r< 6; r++)
            for (int c=0; c<7; c++)
                tab[r][c].setDisable(disabled);
    }

   private void sauvegarderSiDemande() {
        if (checkEnregistrer != null && checkEnregistrer.isSelected()) {
            exporterPartie();
        }
    }

    private void exporterPartie() {
        if (partie == null) {
            new Alert(Alert.AlertType.WARNING, "Aucune partie en cours.").showAndWait();
            return;
        }
        try {
            gestionFichier.enregistrePartie(partie);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Exportation réussie");
            info.setHeaderText(null);
            info.setContentText("Partie exportée dans le dossier '" + gestionFichier.getDossierSauvegarde() + "/'.");
            info.showAndWait();
        } catch (IOException ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Erreur d'exportation");
            err.setContentText(ex.getMessage());
            err.showAndWait();
        }
    }

    private void ouvrirFenetreImporter() {
        new InterfaceImporterPartie().afficher();
    }
   
    private void afficherGagnant(String nom) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de partie");
        alert.setHeaderText("Victoire !");
        alert.setContentText(nom + " a gagné la partie !");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) relancerPartie();
    }
  
    private void afficherMatchNul() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de partie");
        alert.setHeaderText("Match nul !");
        alert.setContentText("La grille est pleine. Aucun gagnant !");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) relancerPartie();
    }

    private void relancerPartie() {
        game.initialiser();
        partie = new Partie(j1, j2);
        joueurCourant = j1;
        interfaceJeu.dessiner();
        fenetre.setLeft(interfaceJeu.getPanneauJoueur1(j1.getNom(), j1.getScore()));
        fenetre.setCenter(interfaceJeu.getGrilleJeu());
        String nomJ2 = modeSimulation ?   j2.getNom() : j2.getNom();
        fenetre.setRight(interfaceJeu.getPanneauJoueur2(nomJ2, j2.getScore()));
        attacherBoutons();
    }

    private void afficherListeJoueurs() {
        InterfaceListeJoueur vue = new InterfaceListeJoueur();
        Stage stage = new Stage();
        stage.setTitle("Liste des joueurs");
        stage.setScene(new Scene(vue.getRoot(), 400, 300));
        stage.show();
    }

    private void afficherListeParties() {
        InterfaceListePartie vue = new InterfaceListePartie();
        Stage stage = new Stage();
        stage.setTitle("Liste des parties");
        stage.setScene(new Scene(vue.getRoot(), 700, 400));
        stage.show();
    }
    
    private TableView<Joueur> construireTableJoueurs(List<Joueur> joueurs) {
        TableView<Joueur> table = new TableView<>();
        TableColumn<Joueur, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Joueur, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        TableColumn<Joueur, Integer> colScore = new TableColumn<>("Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        table.getColumns().addAll(colId, colNom, colScore);
        table.setItems(FXCollections.observableArrayList(joueurs));
        return table;
    }

    private void afficherDetailsJoueur() {
        List<Joueur> joueurs = daoJoueur.findAll();
        Stage stage = new Stage();
        stage.setTitle("Détails d'un joueur");
        TableView<Joueur> table = construireTableJoueurs(joueurs);
        Label labelDetail = new Label("Sélectionnez un joueur.");
        table.getSelectionModel().selectedItemProperty().addListener((obs, ancien, nouveau) -> {
            if (nouveau != null)
                labelDetail.setText("ID : " + nouveau.getId() + " | Nom : " + nouveau.getNom() + " | Score : " + nouveau.getScore());
        });
        VBox vbox = new VBox(10, table, labelDetail);
        stage.setScene(new Scene(vbox, 450, 350));
        stage.show();
    }

    private void afficherHistoriqueJoueur() {
        Stage stage = new Stage();
        stage.setTitle("Historique des parties");
        TableView<String[]> table = new TableView<>();
        String[] colonnes = {"ID", "Joueur 1", "Joueur 2", "Score J1", "Score J2", "Date"};
        for (int i=0; i<colonnes.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(colonnes[i]);
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[idx]));
            table.getColumns().add(col);
        }
        table.setItems(FXCollections.observableArrayList(daoPartie.getPartiesData()));
        VBox vbox = new VBox(10, table);
        stage.setScene(new Scene(vbox, 700, 400));
        stage.show();
    }

    private void ajouterJoueur() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un joueur");
        dialog.setHeaderText("Entrez le nom du joueur :");
        dialog.showAndWait().ifPresent(nom -> daoJoueur.insert(new Joueur(0, nom, 0)));
    }

    private void afficherListeJoueursParParties() {
        List<Joueur> joueurs = daoJoueur.findAll();
        joueurs.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        Stage stage = new Stage();
        stage.setTitle("Classement par score");
        VBox vbox = new VBox(10, construireTableJoueurs(joueurs));
        stage.setScene(new Scene(vbox, 400, 300));
        stage.show();
    }
    public BorderPane getFenetre() {
    	return fenetre; 
    }
    public void setFenetre(BorderPane fenetre) {
    	this.fenetre = fenetre; 
    }
    public InterfaceJeuPuissance getInterfaceJeu() {
    	return interfaceJeu; 
    }
    public void setInterfaceJeu(InterfaceJeuPuissance ij) {
    	this.interfaceJeu = ij; 
    }
    public Joueur getJ1() {
    	return j1; 
    }
    public void setJ1(Joueur j1) {
    	this.j1 = j1; 
    }
    public Joueur getJ2() {
    	return j2; 
    }
    public void setJ2(Joueur j2) {
    	this.j2 = j2; 
    }
    public Joueur getJoueurCourant() {
    	return joueurCourant; 
    }
    public void setJoueurCourant(Joueur j) {
    	this.joueurCourant = j; 
    }
    public Game getGame() { 
    	return game; 
    }
    public void setGame(Game game) {
    	this.game = game; 
    }
    public Partie getPartie() {
    	return partie; 
    }
    public void setPartie(Partie partie) {
    	this.partie = partie; 
    }
}
