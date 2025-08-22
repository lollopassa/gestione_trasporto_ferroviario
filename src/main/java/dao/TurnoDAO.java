package dao;

import domain.Turno;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO {

    // Letture via SP (profilo PERSONALE)
    private static final String PROPS_PERSONALE = "personaleuser.properties";
    // CRUD completo (profilo GESTORE)
    private static final String PROPS_GESTORE   = "gestoreuser.properties";

    // Stored procedures
    private static final String SP_SETT = "{CALL sp_turni_settimanali(?,?)}";
    private static final String SP_STOR = "{CALL sp_storico_turni(?,?,?)}";

    /* =========================
       LETTURE (report)
       ========================= */
    public List<Turno> getTurniSettimanali(String cf, LocalDate refDate) throws DAOException {
        List<Turno> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS_PERSONALE);
             CallableStatement cs = conn.prepareCall(SP_SETT)) {
            cs.setString(1, cf);
            cs.setDate(2, Date.valueOf(refDate));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(mapTurno(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura turni settimanali: " + e.getMessage(), e);
        }
        return out;
    }

    public List<Turno> getStoricoTurni(String cf, LocalDate from, LocalDate to) throws DAOException {
        List<Turno> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS_PERSONALE);
             CallableStatement cs = conn.prepareCall(SP_STOR)) {
            cs.setString(1, cf);
            cs.setDate(2, Date.valueOf(from));
            cs.setDate(3, Date.valueOf(to));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(mapTurno(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura storico turni: " + e.getMessage(), e);
        }
        return out;
    }

    public List<Turno> getPianificazioneMensile(String cf, YearMonth ym) throws DAOException {
        LocalDate from = ym.atDay(1);
        LocalDate to   = ym.atEndOfMonth();
        return getStoricoTurni(cf, from, to);
    }

    /* =========================
       CRUD (gestore)
       ========================= */
    public List<Turno> listAll() throws DAOException {
        final String sql =
                "SELECT cf, idTreno, marca, modello, data_serv, ora_inizio, ora_fine " +
                        "FROM turno " +
                        "ORDER BY data_serv, ora_inizio";
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Turno> out = new ArrayList<>();
            while (rs.next()) out.add(mapTurno(rs));
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore elenco turni: " + e.getMessage(), e);
        }
    }

    public void insert(Turno t) throws DAOException {
        final String sql =
                "INSERT INTO turno(cf, idTreno, marca, modello, data_serv, ora_inizio, ora_fine) " +
                        "VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getCf());
            ps.setString(2, t.getIdTreno());
            ps.setString(3, t.getMarca());
            ps.setString(4, t.getModello());
            ps.setDate(5, Date.valueOf(t.getDataServ()));   // <— corretto
            ps.setTime(6, Time.valueOf(t.getOraInizio()));
            ps.setTime(7, Time.valueOf(t.getOraFine()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento turno: " + e.getMessage(), e);
        }
    }

    /** Update come delete+insert (coerente con UNIQUE dello slot). */
    public void update(Turno oldT, Turno newT) throws DAOException {
        Connection c = null;
        try {
            c = DBConnection.getConnection(PROPS_GESTORE);
            c.setAutoCommit(false);

            int del = deleteInternal(c, oldT);
            if (del == 0) throw new DAOException("Turno da aggiornare non trovato.");

            final String ins =
                    "INSERT INTO turno(cf, idTreno, marca, modello, data_serv, ora_inizio, ora_fine) " +
                            "VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(ins)) {
                ps.setString(1, newT.getCf());
                ps.setString(2, newT.getIdTreno());
                ps.setString(3, newT.getMarca());
                ps.setString(4, newT.getModello());
                ps.setDate(5, Date.valueOf(newT.getDataServ())); // <— corretto
                ps.setTime(6, Time.valueOf(newT.getOraInizio()));
                ps.setTime(7, Time.valueOf(newT.getOraFine()));
                ps.executeUpdate();
            }

            c.commit();
        } catch (SQLException e) {
            try { if (c != null) c.rollback(); } catch (SQLException ignore) {}
            throw new DAOException("Errore update turno: " + e.getMessage(), e);
        } finally {
            try { if (c != null) c.setAutoCommit(true); } catch (SQLException ignore) {}
            try { if (c != null) c.close(); } catch (SQLException ignore) {}
        }
    }

    public void delete(Turno t) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE)) {
            int n = deleteInternal(c, t);
            if (n == 0) throw new DAOException("Turno non trovato.");
        } catch (SQLException e) {
            throw new DAOException("Errore delete turno: " + e.getMessage(), e);
        }
    }

    private int deleteInternal(Connection c, Turno t) throws SQLException {
        final String del =
                "DELETE FROM turno " +
                        "WHERE cf=? AND idTreno=? AND marca=? AND modello=? " +
                        "AND data_serv=? AND ora_inizio=? AND ora_fine=?";
        try (PreparedStatement ps = c.prepareStatement(del)) {
            ps.setString(1, t.getCf());
            ps.setString(2, t.getIdTreno());
            ps.setString(3, t.getMarca());
            ps.setString(4, t.getModello());
            ps.setDate(5, Date.valueOf(t.getDataServ())); // <— corretto
            ps.setTime(6, Time.valueOf(t.getOraInizio()));
            ps.setTime(7, Time.valueOf(t.getOraFine()));
            return ps.executeUpdate();
        }
    }

    /* =========================
       MAPPER
       ========================= */
    private Turno mapTurno(ResultSet rs) throws SQLException {
        return new Turno(
                rs.getString("cf"),
                rs.getDate("data_serv").toLocalDate(),
                rs.getTime("ora_inizio").toLocalTime(),
                rs.getTime("ora_fine").toLocalTime(),
                rs.getString("idTreno"),
                rs.getString("marca"),
                rs.getString("modello")
        );
    }
}
