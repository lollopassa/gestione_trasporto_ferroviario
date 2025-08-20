package dao;

import domain.Turno;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO {

    // Profili distinti
    private static final String PROPS_PERSONALE = "personaleuser.properties";
    private static final String PROPS_GESTORE   = "gestoreuser.properties";

    // --- Stored procedures (profilo PERSONALE) ---
    private static final String CALL_SP_SETTIMANALI = "{ call sp_turni_settimanali(?,?,?) }";
    private static final String CALL_SP_STORICO     = "{ call sp_storico_turni(?,?,?,?) }";

    // --- CRUD (profilo GESTORE) ---
    private static final String SEL_ALL =
            "SELECT nome, cognome, data_serv, ora_inizio, ora_fine, " +
                    "       matricola, marca, modello " +
                    "  FROM turno " +
                    " ORDER BY data_serv, ora_inizio";

    private static final String INS =
            "INSERT INTO turno(nome, cognome, data_serv, ora_inizio, ora_fine, " +
                    "                   matricola, marca, modello) " +
                    "VALUES (?,?,?,?,?,?,?,?)";

    private static final String DEL =
            "DELETE FROM turno " +
                    " WHERE nome=? AND cognome=? AND data_serv=? AND ora_inizio=? " +
                    "   AND matricola=? AND marca=? AND modello=?";

    // ============= METODI USATI DAL PERSONALE =============

    /** Turni della settimana di riferimento (usa sp_turni_settimanali). */
    public List<Turno> getTurniSettimanali(String nome, String cognome, LocalDate refDate)
            throws DAOException {
        List<Turno> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS_PERSONALE);
             CallableStatement cs = c.prepareCall(CALL_SP_SETTIMANALI)) {

            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setDate(3, Date.valueOf(refDate));

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new DAOException("Errore lettura turni settimanali: " + e.getMessage(), e);
        }
    }

    /** Storico turni su intervallo (usa sp_storico_turni). */
    public List<Turno> getStoricoTurni(String nome, String cognome,
                                       LocalDate from, LocalDate to)
            throws DAOException {
        List<Turno> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS_PERSONALE);
             CallableStatement cs = c.prepareCall(CALL_SP_STORICO)) {

            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setDate(3, Date.valueOf(from));
            cs.setDate(4, Date.valueOf(to));

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new DAOException("Errore lettura storico turni: " + e.getMessage(), e);
        }
    }

    // ============= METODI USATI DAL GESTORE (CRUD) =============

    public List<Turno> listAll() throws DAOException {
        List<Turno> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(SEL_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(mapRow(rs));
            }
            return out;

        } catch (SQLException e) {
            throw new DAOException("Errore lettura elenco turni: " + e.getMessage(), e);
        }
    }

    public void insert(Turno t) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(INS)) {

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCognome());
            ps.setDate(3, Date.valueOf(t.getDataServ()));
            ps.setTime(4, Time.valueOf(t.getOraInizio()));
            ps.setTime(5, Time.valueOf(t.getOraFine()));
            ps.setString(6, t.getMatricola());
            ps.setString(7, t.getMarca());
            ps.setString(8, t.getModello());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore inserimento turno: " + e.getMessage(), e);
        }
    }

    public void delete(Turno t) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(DEL)) {

            ps.setString(1, t.getNome());
            ps.setString(2, t.getCognome());
            ps.setDate(3, Date.valueOf(t.getDataServ()));
            ps.setTime(4, Time.valueOf(t.getOraInizio()));
            ps.setString(5, t.getMatricola());
            ps.setString(6, t.getMarca());
            ps.setString(7, t.getModello());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Errore eliminazione turno: " + e.getMessage(), e);
        }
    }

    /** Update semplice: elimina il vecchio record e inserisce il nuovo. */
    public void update(Turno oldT, Turno newT) throws DAOException {
        // opzionalmente: fare in transazione
        delete(oldT);
        insert(newT);
    }

    // ============= MAPPATURA COMUNE =============

    private Turno mapRow(ResultSet rs) throws SQLException {
        return new Turno(
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getDate("data_serv").toLocalDate(),
                rs.getTime("ora_inizio").toLocalTime(),
                rs.getTime("ora_fine").toLocalTime(),
                // NB: per sp_turni_settimanali assicurati che questi campi vengano selezionati!
                getStringSafe(rs, "matricola"),
                getStringSafe(rs, "marca"),
                getStringSafe(rs, "modello")
        );
    }

    private String getStringSafe(ResultSet rs, String col) {
        try { return rs.getString(col); }
        catch (SQLException ex) { return null; } // nel caso la SP non lo restituisca
    }
}
