package controller;

import dao.*;
import domain.*;
import domain.Personale.TipoPersonale;
import exception.DAOException;
import utility.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class GestoreController {
    // DAO già esistenti nel tuo progetto
    private final TipoDAO tipoDAO = new TipoDAO();
    private final StazioneDAO stazioneDAO = new StazioneDAO();
    private final TrattaDAO trattaDAO = new TrattaDAO();
    private final TrenoDAO trenoDAO = new TrenoDAO();
    private final TurnoDAO turnoDAO = new TurnoDAO();

    // Per query “dirette” lato gestore
    private static final String PROPS = "gestoreuser.properties";

    /* =========================
       TIPI
       ========================= */
    public List<Tipo> listTipi() throws DAOException { return tipoDAO.listAll(); }
    public void addTipo(Tipo t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Tipo mancante.");
        tipoDAO.insert(t);
    }
    public void updateTipo(String oldMarca, String oldModello, Tipo nuovo) throws DAOException {
        if (nuovo == null) throw new IllegalArgumentException("Nuovi dati mancanti.");
        tipoDAO.update(oldMarca, oldModello, nuovo);
    }
    public void deleteTipo(String marca, String modello) throws DAOException {
        tipoDAO.delete(marca, modello);
    }

    /* =========================
       STAZIONI
       ========================= */
    public List<Stazione> listStazioni() throws DAOException { return stazioneDAO.listAll(); }
    public void addStazione(Stazione s) throws DAOException {
        if (s == null) throw new IllegalArgumentException("Stazione mancante.");
        stazioneDAO.insert(s);
    }
    public void updateStazione(String nomeVecchio, Stazione nuovo) throws DAOException {
        if (nomeVecchio == null || nomeVecchio.isBlank()) throw new IllegalArgumentException("Nome stazione mancante.");
        if (nuovo == null) throw new IllegalArgumentException("Dati stazione mancanti.");
        stazioneDAO.update(new Stazione(nomeVecchio, null, null), nuovo);
    }
    public void deleteStazione(String nome) throws DAOException {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome stazione mancante.");
        stazioneDAO.delete(new Stazione(nome, null, null));
    }

    /* =========================
       TRATTE
       ========================= */
    public List<Tratta> listTratte() throws DAOException { return trattaDAO.listAll(); }
    public void addTratta(Tratta t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Tratta mancante.");
        trattaDAO.insert(t);
    }
    public void updateTratta(Tratta oldT, Tratta newT) throws DAOException {
        if (oldT == null || newT == null) throw new IllegalArgumentException("Dati tratta mancanti.");
        trattaDAO.update(oldT, newT);
    }
    public void deleteTratta(Tratta t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Tratta mancante.");
        trattaDAO.delete(t);
    }

    /* =========================
       TRENI
       ========================= */
    public List<Treno> listTreni() throws DAOException { return trenoDAO.listAllFull(); }
    public void addTreno(Treno t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Treno mancante.");
        trenoDAO.insert(t);
    }
    public void updateTreno(Treno t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Treno mancante.");
        trenoDAO.update(t);
    }
    public void deleteTreno(String idTreno) throws DAOException {
        if (idTreno == null || idTreno.isBlank()) throw new IllegalArgumentException("idTreno mancante.");
        trenoDAO.delete(idTreno);
    }

    /* =========================
       TURNI (pianificazione mensile)
       ========================= */
    public List<Turno> listTurni() throws DAOException { return turnoDAO.listAll(); }
    public List<Turno> listTurniMese(String cf, YearMonth ym) throws DAOException {
        if (cf == null || cf.isBlank()) throw new IllegalArgumentException("CF mancante.");
        LocalDate from = ym.atDay(1), to = ym.atEndOfMonth();
        return turnoDAO.getStoricoTurni(cf, from, to);
    }
    public void addTurno(Turno t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Turno mancante.");
        turnoDAO.insert(t);
    }
    public void updateTurno(Turno oldT, Turno newT) throws DAOException {
        if (oldT == null || newT == null) throw new IllegalArgumentException("Dati turno mancanti.");
        turnoDAO.update(oldT, newT);
    }
    public void deleteTurno(Turno t) throws DAOException {
        if (t == null) throw new IllegalArgumentException("Turno mancante.");
        turnoDAO.delete(t);
    }

    /* =========================
       CARROZZE
       ========================= */
    public List<Carrozza> listCarrozze() throws DAOException {
        final String sql = "SELECT idTreno, marca, modello, n_carrozza, nome_classe FROM carrozza ORDER BY idTreno, n_carrozza";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Carrozza> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Carrozza(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5)
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore elenco carrozze: " + e.getMessage(), e);
        }
    }

    public void addCarrozza(Carrozza ca) throws DAOException {
        final String sql = "INSERT INTO carrozza(idTreno, marca, modello, n_carrozza, nome_classe) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ca.getIdTreno());
            ps.setString(2, ca.getMarca());
            ps.setString(3, ca.getModello());
            ps.setInt(4,  ca.getNCarrozza());
            ps.setString(5, ca.getNomeClasse());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento carrozza: " + e.getMessage(), e);
        }
    }

    public void updateCarrozzaClasse(String id, String ma, String mo, int n, String nuovaClasse) throws DAOException {
        final String sql = "UPDATE carrozza SET nome_classe=? WHERE idTreno=? AND marca=? AND modello=? AND n_carrozza=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuovaClasse);
            ps.setString(2, id);
            ps.setString(3, ma);
            ps.setString(4, mo);
            ps.setInt(5, n);
            if (ps.executeUpdate() == 0) {
                throw new DAOException("Carrozza non trovata.");
            }
        } catch (SQLException e) {
            throw new DAOException("Errore update carrozza: " + e.getMessage(), e);
        }
    }

    public void deleteCarrozza(String id, String ma, String mo, int n) throws DAOException {
        final String sql = "DELETE FROM carrozza WHERE idTreno=? AND marca=? AND modello=? AND n_carrozza=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, ma);
            ps.setString(3, mo);
            ps.setInt(4, n);
            if (ps.executeUpdate() == 0) {
                throw new DAOException("Carrozza non trovata.");
            }
        } catch (SQLException e) {
            throw new DAOException("Errore delete carrozza: " + e.getMessage(), e);
        }
    }

    /* =========================
       POSTI
       ========================= */
    public List<Posto> listPosti(String id, String ma, String mo, int nCarrozza) throws DAOException {
        final String sql = "SELECT idTreno, marca, modello, n_carrozza, n_posto FROM posto WHERE idTreno=? AND marca=? AND modello=? AND n_carrozza=? ORDER BY n_posto";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, ma);
            ps.setString(3, mo);
            ps.setInt(4, nCarrozza);
            try (ResultSet rs = ps.executeQuery()) {
                List<Posto> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new Posto(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5)
                    ));
                }
                return out;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore elenco posti: " + e.getMessage(), e);
        }
    }

    public void addPosto(Posto p) throws DAOException {
        final String sql = "INSERT INTO posto(idTreno, marca, modello, n_carrozza, n_posto) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getMatricola());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getModello());
            ps.setInt(4,  p.getNCarrozza());
            ps.setString(5, p.getNPosto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento posto: " + e.getMessage(), e);
        }
    }

    public void deletePosto(String id, String ma, String mo, int nCarrozza, String nPosto) throws DAOException {
        final String sql = "DELETE FROM posto WHERE idTreno=? AND marca=? AND modello=? AND n_carrozza=? AND n_posto=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, ma);
            ps.setString(3, mo);
            ps.setInt(4, nCarrozza);
            ps.setString(5, nPosto);
            if (ps.executeUpdate() == 0) {
                throw new DAOException("Posto non trovato.");
            }
        } catch (SQLException e) {
            throw new DAOException("Errore delete posto: " + e.getMessage(), e);
        }
    }

    /* =========================
       TARIFFE
       ========================= */
    public List<Tariffa> listTariffe() throws DAOException {
        final String sql = "SELECT marca, modello, nome_classe, dep_nome_stazione, arr_nome_stazione, prezzo FROM tariffa ORDER BY dep_nome_stazione, arr_nome_stazione, nome_classe";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Tariffa> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Tariffa(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getBigDecimal(6)
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore elenco tariffe: " + e.getMessage(), e);
        }
    }

    public void addTariffa(Tariffa t) throws DAOException {
        final String sql = "INSERT INTO tariffa(marca, modello, nome_classe, dep_nome_stazione, arr_nome_stazione, prezzo) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getMarca());
            ps.setString(2, t.getModello());
            ps.setString(3, t.getNomeClasse());
            ps.setString(4, t.getDepNomeStazione());
            ps.setString(5, t.getArrNomeStazione());
            ps.setBigDecimal(6, t.getPrezzo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento tariffa: " + e.getMessage(), e);
        }
    }

    public void updateTariffaPrezzo(String ma, String mo, String cl, String dep, String arr, double prezzo) throws DAOException {
        final String sql = "UPDATE tariffa SET prezzo=? WHERE marca=? AND modello=? AND nome_classe=? AND dep_nome_stazione=? AND arr_nome_stazione=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, java.math.BigDecimal.valueOf(prezzo));
            ps.setString(2, ma); ps.setString(3, mo); ps.setString(4, cl); ps.setString(5, dep); ps.setString(6, arr);
            if (ps.executeUpdate() == 0) throw new DAOException("Tariffa non trovata.");
        } catch (SQLException e) {
            throw new DAOException("Errore update tariffa: " + e.getMessage(), e);
        }
    }

    public void deleteTariffa(String ma, String mo, String cl, String dep, String arr) throws DAOException {
        final String sql = "DELETE FROM tariffa WHERE marca=? AND modello=? AND nome_classe=? AND dep_nome_stazione=? AND arr_nome_stazione=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ma); ps.setString(2, mo); ps.setString(3, cl); ps.setString(4, dep); ps.setString(5, arr);
            if (ps.executeUpdate() == 0) throw new DAOException("Tariffa non trovata.");
        } catch (SQLException e) {
            throw new DAOException("Errore delete tariffa: " + e.getMessage(), e);
        }
    }

    /* =========================
       PERSONALE (assegnazioni / promozione)
       ========================= */

    /** Elenco completo delle assegnazioni (cf, tipo, treno). */
    public List<Personale> listPersonale() throws DAOException {
        final String sql = "SELECT cf, username, tipo, idTreno, marca, modello FROM personale ORDER BY idTreno, tipo";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Personale> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Personale(
                        rs.getString("cf"),
                        TipoPersonale.valueOf(rs.getString("tipo")),
                        rs.getString("username"),
                        rs.getString("idTreno"),
                        rs.getString("marca"),
                        rs.getString("modello")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore elenco personale: " + e.getMessage(), e);
        }
    }

    /** Promuove un cliente a personale e lo assegna ad un treno (via SP). */
    public Personale promuoviClienteAPersonale(String cf,
                                               TipoPersonale tipo,
                                               String idTreno,
                                               String marca,
                                               String modello) throws DAOException {
        // La SP fa tutti i controlli (esistenza cliente/treno, ruolo già presente, ecc.)
        return PersonaleDAO.promuoviClienteAPersonale(cf, tipo, idTreno, marca, modello);
    }

    /** Cambio treno/tipo per un CF (rimuovo riga vecchia e ne inserisco una nuova dopo check). */
    public void cambiaAssegnazionePersonale(String cf,
                                            String oldId, String oldMa, String oldMo,
                                            TipoPersonale nuovoTipo,
                                            String newId, String newMa, String newMo) throws DAOException {
        if (existsPersonaleByTipo(newId, newMa, newMo, nuovoTipo)) {
            throw new DAOException("Esiste già un " + nuovoTipo + " sul treno " + newId + ".");
        }
        deleteAssegnazione(cf, oldId, oldMa, oldMo);
        final String sql = "INSERT INTO personale(cf, username, tipo, idTreno, marca, modello) " +
                "SELECT cf, username, ?, ?, ?, ? FROM personale WHERE cf=? LIMIT 1";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuovoTipo.name());
            ps.setString(2, newId);
            ps.setString(3, newMa);
            ps.setString(4, newMo);
            ps.setString(5, cf);
            int n = ps.executeUpdate();
            if (n == 0) throw new DAOException("Utente non trovato per clonare username.");
        } catch (SQLException e) {
            throw new DAOException("Errore cambio assegnazione: " + e.getMessage(), e);
        }
    }

    public void rimuoviAssegnazione(String cf, String id, String ma, String mo) throws DAOException {
        deleteAssegnazione(cf, id, ma, mo);
    }

    private void deleteAssegnazione(String cf, String id, String ma, String mo) throws DAOException {
        final String sql = "DELETE FROM personale WHERE cf=? AND idTreno=? AND marca=? AND modello=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cf); ps.setString(2, id); ps.setString(3, ma); ps.setString(4, mo);
            if (ps.executeUpdate() == 0) throw new DAOException("Assegnazione non trovata.");
        } catch (SQLException e) {
            throw new DAOException("Errore rimozione assegnazione: " + e.getMessage(), e);
        }
    }

    private boolean existsPersonaleByTipo(String id, String ma, String mo, TipoPersonale tipo) throws DAOException {
        final String sql = "SELECT COUNT(*) FROM personale WHERE idTreno=? AND marca=? AND modello=? AND tipo=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id); ps.setString(2, ma); ps.setString(3, mo); ps.setString(4, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore verifica personale: " + e.getMessage(), e);
        }
    }

    /* =========================
       COPERTURE (tabella copre)
       ========================= */
    public List<String> listCoperture() throws DAOException {
        final String sql = "SELECT dep_nome_stazione, arr_nome_stazione, idTreno, marca, modello FROM copre ORDER BY dep_nome_stazione, arr_nome_stazione, idTreno";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<String> out = new ArrayList<>();
            while (rs.next()) {
                out.add(String.format("%s → %s | Treno %s (%s %s)",
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            return out;
        } catch (SQLException e) {
            throw new DAOException("Errore elenco coperture: " + e.getMessage(), e);
        }
    }

    public void addCopertura(String dep, String arr, String id, String ma, String mo) throws DAOException {
        final String sql = "INSERT INTO copre(dep_nome_stazione, arr_nome_stazione, idTreno, marca, modello) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dep); ps.setString(2, arr);
            ps.setString(3, id); ps.setString(4, ma); ps.setString(5, mo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento copertura: " + e.getMessage(), e);
        }
    }

    public void deleteCopertura(String dep, String arr, String id, String ma, String mo) throws DAOException {
        final String sql = "DELETE FROM copre WHERE dep_nome_stazione=? AND arr_nome_stazione=? AND idTreno=? AND marca=? AND modello=?";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dep); ps.setString(2, arr); ps.setString(3, id); ps.setString(4, ma); ps.setString(5, mo);
            if (ps.executeUpdate() == 0) throw new DAOException("Copertura non trovata.");
        } catch (SQLException e) {
            throw new DAOException("Errore delete copertura: " + e.getMessage(), e);
        }
    }

    /* =========================
       REGISTRO MANUTENZIONE (solo lettura)
       ========================= */
    public List<String> listRegistro(String idTrenoOrNull) throws DAOException {
        final String base = "SELECT idTreno, marca, modello, data_evento, descrizione, cf FROM vw_registro_manutenzione WHERE 1=1 ";
        final String where = (idTrenoOrNull == null || idTrenoOrNull.isBlank()) ? "" : "AND idTreno = ? ";
        final String order = "ORDER BY idTreno, data_evento";
        try (Connection c = DBConnection.getConnection(PROPS);
             PreparedStatement ps = c.prepareStatement(base + where + order)) {
            if (where.length() > 0) ps.setString(1, idTrenoOrNull);
            try (ResultSet rs = ps.executeQuery()) {
                List<String> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(String.format("Treno %s (%s %s) | %s | %s | cf=%s",
                            rs.getString("idTreno"),
                            rs.getString("marca"),
                            rs.getString("modello"),
                            rs.getTimestamp("data_evento").toLocalDateTime(),
                            rs.getString("descrizione"),
                            rs.getString("cf")));
                }
                return out;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore lettura registro: " + e.getMessage(), e);
        }
    }
}