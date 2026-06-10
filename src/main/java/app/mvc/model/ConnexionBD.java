package app.mvc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {
    //
    private static ConnexionBD instance = null;
    private Connection connection = null;

    private static final String URL = "jdbc:mysql://localhost:3306/puissance4";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    //
    private ConnexionBD() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion reussie.");
        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
    //
    public static ConnexionBD getInstance() {
        if (instance == null) {
            instance = new ConnexionBD();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
