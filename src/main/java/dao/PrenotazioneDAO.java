package dao;

import domain.Prenotazione;
import exception.DAOException;
import utility.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {

    private static final String PROPS = "clienteuser.properties";

    private static final String CALL_SP_NUOVA = "{ call sp_nuova_prenotazione(?,?,?,?,?,?,?,?,?,?,?) }";

    private static final String SQL_BY_CF =
            "SELECT p.codice_prenotazione, p.data_viaggio, p.matricola, p.marca, p.modello, " +
                    "p.n_carrozza, p.n_posto, p.dep_nome_stazione, p.arr_nome_stazione, " +
                    "p.cf_acq, p.data_nascita_acq, p.carta_credito, " +
                    "ca.nome_classe, fn_prezzo_dinamico(p.dep_nome_stazione, p.arr_nome_stazione, ca.nome_classe) AS prezzo " +
                    "FROM prenotazione p " +
                    "JOIN carrozza ca ON p.matricola = ca.matricola AND p.marca = ca.marca AND p.modello = ca.modello AND p.n_carrozza = ca.n_carrozza " +
                    "WHERE p.cf_acq = ? " +
                    "ORDER BY p.data_viaggio DESC";


    public Prenotazione inserisci(Prenotazione richiesta) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(CALL_SP_NUOVA)) {
            // Set input parameters
            cs.setString(1, richiesta.getMatricola());
            cs.setString(2, richiesta.getMarca());
            cs.setString(3, richiesta.getModello());
            cs.setInt(4, richiesta.getNCarrozza());
            cs.setString(5, richiesta.getNumeroPosto());
            cs.setString(6, richiesta.getDepNomeStazione());
            cs.setString(7, richiesta.getArrNomeStazione());
            cs.setDate(8, Date.valueOf(richiesta.getDataViaggio()));
            cs.setString(9, richiesta.getCfAcquirente());
            cs.setDate(10, Date.valueOf(richiesta.getDataNascitaAcquirente()));
            cs.setString(11, richiesta.getCartaCredito());

            boolean hasResult = cs.execute();
            String nomeClasse = null;
            BigDecimal prezzo = null;
            if (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        nomeClasse = rs.getString("nome_classe");
                        prezzo     = rs.getBigDecimal("prezzo_calcolato");
                    }
                }
            }
            return new Prenotazione.Builder()
                    .withDataViaggio(richiesta.getDataViaggio())
                    .withMatricola(richiesta.getMatricola())
                    .withMarca(richiesta.getMarca())
                    .withModello(richiesta.getModello())
                    .withNCarrozza(richiesta.getNCarrozza())
                    .withNumeroPosto(richiesta.getNumeroPosto())
                    .withDepNomeStazione(richiesta.getDepNomeStazione())
                    .withArrNomeStazione(richiesta.getArrNomeStazione())
                    .withCfAcquirente(richiesta.getCfAcquirente())
                    .withDataNascitaAcquirente(richiesta.getDataNascitaAcquirente())
                    .withCartaCredito(richiesta.getCartaCredito())
                    .withNomeClasse(nomeClasse)
                    .withPrezzo(prezzo)
                    .build();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento prenotazione: " + e.getMessage(), e);
        }
    }

    public List<Prenotazione> byCliente(String cf) throws DAOException {
        List<Prenotazione> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SQL_BY_CF)) {
            ps.setString(1, cf);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prenotazione p = new Prenotazione.Builder()
                            .withCodicePrenotazione(rs.getString("codice_prenotazione"))
                            .withDataViaggio(rs.getDate("data_viaggio").toLocalDate())
                            .withMatricola(rs.getString("matricola"))
                            .withMarca(rs.getString("marca"))
                            .withModello(rs.getString("modello"))
                            .withNCarrozza(rs.getInt("n_carrozza"))
                            .withNumeroPosto(rs.getString("n_posto"))
                            .withDepNomeStazione(rs.getString("dep_nome_stazione"))
                            .withArrNomeStazione(rs.getString("arr_nome_stazione"))
                            .withCfAcquirente(rs.getString("cf_acq"))
                            .withDataNascitaAcquirente(rs.getDate("data_nascita_acq").toLocalDate())
                            .withCartaCredito(rs.getString("carta_credito"))
                            .withNomeClasse(rs.getString("nome_classe"))
                            .withPrezzo(rs.getBigDecimal("prezzo"))
                            .build();
                    result.add(p);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore recupero prenotazioni: " + e.getMessage(), e);
        }
        return result;
    }
}
