package app.mvc.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DAOJoueur implements DAO<Joueur> {

    private Connection connection;

    public DAOJoueur() {
        this.connection = ConnexionBD.getInstance().getConnection();
    }
  
    @Override
    public List<Joueur> findAll() {
        List<Joueur> joueurs = new ArrayList<>();
        String sql = "SELECT id, nom, score FROM Joueur";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                joueurs.add(new Joueur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getInt("score")
                ));
            }
        } catch (SQLException e) {
            System.err.println("findAll() - Erreur : " + e.getMessage());
        }
        return joueurs;
    }

    @Override
    public void insert(Joueur j) {
        String sql = "INSERT INTO Joueur (nom, score) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, j.getNom());
            ps.setInt(2, j.getScore());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("insert() - Erreur : " + e.getMessage());
        }
    }

    @Override
    public void update(Joueur j) {
        String sql = "UPDATE Joueur SET score = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, j.getScore());
            ps.setInt(2, j.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("update() - Erreur : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Joueur WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("delete() - Erreur : " + e.getMessage());
        }
    }
}