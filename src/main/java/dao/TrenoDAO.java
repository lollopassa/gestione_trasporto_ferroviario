package dao;

import domain.Tratta;
import domain.Treno;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrenoDAO {

    private static final String PROPS_PERSONALE = "personaleuser.properties";
    private static final String PROPS_GESTORE   = "gestoreuser.properties";

    private static final String SQL_LIGHT =
            "SELECT matricola, marca, modello " +
                    "FROM treno " +
                    "ORDER BY matricola";

    private static final String SQL_BY_TRATTA =
            "SELECT matricola, marca, modello, data_acquisto, orario_partenza, orario_arrivo, " +
                    "dep_nome_stazione, arr_nome_stazione " +
                    "FROM treno " +
                    "WHERE dep_nome_stazione = ? AND arr_nome_stazione = ? " +
                    "ORDER BY matricola";

    private static final String SQL_FULL =
            "SELECT matricola, marca, modello, data_acquisto, orario_partenza, orario_arrivo, " +
                    "dep_nome_stazione, arr_nome_stazione " +
                    "FROM treno " +
                    "ORDER BY matricola";

    private static final String INS =
            "INSERT INTO treno(" +
                    "matricola, marca, modello, data_acquisto, orario_partenza, orario_arrivo, " +
                    "dep_nome_stazione, arr_nome_stazione) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPD =
            "UPDATE treno SET marca = ?, modello = ?, data_acquisto = ?, orario_partenza = ?, orario_arrivo = ?, " +
                    "dep_nome_stazione = ?, arr_nome_stazione = ? " +
                    "WHERE matricola = ?";

    private static final String DEL =
            "DELETE FROM treno WHERE matricola = ?";

    private static final String SEL_VAGONI =
            "SELECT n_carrozza FROM carrozza WHERE matricola=? AND nome_classe=? ORDER BY n_carrozza";

    public List<Treno> getAllTreni() throws DAOException {
        List<Treno> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS_PERSONALE);
             PreparedStatement ps = conn.prepareStatement(SQL_LIGHT);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Treno(
                        rs.getString("matricola"),
                        rs.getString("marca"),
                        rs.getString("modello")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura elenco treni: " + e.getMessage(), e);
        }
        return result;
    }

    public List<Treno> listByTratta(Tratta tratta) throws DAOException {
        List<Treno> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS_PERSONALE);
             PreparedStatement ps = conn.prepareStatement(SQL_BY_TRATTA)) {
            ps.setString(1, tratta.getDepNomeStazione());
            ps.setString(2, tratta.getArrNomeStazione());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Treno(
                            rs.getString("matricola"),
                            rs.getString("marca"),
                            rs.getString("modello"),
                            rs.getDate("data_acquisto").toLocalDate(),
                            rs.getTime("orario_partenza").toLocalTime(),
                            rs.getTime("orario_arrivo").toLocalTime(),
                            rs.getString("dep_nome_stazione"),
                            rs.getString("arr_nome_stazione")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura treni per tratta: " + e.getMessage(), e);
        }
        return result;
    }

    public List<Treno> listAllFull() throws DAOException {
        List<Treno> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = conn.prepareStatement(SQL_FULL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Treno(
                        rs.getString("matricola"),
                        rs.getString("marca"),
                        rs.getString("modello"),
                        rs.getDate("data_acquisto").toLocalDate(),
                        rs.getTime("orario_partenza").toLocalTime(),
                        rs.getTime("orario_arrivo").toLocalTime(),
                        rs.getString("dep_nome_stazione"),
                        rs.getString("arr_nome_stazione")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura completo treni: " + e.getMessage(), e);
        }
        return result;
    }

    public void insert(Treno treno) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = conn.prepareStatement(INS)) {
            ps.setString(1, treno.getMatricola());
            ps.setString(2, treno.getMarca());
            ps.setString(3, treno.getModello());
            ps.setDate(4, Date.valueOf(treno.getDataAcquisto()));
            ps.setTime(5, Time.valueOf(treno.getOrarioPartenza()));
            ps.setTime(6, Time.valueOf(treno.getOrarioArrivo()));
            ps.setString(7, treno.getDepNomeStaz());
            ps.setString(8, treno.getArrNomeStaz());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento treno: " + e.getMessage(), e);
        }
    }

    public void update(Treno treno) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = conn.prepareStatement(UPD)) {
            ps.setString(1, treno.getMarca());
            ps.setString(2, treno.getModello());
            ps.setDate(3, Date.valueOf(treno.getDataAcquisto()));
            ps.setTime(4, Time.valueOf(treno.getOrarioPartenza()));
            ps.setTime(5, Time.valueOf(treno.getOrarioArrivo()));
            ps.setString(6, treno.getDepNomeStaz());
            ps.setString(7, treno.getArrNomeStaz());
            ps.setString(8, treno.getMatricola());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento treno: " + e.getMessage(), e);
        }
    }

    public void delete(String matricola) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = conn.prepareStatement(DEL)) {
            ps.setString(1, matricola);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore eliminazione treno: " + e.getMessage(), e);
        }
    }

    public List<Integer> vagoni(String matricola, String nomeClasse) throws DAOException {
        List<Integer> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS_PERSONALE);
             PreparedStatement ps = conn.prepareStatement(SEL_VAGONI)) {
            ps.setString(1, matricola);
            ps.setString(2, nomeClasse);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(rs.getInt("n_carrozza"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura vagoni: " + e.getMessage(), e);
        }
        return out;
    }
}
