package dao;

import domain.Tariffa;
import domain.Tratta;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TariffaDAO {

    private static final String PROPS = "clienteuser.properties";

    // NB: nessuna citta/provincia in tariffa secondo lo schema
    private static final String SEL_BY_TRATTA_CLASSE =
            "SELECT marca, modello, nome_classe, " +
                    "       dep_nome_stazione, arr_nome_stazione, prezzo " +
                    "FROM tariffa " +
                    "WHERE dep_nome_stazione = ? " +
                    "  AND arr_nome_stazione = ? " +
                    "  AND nome_classe      = ? " +
                    "ORDER BY prezzo";

    /**
     * Restituisce le tariffe per la tratta (dep/arr) e classe (PRIMA/SECONDA).
     * Accetta anche input '1A'/'2A' e li normalizza a PRIMA/SECONDA.
     */
    public List<Tariffa> getByTrattaEClass(Tratta t, String classe) throws DAOException {
        if (t == null) {
            throw new IllegalArgumentException("Tratta nulla.");
        }
        if (classe == null || classe.isBlank()) {
            throw new IllegalArgumentException("Classe nulla/vuota.");
        }

        String nomeClasse = normalizeClasse(classe);

        List<Tariffa> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SEL_BY_TRATTA_CLASSE)) {

            // bind parametri tratta (solo nomi stazione, come da schema)
            ps.setString(1, t.getDepNomeStazione());
            ps.setString(2, t.getArrNomeStazione());
            // bind classe (ENUM PRIMA/SECONDA)
            ps.setString(3, nomeClasse);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // ATTENZIONE: lo schema tariffa non ha citta/provincia.
                    // Costruiamo Tariffa con i campi davvero presenti.
                    Tariffa tr = new Tariffa(
                            rs.getString("marca"),
                            rs.getString("modello"),
                            rs.getString("nome_classe"),
                            rs.getString("dep_nome_stazione"),
                            rs.getString("arr_nome_stazione"),
                            rs.getBigDecimal("prezzo")
                    );
                    out.add(tr);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura tariffe: " + e.getMessage(), e);
        }
        return out;
    }

    // Normalizza input utente 1A/2A -> PRIMA/SECONDA (o lascia PRIMA/SECONDA intatti)
    private static String normalizeClasse(String cl) {
        String c = cl.trim().toUpperCase();
        if ("1A".equals(c) || "PRIMA".equals(c)) return "PRIMA";
        if ("2A".equals(c) || "SECONDA".equals(c)) return "SECONDA";
        // fallback: lascia così (potrebbe già essere PRIMA/SECONDA)
        return c;
    }
}
