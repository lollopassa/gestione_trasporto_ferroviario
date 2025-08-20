// src/main/java/dao/TipoDAO.java
package dao;

import domain.Tipo;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoDAO {
    private static final String PROPS   = "gestoreuser.properties";
    private static final String SEL_ALL = "SELECT marca, modello FROM tipo ORDER BY marca, modello";
    private static final String INS     = "INSERT INTO tipo(marca, modello) VALUES(?, ?)";
    private static final String UPD     = "UPDATE tipo SET marca = ?, modello = ? WHERE marca = ? AND modello = ?";
    private static final String DEL     = "DELETE FROM tipo WHERE marca = ? AND modello = ?";

    /** Restituisce tutti i tipi (marca+modello). */
    public List<Tipo> listAll() throws DAOException {
        List<Tipo> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SEL_ALL)) {
            while (rs.next()) {
                out.add(new Tipo(
                        rs.getString("marca"),
                        rs.getString("modello")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura tipi treno: " + e.getMessage(), e);
        }
    }

    /** Inserisce un nuovo tipo. */
    public void insert(Tipo t) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(INS)) {
            ps.setString(1, t.getMarca());
            ps.setString(2, t.getModello());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento tipo: " + e.getMessage(), e);
        }
    }


    public void update(String oldMarca, String oldModello, Tipo newTipo) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(UPD)) {
            ps.setString(1, newTipo.getMarca());
            ps.setString(2, newTipo.getModello());
            ps.setString(3, oldMarca);
            ps.setString(4, oldModello);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento tipo: " + e.getMessage(), e);
        }
    }

    public void delete(String marca, String modello) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(DEL)) {
            ps.setString(1, marca);
            ps.setString(2, modello);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore cancellazione tipo: " + e.getMessage(), e);
        }
    }
}
