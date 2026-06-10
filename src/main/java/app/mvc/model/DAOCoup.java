package app.mvc.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCoup implements DAO<Coup> {

    private Connection connection;

    public DAOCoup() {
        this.connection = ConnexionBD.getInstance().getConnection();
    }

    @Override
    public List<Coup> findAll() {
        List<Coup> coups = new ArrayList<>();
        String sql = "SELECT coupj1, coupj2 FROM coup";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Coup c = new Coup(rs.getInt("coupj1"));
                c.setCoupJ2(rs.getInt("coupj2"));
                coups.add(c);
            }
        } catch (SQLException e) {
            System.err.println("findAll() - Erreur : " + e.getMessage());
        }
        return coups;
    }

    public List<Coup> findByPartie(int idPartie) {
        List<Coup> coups = new ArrayList<>();
        String sql = "SELECT coupj1, coupj2 FROM coup WHERE idp = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPartie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Coup c = new Coup(rs.getInt("coupj1"));
                c.setCoupJ2(rs.getInt("coupj2"));
                coups.add(c);
            }
        } catch (SQLException e) {
            System.err.println("findByPartie() - Erreur : " + e.getMessage());
        }
        return coups;
    }

   public void insert(Coup c, int idPartie, int idCoup) {
        String sql = "INSERT INTO coup (idp, idc, coupj1, coupj2) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPartie);
            ps.setInt(2, idCoup);
            ps.setInt(3, c.getCoupJ1());
            ps.setInt(4, c.getCoupJ2());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("insert() - Erreur : " + e.getMessage());
        }
    }

    @Override
    public void insert(Coup c) {
        System.err.println("insert(Coup) : utilise insert(Coup, idPartie, idCoup) à la place.");
    }

    @Override
    public void update(Coup c) {
        //Mezelet
    }

    @Override
    public void delete(int idPartie) {
        String sql = "DELETE FROM coup WHERE idp = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPartie);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("delete() - Erreur : " + e.getMessage());
        }
    }
}