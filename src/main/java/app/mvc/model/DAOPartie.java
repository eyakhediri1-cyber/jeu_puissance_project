package app.mvc.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DAOPartie implements DAO<Partie> {

    private Connection connection;

    public DAOPartie() {
        this.connection = ConnexionBD.getInstance().getConnection();
    }

    
    @Override
    public void insert(Partie p) {
        String sqlPartie = "INSERT INTO partie (j1, j2, scorej1, scorej2, date) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlPartie, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getJ1().getId());
            ps.setInt(2, p.getJ2().getId());
            ps.setInt(3, p.getJ1().getScore());
            ps.setInt(4, p.getJ2().getScore());
            ps.setDate(5, Date.valueOf(p.getDate()));
            ps.executeUpdate();

            
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int idPartie = keys.getInt(1);
                insertCoups(idPartie, p.getLisCoupJ());
            }
        } catch (SQLException e) {
            System.err.println("insert(Partie) - Erreur : " + e.getMessage());
        }
    }

    private void insertCoups(int idPartie, List<Coup> coups) {
        String sqlCoup = "INSERT INTO coup (idp, idc, coupj1, coupj2) VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlCoup)) {
            int idc = 1;
            for (Coup c : coups) {
                ps.setInt(1, idPartie);
                ps.setInt(2, idc++);
                ps.setInt(3, c.getCoupJ1());
                ps.setInt(4, c.getCoupJ2());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.err.println("insertCoups() - Erreur : " + e.getMessage());
        }
    }

   @Override
    public List<Partie> findAll() {
        return new ArrayList<>();
    }

    @Override public void update(Partie t)  {}
    @Override public void delete(int id)    {}

  public List<String[]> getPartiesData() {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT p.id, j1.nom, j2.nom, p.scorej1, p.scorej2, p.date " +
                     "FROM partie p " +
                     "JOIN Joueur j1 ON p.j1 = j1.id " +
                     "JOIN Joueur j2 ON p.j2 = j2.id " +
                     "ORDER BY p.date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{
                    String.valueOf(rs.getInt(1)),
                    rs.getString(2),
                    rs.getString(3),
                    String.valueOf(rs.getInt(4)),
                    String.valueOf(rs.getInt(5)),
                    rs.getString(6)
                });
            }
        } catch (SQLException e) {
            System.err.println("getPartiesData() - Erreur : " + e.getMessage());
        }
        return rows;
    }
}