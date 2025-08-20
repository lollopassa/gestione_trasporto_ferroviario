package dao;

import domain.Prenotazione;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {

    private static final String PROPS = "clienteuser.properties";

    // 20 parametri, corrispondenti alla firma della sp_nuova_prenotazione
    private static final String CALL_SQL =
            "{ call sp_nuova_prenotazione(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";

    public void inserisci(Prenotazione p) throws DAOException {
        try (Connection conn = DBConnection.getConnection(PROPS);
             CallableStatement cs = conn.prepareCall(CALL_SQL)) {

            // 1–6: POSTO (matricola, marca, modello, classe, carrozza, posto)
            cs.setString(1, p.getMatricola());
            cs.setString(2, p.getMarca());
            cs.setString(3, p.getModello());
            cs.setString(4, p.getClasse());
            cs.setInt(   5, p.getNCarrozza());
            cs.setString(6, p.getNumeroPosto());

            // 7–12: TRATTA
            cs.setString(7,  p.getDepStazione());
            cs.setString(8,  p.getDepCitta());
            cs.setString(9,  p.getDepProvincia());
            cs.setString(10, p.getArrStazione());
            cs.setString(11, p.getArrCitta());
            cs.setString(12, p.getArrProvincia());

            // 13–15: PASSEGGERO (cliente)
            cs.setString(13, p.getNomeAcquirente());
            cs.setString(14, p.getCognomeAcquirente());
            cs.setDate(  15, Date.valueOf(p.getDataViaggio()));

            // 16–20: ACQUIRENTE
            cs.setString(16, p.getNomeAcquirente());
            cs.setString(17, p.getCognomeAcquirente());
            cs.setDate(  18, Date.valueOf(p.getDataNascitaAcquirente()));
            cs.setString(19, p.getCfAcquirente());
            cs.setString(20, p.getCartaCredito());

            cs.execute();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento prenotazione: " + e.getMessage(), e);
        }
    }

    private static final String SEL_BY_CLIENTE =
            "SELECT * FROM prenotazione " +
                    " WHERE nome_acq = ? AND cognome_acq = ? " +
                    " ORDER BY data_viaggio DESC";

    public List<Prenotazione> byCliente(String nome, String cognome) throws DAOException {
        List<Prenotazione> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(SEL_BY_CLIENTE)) {

            ps.setString(1, nome);
            ps.setString(2, cognome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Prenotazione.Builder()
                            .withMatricola(rs.getString("matricola"))
                            .withMarca(rs.getString("marca"))
                            .withModello(rs.getString("modello"))
                            .withClasse(rs.getString("nome_classe"))
                            .withNCarrozza(rs.getInt("n_carrozza"))
                            .withNumeroPosto(rs.getString("n_posto"))
                            .withDepStazione(rs.getString("dep_nome_stazione"))
                            .withDepCitta(rs.getString("dep_citta"))
                            .withDepProvincia(rs.getString("dep_provincia"))
                            .withArrStazione(rs.getString("arr_nome_stazione"))
                            .withArrCitta(rs.getString("arr_citta"))
                            .withArrProvincia(rs.getString("arr_provincia"))
                            .withDataViaggio(rs.getDate("data_viaggio").toLocalDate())
                            .withPrezzo(rs.getBigDecimal("prezzo"))
                            .withNomeAcquirente(rs.getString("nome_acq"))
                            .withCognomeAcquirente(rs.getString("cognome_acq"))
                            .withDataNascitaAcquirente(rs.getDate("data_nascita_acq").toLocalDate())
                            .withCfAcquirente(rs.getString("cf_acq"))
                            .withCartaCredito(rs.getString("carta_credito"))
                            .build()
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore recupero prenotazioni: " + e.getMessage(), e);
        }
        return out;
    }
}
