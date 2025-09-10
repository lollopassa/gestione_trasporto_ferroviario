package dao;

import domain.Carrozza;
import domain.Fermata;
import domain.Prenotazione;
import domain.Posto;
import domain.Stazione;
import domain.Tratta;
import domain.Treno;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private final String props;

    public ClienteDao(String propertiesFile) { this.props = propertiesFile; }

    public static class SintesiPrenotazione {
        public final int idPrenotazione;
        public final Date dataViaggio;
        public final String idTreno;
        public final int idTratta;
        public final String partenza;
        public final String arrivo;
        public final String classe;
        public final int idComponente;
        public final int numeroPosto;
        public final String cfPrenotante;
        public final String cfPasseggero;
        public final String nomePasseggero;
        public final String cognomePasseggero;
        public final String ccLast4;

        public SintesiPrenotazione(int idPrenotazione, Date dataViaggio, String idTreno, int idTratta,
                                   String partenza, String arrivo, String classe, int idComponente, int numeroPosto,
                                   String cfPrenotante, String cfPasseggero, String nomePasseggero, String cognomePasseggero,
                                   String ccLast4) {
            this.idPrenotazione = idPrenotazione;
            this.dataViaggio = dataViaggio;
            this.idTreno = idTreno;
            this.idTratta = idTratta;
            this.partenza = partenza;
            this.arrivo = arrivo;
            this.classe = classe;
            this.idComponente = idComponente;
            this.numeroPosto = numeroPosto;
            this.cfPrenotante = cfPrenotante;
            this.cfPasseggero = cfPasseggero;
            this.nomePasseggero = nomePasseggero;
            this.cognomePasseggero = cognomePasseggero;
            this.ccLast4 = ccLast4;
        }
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

    private Fermata mapFermata(ResultSet rs) throws SQLException {
        Time arr = rs.getTime("orarioArrPrev");
        Time par = rs.getTime("orarioPartPrev");
        return new Fermata(
                rs.getInt("idTratta"),
                rs.getString("nome"),
                rs.getString("citta"),
                rs.getString("provincia"),
                rs.getInt("progressivo"),
                arr != null ? arr.toLocalTime() : null,
                par != null ? par.toLocalTime() : null
        );
    }

    private Stazione mapStazione(ResultSet rs) throws SQLException {
        return new Stazione(
                rs.getString("nome"),
                rs.getString("citta"),
                rs.getString("provincia")
        );
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

    private Prenotazione mapPrenotazione(ResultSet rs) throws SQLException {
        return new Prenotazione(
                rs.getInt("idPrenotazione"),
                rs.getString("cfPasseggero"),
                rs.getString("nomePasseggero"),
                rs.getString("cognomePasseggero"),
                rs.getDate("dataNascitaPasseggero").toLocalDate(),
                rs.getString("cc_last4"),
                rs.getDate("dataViaggio").toLocalDate(),
                rs.getString("cfPrenotante"),
                rs.getInt("idTratta"),
                rs.getInt("numeroPosto"),
                rs.getInt("idComponente"),
                rs.getString("idTreno")
        );
    }

    public List<Tratta> elencoTratte() throws SQLException {
        List<Tratta> out = new ArrayList<>();
        String sql = "SELECT * FROM tratta ORDER BY idTratta";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapTratta(rs));
        }
        return out;
    }

    public List<Stazione> elencoStazioni() throws SQLException {
        List<Stazione> out = new ArrayList<>();
        String sql = "SELECT nome,citta,provincia FROM stazione ORDER BY citta,nome";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapStazione(rs));
        }
        return out;
    }

    public List<Fermata> fermateTratta(int idTratta) throws SQLException {
        List<Fermata> out = new ArrayList<>();
        String sql = "SELECT * FROM fermata WHERE idTratta=? ORDER BY progressivo";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idTratta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapFermata(rs));
            }
        }
        return out;
    }

    public List<Treno> treniSuTratta(int idTratta) throws SQLException {
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

    public int countPostiInCarrozza(String idTreno, int idComponente) throws SQLException {
        String sql = "SELECT COUNT(*) AS n FROM posto WHERE idTreno=? AND idComponente=?";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idTreno);
            ps.setInt(2, idComponente);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt("n"); }
        }
    }

    public List<Integer> postiLiberi(String idTreno, int idComponente, LocalDate dataViaggio) throws SQLException {
        List<Integer> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_cerca_posti_liberi(?,?,?)}")) {
            cs.setString(1, idTreno);
            cs.setInt(2, idComponente);
            cs.setDate(3, Date.valueOf(dataViaggio));
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(rs.getInt("numeroPosto"));
            }
        }
        return out;
    }


    public List<Prenotazione> prenotazioniPerPrenotanteRaw(String cfPrenotante) throws SQLException {
        List<Prenotazione> out = new ArrayList<>();
        String sql =
                "SELECT * " +
                        "FROM prenotazione " +
                        "WHERE cfPrenotante=? " +
                        "ORDER BY dataViaggio, idPrenotazione";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cfPrenotante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapPrenotazione(rs));
            }
        }
        return out;
    }

    public List<SintesiPrenotazione> prenotazioniPerPrenotante(String cfPrenotante) throws SQLException {
        List<SintesiPrenotazione> out = new ArrayList<>();
        String sql = "SELECT * FROM v_prenotazioni WHERE cfPrenotante=? ORDER BY dataViaggio, idPrenotazione";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cfPrenotante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new SintesiPrenotazione(
                            rs.getInt("idPrenotazione"),
                            rs.getDate("dataViaggio"),
                            rs.getString("idTreno"),
                            rs.getInt("idTratta"),
                            rs.getString("partenza"),
                            rs.getString("arrivo"),
                            rs.getString("classe"),
                            rs.getInt("idComponente"),
                            rs.getInt("numeroPosto"),
                            rs.getString("cfPrenotante"),
                            rs.getString("cfPasseggero"),
                            rs.getString("nomePasseggero"),
                            rs.getString("cognomePasseggero"),
                            rs.getString("cc_last4")
                    ));
                }
            }
        }
        return out;
    }

    public void prenota(String cfPasseggero, String nomePasseggero, String cognomePasseggero, LocalDate dataNascita,
                        String ccFull, LocalDate dataViaggio, String cfPrenotante,
                        int idTratta, String idTreno, int idComponente, int numeroPosto) throws SQLException {
        try (Connection c = DBConnection.getConnection(props);
             CallableStatement cs = c.prepareCall("{CALL sp_prenota_biglietto(?,?,?,?,?,?,?,?,?,?,?)}")) {
            cs.setString(1,  cfPasseggero);
            cs.setString(2,  nomePasseggero);
            cs.setString(3,  cognomePasseggero);
            cs.setDate(4,    Date.valueOf(dataNascita));
            cs.setString(5,  ccFull);
            cs.setDate(6,    Date.valueOf(dataViaggio));
            cs.setString(7,  cfPrenotante);
            cs.setInt(8,     idTratta);
            cs.setString(9,  idTreno);
            cs.setInt(10,    idComponente);
            cs.setInt(11,    numeroPosto);
            cs.execute();
        }
    }

    public List<Posto> listPostiByCarrozza(String idTreno, int idComponente) throws SQLException {
        List<Posto> out = new ArrayList<>();
        String sql = "SELECT numeroPosto, idComponente, idTreno FROM posto WHERE idTreno=? AND idComponente=? ORDER BY numeroPosto";
        try (Connection c = DBConnection.getConnection(props);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idTreno);
            ps.setInt(2, idComponente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Posto(
                            rs.getInt("numeroPosto"),
                            rs.getInt("idComponente"),
                            rs.getString("idTreno")
                    ));
                }
            }
        }
        return out;
    }
}
