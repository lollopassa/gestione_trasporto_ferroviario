package dao;

import domain.Tratta;
import domain.Treno;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class GestoreDao {
    private final String props;

    public GestoreDao(String propertiesFile) { this.props = propertiesFile; }

    public static class PersonaleLight {
        public final String cf, nome, cognome, tipo;
        public PersonaleLight(String cf, String nome, String cognome, String tipo) {
            this.cf = cf; this.nome = nome; this.cognome = cognome; this.tipo = tipo;
        }
    }

    public static class EsitoMese {
        public final List<LocalDate> pianificati = new ArrayList<>();
        public final List<LocalDate> conflitti   = new ArrayList<>();
        public final List<LocalDate> errori      = new ArrayList<>();
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
    private Tratta mapTratta(ResultSet rs) throws SQLException {
        return new Tratta(
                rs.getInt("idTratta"),
                rs.getInt("numTreniOperativi"),
                rs.getString("nomePart"),
                rs.getString("cittaPart"),
                rs.getString("provPart"),
                rs.getString("nomeArr"),
                rs.getString("cittaArr"),
                rs.getString("provArr"),
                rs.getTime("orarioPartenza").toLocalTime(),
                rs.getTime("orarioArrivo").toLocalTime()
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

    public List<PersonaleLight> listPersonaleByTipo(String tipoOrNull) throws SQLException {
        List<PersonaleLight> out = new ArrayList<>();
        String base = "SELECT cf,nome,cognome,tipo FROM personale";
        String sql = (tipoOrNull==null) ? base + " ORDER BY cognome,nome"
                : base + " WHERE tipo=? ORDER BY cognome,nome";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (tipoOrNull!=null) ps.setString(1, tipoOrNull);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new PersonaleLight(
                        rs.getString("cf"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("tipo")
                ));
            }
        }
        return out;
    }

    public List<Tratta> listTratte() throws SQLException {
        List<Tratta> out = new ArrayList<>();
        String sql = "SELECT * FROM tratta ORDER BY idTratta";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapTratta(rs));
        }
        return out;
    }

    public List<Treno> listTreniByTratta(int idTratta) throws SQLException {
        List<Treno> out = new ArrayList<>();
        String sql =
                "SELECT t.matricola, t.idTratta, " +
                        "       tr.nomePart, tr.cittaPart, tr.provPart, " +
                        "       tr.nomeArr,  tr.cittaArr,  tr.provArr " +
                        "FROM treno t " +
                        "JOIN tratta tr ON tr.idTratta = t.idTratta " +
                        "WHERE t.idTratta=? " +
                        "ORDER BY t.matricola";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idTratta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapTreno(rs));
            }
        }
        return out;
    }

    public EsitoMese programmaMeseTreno(String idTreno, YearMonth mese,
                                        String cfMacchinista, java.time.LocalTime mInizio, java.time.LocalTime mFine,
                                        String cfCapotreno, java.time.LocalTime cInizio, java.time.LocalTime cFine) throws SQLException {
        EsitoMese esito = new EsitoMese();
        try (Connection conn = DBConnection.getConnection(props)) {
            conn.setAutoCommit(true);
            for (int day = 1; day <= mese.lengthOfMonth(); day++) {
                LocalDate data = mese.atDay(day);
                boolean okMac = false, okCap = false;
                boolean conflitto = false;

                try (CallableStatement cs = conn.prepareCall("{CALL sp_crea_turno(?,?,?,?,?)}")) {
                    cs.setString(1, cfMacchinista);
                    cs.setDate(2, Date.valueOf(data));
                    cs.setString(3, idTreno);
                    cs.setTime(4, Time.valueOf(mInizio));
                    cs.setTime(5, Time.valueOf(mFine));
                    cs.execute();
                    okMac = true;
                } catch (SQLException e) {
                    if (isConflittoTurno(e)) conflitto = true; else esito.errori.add(data);
                }

                try (CallableStatement cs = conn.prepareCall("{CALL sp_crea_turno(?,?,?,?,?)}")) {
                    cs.setString(1, cfCapotreno);
                    cs.setDate(2, Date.valueOf(data));
                    cs.setString(3, idTreno);
                    cs.setTime(4, Time.valueOf(cInizio));
                    cs.setTime(5, Time.valueOf(cFine));
                    cs.execute();
                    okCap = true;
                } catch (SQLException e) {
                    if (isConflittoTurno(e)) conflitto = true; else esito.errori.add(data);
                }

                if (okMac && okCap) esito.pianificati.add(data);
                else if (conflitto) esito.conflitti.add(data);
            }
        }
        return esito;
    }

    private boolean isConflittoTurno(SQLException e) {
        String state = e.getSQLState();
        if ("45021".equals(state)) return true;
        String msg = e.getMessage();
        return msg != null && msg.toLowerCase().contains("già assegnato");
    }

    public void assegnaTrenoATratta(String idTreno, int idTratta) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_cambia_tratta_treno(?,?)}")) {
            cs.setString(1, idTreno);
            cs.setInt(2, idTratta);
            cs.execute();
        }
    }
    public void spostaTrenoSuTratta(String idTreno, int nuovaIdTratta) throws SQLException {
        assegnaTrenoATratta(idTreno, nuovaIdTratta);
    }
    public void aggiornaNumTreniOperativi(int idTratta, int nuovoValore) throws SQLException {
        String sql = "UPDATE tratta SET numTreniOperativi=? WHERE idTratta=?";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nuovoValore);
            ps.setInt(2, idTratta);
            ps.executeUpdate();
        }
    }

    public void creaTreno(String matricola, int idTratta) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_crea_treno(?,?)}")) {
            cs.setString(1, matricola);
            cs.setInt(2, idTratta);
            cs.execute();
        }
    }
    public void creaLocomotiva(int idComponente, String idTreno, String marca, String modello,
                               LocalDate dataAcquisto, String manutenzioneNullable) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_crea_locomotiva(?,?,?,?,?,?)}")) {
            cs.setInt(1, idComponente);
            cs.setString(2, idTreno);
            cs.setString(3, marca);
            cs.setString(4, modello);
            cs.setDate(5, Date.valueOf(dataAcquisto));
            if (manutenzioneNullable == null) cs.setNull(6, Types.LONGVARCHAR);
            else cs.setString(6, manutenzioneNullable);
            cs.execute();
        }
    }
    public void creaCarrozzaConPosti(int idComponente, String idTreno, String classe, int numero,
                                     String marca, String modello, LocalDate dataAcquisto, int numPosti) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_crea_carrozza_con_posti(?,?,?,?,?,?,?,?)}")) {
            cs.setInt(1, idComponente);
            cs.setString(2, idTreno);
            cs.setString(3, classe.toUpperCase());
            cs.setInt(4, numero);
            cs.setString(5, marca);
            cs.setString(6, modello);
            cs.setDate(7, Date.valueOf(dataAcquisto));
            cs.setInt(8, numPosti);
            cs.execute();
        }
    }

    public List<Treno> listTreniSenzaLocomotiva() throws SQLException {
        List<Treno> out = new ArrayList<>();
        String sql =
                "SELECT t.matricola, t.idTratta, tr.nomePart, tr.cittaPart, tr.provPart, " +
                        "       tr.nomeArr,  tr.cittaArr,  tr.provArr " +
                        "FROM treno t " +
                        "JOIN tratta tr ON tr.idTratta = t.idTratta " +
                        "LEFT JOIN locomotiva l ON l.idTreno = t.matricola " +
                        "WHERE l.idTreno IS NULL " +
                        "ORDER BY t.matricola";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapTreno(rs));
        }
        return out;
    }

    public void aggiungiFermata(int idTratta, String nome, String citta, String prov,
                                int progressivo, LocalTime orarioArr, LocalTime orarioPart) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_aggiungi_fermata(?,?,?,?,?,?,?)}")) {
            cs.setInt(1, idTratta);
            cs.setString(2, nome);
            cs.setString(3, citta);
            cs.setString(4, prov);
            cs.setInt(5, progressivo);
            cs.setTime(6, Time.valueOf(orarioArr));
            cs.setTime(7, Time.valueOf(orarioPart));
            cs.execute();
        }
    }

    public void creaStazione(String nome, String citta, String prov) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_crea_stazione(?,?,?)}")) {
            cs.setString(1, nome);
            cs.setString(2, citta);
            cs.setString(3, prov);
            cs.execute();
        }
    }
}
