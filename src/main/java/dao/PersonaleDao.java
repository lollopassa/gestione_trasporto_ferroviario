package dao;

import domain.Carrozza;
import domain.Locomotiva;
import domain.Personale;
import domain.TipoPersonale;
import domain.Treno;
import domain.Turno;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonaleDao {
    private final String props;

    public PersonaleDao(String propertiesFile) { this.props = propertiesFile; }

    public static class ReportSettimanale {
        public final Date dataServizio;
        public final String idTreno;
        public final String tipo;
        public final Time oraInizio;
        public final Time oraFine;
        public final Time durata;
        public ReportSettimanale(Date dataServizio, String idTreno, String tipo,
                                 Time oraInizio, Time oraFine, Time durata) {
            this.dataServizio = dataServizio; this.idTreno = idTreno; this.tipo = tipo;
            this.oraInizio = oraInizio; this.oraFine = oraFine; this.durata = durata;
        }
    }

    public static class ComposizioneTreno {
        public final String idTreno;
        public final int idTratta;
        public final int numCarrozzePrima, numCarrozzeSeconda, numCarrozzeTotali;
        public final int postiPrimaTot, postiSecondaTot, postiTotali;
        public ComposizioneTreno(String idTreno, int idTratta,
                                 int numCarrozzePrima, int numCarrozzeSeconda, int numCarrozzeTotali,
                                 int postiPrimaTot, int postiSecondaTot, int postiTotali) {
            this.idTreno = idTreno; this.idTratta = idTratta;
            this.numCarrozzePrima = numCarrozzePrima; this.numCarrozzeSeconda = numCarrozzeSeconda;
            this.numCarrozzeTotali = numCarrozzeTotali;
            this.postiPrimaTot = postiPrimaTot; this.postiSecondaTot = postiSecondaTot; this.postiTotali = postiTotali;
        }
    }

    private Treno mapTreno(ResultSet rs) throws SQLException {
        return new Treno(
                rs.getString("matricola"),
                rs.getInt("idTratta"),
                rs.getString("nomePart"),
                rs.getString("cittaPart"),
                rs.getString("provPart"),
                rs.getString("nomeArr"),
                rs.getString("cittaArr"),
                rs.getString("provArr")
        );
    }

    private Carrozza mapCarrozza(ResultSet rs) throws SQLException {
        return new Carrozza(
                rs.getInt("idComponente"),
                rs.getString("idTreno"),
                rs.getString("classe"),
                rs.getInt("numero"),
                rs.getString("marca"),
                rs.getString("modello"),
                rs.getDate("dataAcquisto").toLocalDate(),
                rs.getString("manutenzione")
        );
    }

    private Locomotiva mapLocomotiva(ResultSet rs) throws SQLException {
        return new Locomotiva(
                rs.getInt("idComponente"),
                rs.getString("idTreno"),
                rs.getString("marca"),
                rs.getString("modello"),
                rs.getDate("dataAcquisto").toLocalDate(),
                rs.getString("manutenzione")
        );
    }

    private Turno mapTurno(String cf, ResultSet rs) throws SQLException {
        return new Turno(
                cf,
                rs.getDate("dataServizio").toLocalDate(),
                rs.getString("idTreno"),
                rs.getTime("oraInizio").toLocalTime(),
                rs.getTime("oraFine").toLocalTime()
        );
    }

    public List<Treno> listTreni() throws SQLException {
        List<Treno> out = new ArrayList<>();
        String sql =
                "SELECT t.matricola, t.idTratta, " +
                        "       tr.nomePart, tr.cittaPart, tr.provPart, " +
                        "       tr.nomeArr,  tr.cittaArr,  tr.provArr " +
                        "FROM treno t " +
                        "JOIN tratta tr ON tr.idTratta = t.idTratta " +
                        "ORDER BY t.matricola";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapTreno(rs));
        }
        return out;
    }

    public List<Carrozza> listCarrozzeByTreno(String idTreno) throws SQLException {
        List<Carrozza> out = new ArrayList<>();
        String sql = "SELECT * FROM carrozza WHERE idTreno=? ORDER BY numero";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idTreno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapCarrozza(rs));
            }
        }
        return out;
    }

    public List<Locomotiva> listLocomotiveByTreno(String idTreno) throws SQLException {
        List<Locomotiva> out = new ArrayList<>();
        String sql = "SELECT * FROM locomotiva WHERE idTreno=? ORDER BY idComponente";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idTreno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapLocomotiva(rs));
            }
        }
        return out;
    }

    public List<ReportSettimanale> reportSettimanale(String cf, LocalDate dataInizio) throws SQLException {
        List<ReportSettimanale> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_report_turni_settimanale(?,?)}")) {
            cs.setString(1, cf);
            cs.setDate(2, Date.valueOf(dataInizio));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    out.add(new ReportSettimanale(
                            rs.getDate("dataServizio"),
                            rs.getString("idTreno"),
                            rs.getString("tipo"),
                            rs.getTime("oraInizio"),
                            rs.getTime("oraFine"),
                            rs.getTime("durata")
                    ));
                }
            }
        }
        return out;
    }

    public List<Turno> reportSettimanaleTurni(String cf, LocalDate dataInizio) throws SQLException {
        List<Turno> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_report_turni_settimanale(?,?)}")) {
            cs.setString(1, cf);
            cs.setDate(2, Date.valueOf(dataInizio));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(mapTurno(cf, rs));
            }
        }
        return out;
    }

    public int appendManutenzioneLocomotiva(int idComponente, String idTreno, String testo) throws SQLException {
        String select = "SELECT manutenzione FROM locomotiva WHERE idComponente=? AND idTreno=?";
        String update = "UPDATE locomotiva SET manutenzione=? WHERE idComponente=? AND idTreno=?";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement sel = c.prepareStatement(select);
             PreparedStatement up = c.prepareStatement(update)) {

            sel.setInt(1, idComponente);
            sel.setString(2, idTreno);
            String existing = null;
            try (ResultSet rs = sel.executeQuery()) { if (rs.next()) existing = rs.getString(1); }
            String line = String.format("[%s] %s", Timestamp.valueOf(java.time.LocalDateTime.now()), testo);
            String nuovo = (existing == null || existing.isBlank()) ? line : existing + System.lineSeparator() + line;

            up.setString(1, nuovo);
            up.setInt(2, idComponente);
            up.setString(3, idTreno);
            return up.executeUpdate();
        }
    }

    public int appendManutenzioneCarrozza(int idComponente, String idTreno, String classe, String testo) throws SQLException {
        String select = "SELECT manutenzione FROM carrozza WHERE idComponente=? AND idTreno=? AND classe=?";
        String update = "UPDATE carrozza SET manutenzione=? WHERE idComponente=? AND idTreno=? AND classe=?";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement sel = c.prepareStatement(select);
             PreparedStatement up = c.prepareStatement(update)) {

            sel.setInt(1, idComponente);
            sel.setString(2, idTreno);
            sel.setString(3, classe);
            String existing = null;
            try (ResultSet rs = sel.executeQuery()) { if (rs.next()) existing = rs.getString(1); }
            String line = String.format("[%s] %s", Timestamp.valueOf(java.time.LocalDateTime.now()), testo);
            String nuovo = (existing == null || existing.isBlank()) ? line : existing + System.lineSeparator() + line;

            up.setString(1, nuovo);
            up.setInt(2, idComponente);
            up.setString(3, idTreno);
            up.setString(4, classe);
            return up.executeUpdate();
        }
    }

    public Personale getPersonale(String cf) throws SQLException {
        String sql = "SELECT cf,nome,cognome,tipo FROM personale WHERE cf=?";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cf);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Personale(
                        rs.getString("cf"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("tipo")
                );
            }
        }
    }

    public List<Personale> listPersonaleByTipo(TipoPersonale tipo) throws SQLException {
        String sql = "SELECT cf,nome,cognome,tipo FROM personale WHERE tipo=? ORDER BY cognome,nome";
        List<Personale> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Personale(
                            rs.getString("cf"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("tipo")
                    ));
                }
            }
        }
        return out;
    }
}
