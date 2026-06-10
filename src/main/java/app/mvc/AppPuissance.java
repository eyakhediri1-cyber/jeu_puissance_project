package app.mvc;

import app.mvc.control.Controlleur;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AppPuissance extends Application {
	
	Controlleur control = new Controlleur();

    @Override
    public void start(Stage stage) {
        control.gameControlleur();
        Scene scene = new Scene(control.getFenetre(), 900, 560);
        stage.setTitle("Jeu Puissance 4 ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
