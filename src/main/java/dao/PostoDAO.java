package dao;

import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostoDAO {

    private static final String PROPS = "clienteuser.properties";

    // Posti liberi per matricola + carrozza + classe + data
    private static final String SQL_POSTI_LIBERI =
            "SELECT po.n_posto " +
                    "FROM posto po " +
                    "JOIN carrozza ca " +
                    "  ON ca.matricola = po.matricola " +
                    " AND ca.marca     = po.marca " +
                    " AND ca.modello   = po.modello " +
                    " AND ca.n_carrozza= po.n_carrozza " +
                    "WHERE po.matricola = ? " +
                    "  AND po.n_carrozza = ? " +
                    "  AND ca.nome_classe = ? " +
                    "  AND NOT EXISTS ( " +
                    "      SELECT 1 FROM prenotazione pr " +
                    "       WHERE pr.matricola  = po.matricola " +
                    "         AND pr.marca      = po.marca " +
                    "         AND pr.modello    = po.modello " +
                    "         AND pr.n_carrozza = po.n_carrozza " +
                    "         AND pr.n_posto    = po.n_posto " +
                    "         AND pr.data_viaggio = ? " +
                    "  ) " +
                    "ORDER BY po.n_posto";

    public List<String> postiLiberi(String matricola, int nCarrozza, String nomeClasse, LocalDate data) throws DAOException {
        if (matricola == null || matricola.isBlank()) throw new IllegalArgumentException("Matricola mancante.");
        if (nCarrozza <= 0) throw new IllegalArgumentException("Numero carrozza non valido.");
        if (nomeClasse == null || nomeClasse.isBlank()) throw new IllegalArgumentException("Classe mancante.");
        if (data == null) throw new IllegalArgumentException("Data mancante.");

        String clazz = normalizeClasse(nomeClasse);

        List<String> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SQL_POSTI_LIBERI)) {

            ps.setString(1, matricola);
            ps.setInt(2, nCarrozza);
            ps.setString(3, clazz);
            ps.setDate(4, Date.valueOf(data));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString("n_posto"));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura posti liberi: " + e.getMessage(), e);
        }
        return out;
    }

    private static String normalizeClasse(String cl) {
        String c = cl.trim().toUpperCase();
        if ("1A".equals(c) || "PRIMA".equals(c))   return "PRIMA";
        if ("2A".equals(c) || "SECONDA".equals(c)) return "SECONDA";
        return c;
    }
}
