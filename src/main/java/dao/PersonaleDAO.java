package dao;

import domain.Personale;
import domain.Personale.TipoPersonale;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;

public final class PersonaleDAO {

    private static final String PROPS_LOGIN   = "loginuser.properties";   // per letture (findCfByUsername)
    private static final String PROPS_GESTORE = "gestoreuser.properties"; // per promozione (SP eseguita dal gestore)

    private static final String SP_PROMUOVI =
            "{CALL sp_promuovi_cliente_a_personale(?,?,?,?,?)}";

    private static final String SQL_CF_BY_USER =
            "SELECT cf FROM personale WHERE username = ?";

    private PersonaleDAO() { }

    /** PROMOZIONE: da cliente a personale (eseguita dal Gestore).
     *  Aggiorna credentials.role=PERSONALE e inserisce in tabella 'personale'. */
    public static Personale promuoviClienteAPersonale(String cf,
                                                      TipoPersonale tipo,
                                                      String idTreno,
                                                      String marca,
                                                      String modello) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_GESTORE);
             CallableStatement cs = conn.prepareCall(SP_PROMUOVI)) {

            cs.setString(1, cf);
            cs.setString(2, tipo.name());   // 'MACCHINISTA' | 'CAPOTRENO'
            cs.setString(3, idTreno);
            cs.setString(4, marca);
            cs.setString(5, modello);

            boolean hasRs = cs.execute();

            String username = null;
            if (hasRs) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    }
                }
            }
            if (username == null) username = "(promosso)";

            return new Personale(cf, tipo, username, idTreno, marca, modello);

        } catch (SQLException e) {
            throw new DAOException("Errore promozione cliente a personale: " + e.getMessage(), e);
        }
    }

    /** Ricava il CF del personale dallo username (usato lato PersonaleView). */
    public static String findCfByUsername(String username) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS_LOGIN);
             PreparedStatement ps = conn.prepareStatement(SQL_CF_BY_USER)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lookup CF personale per username: " + e.getMessage(), e);
        }
    }
}
