// src/main/java/dao/TrenoDAO.java
package dao;

import domain.Treno;
import domain.Tratta;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrenoDAO {

    // Profilo per il gestore (lettura-scrittura completa)
    private static final String PROPS = "gestoreuser.properties";

    // PER PERSONALE: elenco leggero
    private static final String SQL_ALL_LIGHT =
            "SELECT matricola, marca, modello " +
                    "  FROM treno " +
                    " ORDER BY matricola";

    // PER PERSONALE: treni su tratta
    private static final String SEL_BY_TRATTA =
            "SELECT matricola, marca, modello, data_acquisto, orario_partenza, orario_arrivo, " +
                    "       dep_nome_stazione, dep_citta, dep_provincia, " +
                    "       arr_nome_stazione, arr_citta, arr_provincia " +
                    "  FROM treno " +
                    " WHERE dep_nome_stazione=? AND dep_citta=? AND dep_provincia=? " +
                    "   AND arr_nome_stazione=? AND arr_citta=? AND arr_provincia=? " +
                    " ORDER BY matricola";

    // PER PERSONALE: vagoni di una classe
    private static final String SEL_VAGONI =
            "SELECT n_carrozza " +
                    "  FROM carrozza " +
                    " WHERE matricola=? AND nome_classe=? " +
                    " ORDER BY n_carrozza";

    // PER GESTORE: elenco completo di tutti i campi
    private static final String SQL_ALL_FULL =
            "SELECT matricola, marca, modello, " +
                    "       data_acquisto, orario_partenza, orario_arrivo, " +
                    "       dep_nome_stazione, dep_citta, dep_provincia, " +
                    "       arr_nome_stazione, arr_citta, arr_provincia " +
                    "  FROM treno " +
                    " ORDER BY matricola";

    // PER GESTORE: insert leggero (solo matricola, marca, modello)
    private static final String INS_LIGHT =
            "INSERT INTO treno(matricola, marca, modello, " +
                    "  data_acquisto, orario_partenza, orario_arrivo, " +
                    "  dep_nome_stazione, dep_citta, dep_provincia, " +
                    "  arr_nome_stazione, arr_citta, arr_provincia) " +
                    // qui inserisco placeholder dummy; in un caso reale chiederesti tutti i campi
                    "VALUES(?, ?, ?, CURDATE(), '00:00','00:00', '??','??','??','??','??','??')";

    // PER GESTORE: update leggero (solo marca/modello)
    private static final String UPD_LIGHT =
            "UPDATE treno " +
                    "   SET marca = ?, modello = ? " +
                    " WHERE matricola = ?";

    // PER GESTORE: delete per matricola
    private static final String DEL_BY_MATR =
            "DELETE FROM treno WHERE matricola = ?";

    public TrenoDAO() { /* niente da fare */ }

    //
    // ====== metodi per il personale ======
    //

    public List<Treno> getAllTreni() throws DAOException {
        List<Treno> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SQL_ALL_LIGHT);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Treno(
                        rs.getString("matricola"),
                        rs.getString("marca"),
                        rs.getString("modello")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura elenco treni: " + e.getMessage(), e);
        }
    }

    /** Restituisce tutti i convogli operativi sulla tratta specificata. */
    public List<Treno> listByTratta(Tratta t) throws DAOException {
        List<Treno> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(SEL_BY_TRATTA)) {

            ps.setString(1, t.getDepNomeStazione());
            ps.setString(2, t.getDepCitta());
            ps.setString(3, t.getDepProvincia());
            ps.setString(4, t.getArrNomeStazione());
            ps.setString(5, t.getArrCitta());
            ps.setString(6, t.getArrProvincia());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Treno(
                            rs.getString("matricola"),
                            rs.getString("marca"),
                            rs.getString("modello"),
                            rs.getDate("data_acquisto").toLocalDate(),
                            rs.getTime("orario_partenza").toLocalTime(),
                            rs.getTime("orario_arrivo").toLocalTime(),
                            rs.getString("dep_nome_stazione"),
                            rs.getString("dep_citta"),
                            rs.getString("dep_provincia"),
                            rs.getString("arr_nome_stazione"),
                            rs.getString("arr_citta"),
                            rs.getString("arr_provincia")
                    ));
                }
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura treni per tratta: " + e.getMessage(), e);
        }
    }

    /** Restituisce i numeri di carrozza per un dato treno e classe. */
    public List<Integer> vagoni(String matricola, String nomeClasse) throws DAOException {
        List<Integer> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SEL_VAGONI)) {

            ps.setString(1, matricola);
            ps.setString(2, nomeClasse);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(rs.getInt("n_carrozza"));
                }
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura vagoni: " + e.getMessage(), e);
        }
    }

    //
    // ====== metodi per il gestore ======
    //

    /** Restituisce tutti i treni, con tutti i campi. */
    public List<Treno> listAllFull() throws DAOException {
        List<Treno> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SQL_ALL_FULL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Treno(
                        rs.getString("matricola"),
                        rs.getString("marca"),
                        rs.getString("modello"),
                        rs.getDate("data_acquisto").toLocalDate(),
                        rs.getTime("orario_partenza").toLocalTime(),
                        rs.getTime("orario_arrivo").toLocalTime(),
                        rs.getString("dep_nome_stazione"),
                        rs.getString("dep_citta"),
                        rs.getString("dep_provincia"),
                        rs.getString("arr_nome_stazione"),
                        rs.getString("arr_citta"),
                        rs.getString("arr_provincia")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore lettura completo treni: " + e.getMessage(), e);
        }
    }

    /** Inserisce un nuovo treno (leggero). */
    public void insert(Treno t) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(INS_LIGHT)) {

            ps.setString(1, t.getMatricola());
            ps.setString(2, t.getMarca());
            ps.setString(3, t.getModello());
            // gli altri campi vanno compilati o parametrizzati a seconda del tuo schema
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento treno: " + e.getMessage(), e);
        }
    }

    /** Aggiorna solo marca e modello di un treno identificato da matricola. */
    public void update(Treno t) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(UPD_LIGHT)) {

            ps.setString(1, t.getMarca());
            ps.setString(2, t.getModello());
            ps.setString(3, t.getMatricola());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornamento treno: " + e.getMessage(), e);
        }
    }

    /** Elimina un treno identificato da matricola. */
    public void delete(String matricola) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(DEL_BY_MATR)) {

            ps.setString(1, matricola);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore eliminazione treno: " + e.getMessage(), e);
        }
    }
}
