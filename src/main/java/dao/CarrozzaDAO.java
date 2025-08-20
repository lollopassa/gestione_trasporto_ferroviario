package dao;

import domain.Carrozza;
import exception.DAOException;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CarrozzaDAO {

    private static final String PROPS = "gestoreuser.properties";

    // 5 colonne: matricola, marca, modello, n_carrozza, nome_classe
    private static final String INS_CARROZZA =
            "INSERT INTO carrozza (" +
                    "  matricola, marca, modello, n_carrozza, nome_classe" +
                    ") VALUES (?,?,?,?,?)";

    public void inserisci(Carrozza c) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(INS_CARROZZA)) {

            ps.setString(1, c.getMatricola());
            ps.setString(2, c.getMarca());
            ps.setString(3, c.getModello());
            ps.setInt   (4, c.getNCarrozza());
            ps.setString(5, c.getNomeClasse());

            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new DAOException(
                    "Esiste già la carrozza " + c.getNCarrozza()
                            + " per il treno " + c.getMatricola(), dup
            );
        } catch (SQLException e) {
            throw new DAOException(
                    "Errore inserimento carrozza: " + e.getMessage(), e
            );
        }
    }
}