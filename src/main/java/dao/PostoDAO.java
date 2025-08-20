// src/main/java/dao/PostoDAO.java
package dao;

import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostoDAO {

    private static final String PROPS = "clienteuser.properties";

    private static final String POSTI_LIBERI =
            "SELECT p.n_posto " +
                    "FROM posto p " +
                    " JOIN carrozza c ON c.matricola = p.matricola " +
                    "  AND c.marca     = p.marca " +
                    "  AND c.modello   = p.modello " +
                    "  AND c.n_carrozza= p.n_carrozza " +
                    "LEFT JOIN prenotazione pr " +
                    "  ON pr.matricola   = p.matricola " +
                    " AND pr.marca       = p.marca " +
                    " AND pr.modello     = p.modello " +
                    " AND pr.n_carrozza  = p.n_carrozza " +
                    " AND pr.n_posto     = p.n_posto " +
                    " AND pr.data_viaggio= ? " +   // 1 = data
                    "WHERE p.matricola   = ? " +   // 2 = matricola
                    "  AND p.n_carrozza  = ? " +   // 3 = numero carrozza
                    "  AND c.nome_classe = ? " +   // 4 = classe
                    "  AND pr.n_posto IS NULL " +
                    "ORDER BY p.n_posto";

    public List<String> postiLiberi(String matricola,
                                    int nCarrozza,
                                    String nomeClasse,
                                    LocalDate data) throws DAOException {
        List<String> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(POSTI_LIBERI)) {

            ps.setDate   (1, Date.valueOf(data));
            ps.setString (2, matricola);
            ps.setInt    (3, nCarrozza);
            ps.setString (4, nomeClasse);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    out.add(rs.getString("n_posto"));
            }
            return out;

        } catch (SQLException e) {
            throw new DAOException("Errore lettura posti liberi: " + e.getMessage(), e);
        }
    }

}
