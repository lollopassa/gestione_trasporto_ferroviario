package dao;

import domain.Stazione;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StazioneDAO {

    private static final String PROPS = "gestoreuser.properties";

    private static final String SEL_ALL =
            "SELECT nome_stazione, citta, provincia FROM stazione ORDER BY citta, nome_stazione";

    private static final String INS =
            "INSERT INTO stazione(nome_stazione, citta, provincia) VALUES(?,?,?)";

    private static final String UPD =
            "UPDATE stazione SET nome_stazione = ?, citta = ?, provincia = ? WHERE nome_stazione = ?";

    private static final String DEL =
            "DELETE FROM stazione WHERE nome_stazione = ?";

    public List<Stazione> listAll() throws DAOException {
        List<Stazione> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SEL_ALL)) {
            while (rs.next()) {
                result.add(new Stazione(
                        rs.getString("nome_stazione"),
                        rs.getString("citta"),
                        rs.getString("provincia")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura stazioni: " + e.getMessage(), e);
        }
        return result;
    }

    public void insert(Stazione stazione) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(INS)) {
            ps.setString(1, stazione.getNomeStazione());
            ps.setString(2, stazione.getCitta());
            ps.setString(3, stazione.getProvincia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento stazione: " + e.getMessage(), e);
        }
    }

    public void update(Stazione oldS, Stazione newS) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(UPD)) {
            ps.setString(1, newS.getNomeStazione());
            ps.setString(2, newS.getCitta());
            ps.setString(3, newS.getProvincia());
            ps.setString(4, oldS.getNomeStazione());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento stazione: " + e.getMessage(), e);
        }
    }

    public void delete(Stazione s) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(DEL)) {
            ps.setString(1, s.getNomeStazione());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore cancellazione stazione: " + e.getMessage(), e);
        }
    }
}
