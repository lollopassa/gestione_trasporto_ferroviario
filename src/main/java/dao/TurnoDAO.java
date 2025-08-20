package dao;

import domain.Turno;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO {

    private static final String PROPS_PERSONALE = "personaleuser.properties";
    private static final String PROPS_GESTORE   = "gestoreuser.properties";

    private static final String CALL_SP_SETTIMANALI = "{ call sp_turni_settimanali(?,?,?) }";
    private static final String CALL_SP_STORICO     = "{ call sp_storico_turni(?,?,?,?) }";

    private static final String SEL_ALL =
            "SELECT nome, cognome, data_serv, ora_inizio, ora_fine, matricola, marca, modello " +
                    "FROM turno ORDER BY data_serv, ora_inizio";
    private static final String INS =
            "INSERT INTO turno(nome, cognome, data_serv, ora_inizio, ora_fine, matricola, marca, modello) " +
                    "VALUES (?,?,?,?,?,?,?,?)";
    private static final String DEL =
            "DELETE FROM turno WHERE nome=? AND cognome=? AND data_serv=? AND ora_inizio=? " +
                    "AND matricola=? AND marca=? AND modello=?";

    public List<Turno> getTurniSettimanali(String nome, String cognome, LocalDate refDate) throws DAOException {
        List<Turno> result = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS_PERSONALE);
             CallableStatement cs = c.prepareCall(CALL_SP_SETTIMANALI)) {
            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setDate(3, Date.valueOf(refDate));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura turni settimanali: " + e.getMessage(), e);
        }
        return result;
    }

    public List<Turno> getStoricoTurni(String nome, String cognome, LocalDate from, LocalDate to) throws DAOException {
        List<Turno> result = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS_PERSONALE);
             CallableStatement cs = c.prepareCall(CALL_SP_STORICO)) {
            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setDate(3, Date.valueOf(from));
            cs.setDate(4, Date.valueOf(to));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura storico turni: " + e.getMessage(), e);
        }
        return result;
    }

    public List<Turno> listAll() throws DAOException {
        List<Turno> result = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(SEL_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura elenco turni: " + e.getMessage(), e);
        }
        return result;
    }

    public void insert(Turno turno) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(INS)) {
            ps.setString(1, turno.getNome());
            ps.setString(2, turno.getCognome());
            ps.setDate(3, Date.valueOf(turno.getDataServ()));
            ps.setTime(4, Time.valueOf(turno.getOraInizio()));
            ps.setTime(5, Time.valueOf(turno.getOraFine()));
            ps.setString(6, turno.getMatricola());
            ps.setString(7, turno.getMarca());
            ps.setString(8, turno.getModello());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento turno: " + e.getMessage(), e);
        }
    }

    public void delete(Turno turno) throws DAOException {
        try (Connection c = DBConnection.getConnection(PROPS_GESTORE);
             PreparedStatement ps = c.prepareStatement(DEL)) {
            ps.setString(1, turno.getNome());
            ps.setString(2, turno.getCognome());
            ps.setDate(3, Date.valueOf(turno.getDataServ()));
            ps.setTime(4, Time.valueOf(turno.getOraInizio()));
            ps.setString(5, turno.getMatricola());
            ps.setString(6, turno.getMarca());
            ps.setString(7, turno.getModello());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore eliminazione turno: " + e.getMessage(), e);
        }
    }

    public void update(Turno oldTurno, Turno newTurno) throws DAOException {
        delete(oldTurno);
        insert(newTurno);
    }

    private Turno mapRow(ResultSet rs) throws SQLException {
        String matricola = null;
        String marca     = null;
        String modello   = null;
        try { matricola = rs.getString("matricola"); } catch (SQLException ignored) {}
        try { marca     = rs.getString("marca"); }     catch (SQLException ignored) {}
        try { modello   = rs.getString("modello"); }   catch (SQLException ignored) {}
        return new Turno(
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getDate("data_serv").toLocalDate(),
                rs.getTime("ora_inizio").toLocalTime(),
                rs.getTime("ora_fine").toLocalTime(),
                matricola,
                marca,
                modello
        );
    }
}
