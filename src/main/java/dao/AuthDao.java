package dao;

import domain.Cliente;
import domain.Credenziali;
import domain.Ruolo;
import utility.DBConnection;

import javax.sql.DataSource;
import java.sql.*;

public class AuthDao {
    private final DataSource ds;
    private final String props;

    public AuthDao(String propertiesFile) {
        this.ds = null;
        this.props = propertiesFile;
    }

    public static class EsitoLogin {
        public final String cf;
        public final Ruolo ruolo;
        public EsitoLogin(String cf, Ruolo ruolo) { this.cf = cf; this.ruolo = ruolo; }
        @Override public String toString() { return "EsitoLogin{cf='" + cf + "', ruolo=" + ruolo + '}'; }
    }

    private Connection getConn() throws SQLException {
        if (ds != null) return ds.getConnection();
        return DBConnection.getConnection(props);
    }

    public EsitoLogin login(String username, String password) throws SQLException {
        try (Connection c = getConn();
             CallableStatement cs = c.prepareCall("{CALL sp_login(?,?)}")) {
            cs.setString(1, username);
            cs.setString(2, password);
            try (ResultSet rs = cs.executeQuery()) {
                if (!rs.next()) return null;
                String cf = rs.getString("cf");
                String ruoloDb = rs.getString("ruolo");
                Ruolo ruolo = (ruoloDb != null ? Ruolo.valueOf(ruoloDb.toUpperCase()) : null);
                return new EsitoLogin(cf, ruolo);
            }
        }
    }

    public void registraPersonale(String cf, String nome, String cognome,
                                  String username, String password, String tipo) throws SQLException {
        try (Connection c = getConn();
             CallableStatement cs = c.prepareCall("{CALL sp_registra_personale(?,?,?,?,?,?)}")) {
            cs.setString(1, cf);
            cs.setString(2, nome);
            cs.setString(3, cognome);
            cs.setString(4, tipo.toUpperCase());
            cs.setString(5, username);
            cs.setString(6, password);
            cs.execute();
        }
    }

    public Cliente registraCliente(String cf, String nome, String cognome,
                                   String username, String password) throws SQLException {
        try (Connection c = getConn();
             CallableStatement cs = c.prepareCall("{CALL sp_registra_cliente(?,?,?,?,?)}")) {
            cs.setString(1, cf);
            cs.setString(2, nome);
            cs.setString(3, cognome);
            cs.setString(4, username);
            cs.setString(5, password);
            cs.execute();
            return new Cliente(cf, nome, cognome);
        }
    }

    public Credenziali getCredenzialiByUsername(String username) throws SQLException {
        String sql = "SELECT cf, username, password, ruolo FROM credenziali WHERE username=?";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Credenziali(
                        rs.getString("cf"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Ruolo.valueOf(rs.getString("ruolo").toUpperCase())
                );
            }
        }
    }

}
