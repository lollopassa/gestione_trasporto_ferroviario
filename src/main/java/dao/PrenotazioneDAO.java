package dao;

import domain.Prenotazione;
import exception.DAOException;
import utility.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {
    private static final String PROPS = "clienteuser.properties";

    private static final String CALL_SP_NUOVA =
            "{ CALL sp_nuova_prenotazione(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";

    // Elenco biglietti per CF VIAGGIATORE, con classe e prezzo dinamico
    private static final String SQL_BY_CF_VIAGGIATORE =
            "SELECT p.codice_prenotazione, p.data_viaggio, p.idTreno, p.marca, p.modello, " +
                    "       p.n_carrozza, p.n_posto, p.dep_nome_stazione, p.arr_nome_stazione, " +
                    "       p.cf_viaggiatore, p.nome_viagg, p.cognome_viagg, p.data_nascita_viagg, " +
                    "       p.cf_acq, p.carta_credito, " +
                    "       ca.nome_classe, fn_prezzo_dinamico(p.dep_nome_stazione, p.arr_nome_stazione, ca.nome_classe) AS prezzo " +
                    "FROM prenotazione p " +
                    "JOIN carrozza ca ON p.idTreno=ca.idTreno AND p.marca=ca.marca AND p.modello=ca.modello AND p.n_carrozza=ca.n_carrozza " +
                    "WHERE p.cf_viaggiatore = ? " +
                    "ORDER BY p.data_viaggio DESC";

    public Prenotazione inserisci(Prenotazione r) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(CALL_SP_NUOVA)) {

            cs.setString(1,  r.getIdTreno());
            cs.setString(2,  r.getMarca());
            cs.setString(3,  r.getModello());
            cs.setInt(4,     r.getNCarrozza());
            cs.setString(5,  r.getNumeroPosto());
            cs.setString(6,  r.getDepNomeStazione());
            cs.setString(7,  r.getArrNomeStazione());
            cs.setDate(8,    Date.valueOf(r.getDataViaggio()));

            // Dati VIAGGIATORE
            cs.setString(9,  r.getCfViaggiatore());
            cs.setString(10, r.getNomeViaggiatore());
            cs.setString(11, r.getCognomeViaggiatore());
            cs.setDate(12,   Date.valueOf(r.getDataNascitaViaggiatore()));

            // Dati ACQUIRENTE
            cs.setString(13, r.getCfAcquirente());
            cs.setString(14, r.getCartaCredito());

            String nomeClasse = null;
            BigDecimal prezzo = null;

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        nomeClasse = rs.getString("nome_classe");
                        prezzo     = rs.getBigDecimal("prezzo_calcolato");
                    }
                }
            }

            return new Prenotazione.Builder()
                    .withDataViaggio(r.getDataViaggio())
                    .withIdTreno(r.getIdTreno())
                    .withMarca(r.getMarca())
                    .withModello(r.getModello())
                    .withNCarrozza(r.getNCarrozza())
                    .withNumeroPosto(r.getNumeroPosto())
                    .withDepNomeStazione(r.getDepNomeStazione())
                    .withArrNomeStazione(r.getArrNomeStazione())
                    .withCfViaggiatore(r.getCfViaggiatore())
                    .withNomeViaggiatore(r.getNomeViaggiatore())
                    .withCognomeViaggiatore(r.getCognomeViaggiatore())
                    .withDataNascitaViaggiatore(r.getDataNascitaViaggiatore())
                    .withCfAcquirente(r.getCfAcquirente())
                    .withCartaCredito(r.getCartaCredito())
                    .withNomeClasse(nomeClasse)
                    .withPrezzo(prezzo)
                    .build();

        } catch (SQLException e) {
            throw new DAOException("Errore inserimento prenotazione: " + e.getMessage(), e);
        }
    }

    public List<Prenotazione> byCliente(String cfViaggiatore) throws DAOException {
        List<Prenotazione> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(PROPS);
             PreparedStatement ps = conn.prepareStatement(SQL_BY_CF_VIAGGIATORE)) {

            ps.setString(1, cfViaggiatore.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prenotazione p = new Prenotazione.Builder()
                            .withCodicePrenotazione(rs.getString("codice_prenotazione"))
                            .withDataViaggio(rs.getDate("data_viaggio").toLocalDate())
                            .withIdTreno(rs.getString("idTreno"))
                            .withMarca(rs.getString("marca"))
                            .withModello(rs.getString("modello"))
                            .withNCarrozza(rs.getInt("n_carrozza"))
                            .withNumeroPosto(rs.getString("n_posto"))
                            .withDepNomeStazione(rs.getString("dep_nome_stazione"))
                            .withArrNomeStazione(rs.getString("arr_nome_stazione"))
                            .withCfViaggiatore(rs.getString("cf_viaggiatore"))
                            .withNomeViaggiatore(rs.getString("nome_viagg"))
                            .withCognomeViaggiatore(rs.getString("cognome_viagg"))
                            .withDataNascitaViaggiatore(rs.getDate("data_nascita_viagg").toLocalDate())
                            .withCfAcquirente(rs.getString("cf_acq"))
                            .withCartaCredito(rs.getString("carta_credito"))
                            .withNomeClasse(rs.getString("nome_classe"))
                            .withPrezzo(rs.getBigDecimal("prezzo"))
                            .build();
                    out.add(p);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore recupero prenotazioni: " + e.getMessage(), e);
        }
        return out;
    }
}
