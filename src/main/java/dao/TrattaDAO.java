package dao;

import domain.Tratta;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrattaDAO {

    private static final String PROPS = "gestoreuser.properties";

    private static final String SEL_ALL =
            "SELECT dep_nome_stazione, arr_nome_stazione, km, num_treni_operativi " +
                    "FROM tratta " +
                    "ORDER BY dep_nome_stazione, arr_nome_stazione";

    private static final String INS =
            "INSERT INTO tratta(dep_nome_stazione, arr_nome_stazione, km, num_treni_operativi) " +
                    "VALUES (?,?,?,?)";

    private static final String UPD =
            "UPDATE tratta SET dep_nome_stazione=?, arr_nome_stazione=?, km=?, num_treni_operativi=? " +
                    "WHERE dep_nome_stazione=? AND arr_nome_stazione=?";

    private static final String DEL =
            "DELETE FROM tratta WHERE dep_nome_stazione=? AND arr_nome_stazione=?";

    public List<Tratta> listAll() throws DAOException {
        List<Tratta> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SEL_ALL)) {
            while (rs.next()) {
                result.add(new Tratta(
                        rs.getString("dep_nome_stazione"),
                        rs.getString("arr_nome_stazione"),
                        rs.getInt("km"),
                        rs.getInt("num_treni_operativi")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura tratte: " + e.getMessage(), e);
        }
        return result;
    }

    public void insert(Tratta tratta) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(INS)) {
            ps.setString(1, tratta.getDepNomeStazione());
            ps.setString(2, tratta.getArrNomeStazione());
            ps.setInt(3, tratta.getKm());
            ps.setInt(4, tratta.getNumTreniOperativi());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento tratta: " + e.getMessage(), e);
        }
    }

    public void update(Tratta oldTratta, Tratta newTratta) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(UPD)) {
            ps.setString(1, newTratta.getDepNomeStazione());
            ps.setString(2, newTratta.getArrNomeStazione());
            ps.setInt(3, newTratta.getKm());
            ps.setInt(4, newTratta.getNumTreniOperativi());
            // where
            ps.setString(5, oldTratta.getDepNomeStazione());
            ps.setString(6, oldTratta.getArrNomeStazione());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento tratta: " + e.getMessage(), e);
        }
    }

    public void delete(Tratta tratta) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(DEL)) {
            ps.setString(1, tratta.getDepNomeStazione());
            ps.setString(2, tratta.getArrNomeStazione());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore cancellazione tratta: " + e.getMessage(), e);
        }
    }
}
