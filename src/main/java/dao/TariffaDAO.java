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

    private static final String SEL_BY_TRATTA_CLASSE =
            "SELECT marca, modello, nome_classe, " +
                    "       dep_nome_stazione, dep_citta, dep_provincia, " +
                    "       arr_nome_stazione, arr_citta, arr_provincia, prezzo " +
                    "FROM tariffa " +
                    "WHERE dep_nome_stazione = ? AND dep_citta = ? AND dep_provincia = ? " +
                    "  AND arr_nome_stazione = ? AND arr_citta = ? AND arr_provincia = ? " +
                    "  AND nome_classe      = ? " +
                    "ORDER BY prezzo";

    public List<Tariffa> getByTrattaEClass(Tratta t, String classe) throws DAOException {
        List<Tariffa> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SEL_BY_TRATTA_CLASSE)) {

            // bind parametri tratta
            ps.setString(1, t.getDepNomeStazione());
            ps.setString(2, t.getDepCitta());
            ps.setString(3, t.getDepProvincia());
            ps.setString(4, t.getArrNomeStazione());
            ps.setString(5, t.getArrCitta());
            ps.setString(6, t.getArrProvincia());
            // bind classe
            ps.setString(7, classe);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tariffa tr = new Tariffa(
                            rs.getString("marca"),
                            rs.getString("modello"),
                            rs.getString("nome_classe"),
                            rs.getString("dep_nome_stazione"),
                            rs.getString("dep_citta"),
                            rs.getString("dep_provincia"),
                            rs.getString("arr_nome_stazione"),
                            rs.getString("arr_citta"),
                            rs.getString("arr_provincia"),
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
}
