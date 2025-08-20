package dao;

import domain.Stazione;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StazioneDAO {
    private static final String PROPS      = "gestoreuser.properties";
    private static final String SEL_ALL    =
            "SELECT nome_stazione, citta, provincia FROM stazione ORDER BY citta, nome_stazione";
    private static final String INS        =
            "INSERT INTO stazione(nome_stazione, citta, provincia) VALUES(?, ?, ?)";
    private static final String UPD        =
            "UPDATE stazione SET nome_stazione = ?, citta = ?, provincia = ? " +
                    "WHERE nome_stazione = ? AND citta = ? AND provincia = ?";
    private static final String DEL        =
            "DELETE FROM stazione WHERE nome_stazione = ? AND citta = ? AND provincia = ?";

    public List<Stazione> listAll() throws DAOException {
        List<Stazione> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SEL_ALL)) {
            while (rs.next()) {
                out.add(new Stazione(
                        rs.getString("nome_stazione"),
                        rs.getString("citta"),
                        rs.getString("provincia")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura stazioni: " + e.getMessage(), e);
        }
    }

    public void insert(Stazione s) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(INS)) {
            ps.setString(1, s.getNomeStazione());
            ps.setString(2, s.getCitta());
            ps.setString(3, s.getProvincia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento stazione: " + e.getMessage(), e);
        }
    }

    public void update(Stazione oldS, Stazione newS) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(UPD)) {
            ps.setString(1, newS.getNomeStazione());
            ps.setString(2, newS.getCitta());
            ps.setString(3, newS.getProvincia());
            ps.setString(4, oldS.getNomeStazione());
            ps.setString(5, oldS.getCitta());
            ps.setString(6, oldS.getProvincia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento stazione: " + e.getMessage(), e);
        }
    }

    public void delete(Stazione s) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(DEL)) {
            ps.setString(1, s.getNomeStazione());
            ps.setString(2, s.getCitta());
            ps.setString(3, s.getProvincia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore cancellazione stazione: " + e.getMessage(), e);
        }
    }
}
