package dao;

import domain.Tratta;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrattaDAO {
    private static final String PROPS   = "gestoreuser.properties";
    private static final String SEL_ALL =
            "SELECT dep_nome_stazione, dep_citta, dep_provincia, " +
                    "       arr_nome_stazione, arr_citta, arr_provincia, " +
                    "       km, num_treni_operativi " +
                    "  FROM tratta " +
                    " ORDER BY dep_citta, arr_citta";
    private static final String INS     =
            "INSERT INTO tratta(" +
                    "  dep_nome_stazione, dep_citta, dep_provincia," +
                    "  arr_nome_stazione, arr_citta, arr_provincia," +
                    "  km, num_treni_operativi" +
                    ") VALUES(?,?,?,?,?,?,?,?)";
    private static final String UPD     =
            "UPDATE tratta SET " +
                    "  dep_nome_stazione = ?, dep_citta = ?, dep_provincia = ?, " +
                    "  arr_nome_stazione = ?, arr_citta = ?, arr_provincia = ?, " +
                    "  km = ?, num_treni_operativi = ? " +
                    "WHERE dep_nome_stazione = ? AND dep_citta = ? AND dep_provincia = ? " +
                    "  AND arr_nome_stazione = ? AND arr_citta = ? AND arr_provincia = ?";
    private static final String DEL     =
            "DELETE FROM tratta " +
                    "WHERE dep_nome_stazione = ? AND dep_citta = ? AND dep_provincia = ? " +
                    "  AND arr_nome_stazione = ? AND arr_citta = ? AND arr_provincia = ?";

    public List<Tratta> listAll() throws DAOException {
        List<Tratta> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(SEL_ALL)) {
            while (rs.next()) {
                out.add(new Tratta(
                        rs.getString("dep_nome_stazione"),
                        rs.getString("dep_citta"),
                        rs.getString("dep_provincia"),
                        rs.getString("arr_nome_stazione"),
                        rs.getString("arr_citta"),
                        rs.getString("arr_provincia"),
                        rs.getInt("km"),
                        rs.getInt("num_treni_operativi")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura tratte: " + e.getMessage(), e);
        }
    }

    public void insert(Tratta t) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(INS)) {
            ps.setString(1, t.getDepNomeStazione());
            ps.setString(2, t.getDepCitta());
            ps.setString(3, t.getDepProvincia());
            ps.setString(4, t.getArrNomeStazione());
            ps.setString(5, t.getArrCitta());
            ps.setString(6, t.getArrProvincia());
            ps.setInt(7, t.getKm());
            ps.setInt(8, t.getNumTreniOperativi());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento tratta: " + e.getMessage(), e);
        }
    }

    public void update(Tratta oldT, Tratta newT) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(UPD)) {
            ps.setString(1, newT.getDepNomeStazione());
            ps.setString(2, newT.getDepCitta());
            ps.setString(3, newT.getDepProvincia());
            ps.setString(4, newT.getArrNomeStazione());
            ps.setString(5, newT.getArrCitta());
            ps.setString(6, newT.getArrProvincia());
            ps.setInt(7, newT.getKm());
            ps.setInt(8, newT.getNumTreniOperativi());
            // where old keys
            ps.setString(9, oldT.getDepNomeStazione());
            ps.setString(10, oldT.getDepCitta());
            ps.setString(11, oldT.getDepProvincia());
            ps.setString(12, oldT.getArrNomeStazione());
            ps.setString(13, oldT.getArrCitta());
            ps.setString(14, oldT.getArrProvincia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento tratta: " + e.getMessage(), e);
        }
    }

    public void delete(Tratta t) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(DEL)) {
            ps.setString(1, t.getDepNomeStazione());
            ps.setString(2, t.getDepCitta());
            ps.setString(3, t.getDepProvincia());
            ps.setString(4, t.getArrNomeStazione());
            ps.setString(5, t.getArrCitta());
            ps.setString(6, t.getArrProvincia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore cancellazione tratta: " + e.getMessage(), e);
        }
    }
}
