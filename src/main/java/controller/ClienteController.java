package controller;

import dao.ClienteDAO;
import dao.PostoDAO;
import dao.PrenotazioneDAO;
import dao.TrattaDAO;
import dao.TrenoDAO;
import domain.Prenotazione;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ClienteController {
    private final TrattaDAO trattaDAO = new TrattaDAO();
    private final TrenoDAO trenoDAO = new TrenoDAO();
    private final PostoDAO postoDAO = new PostoDAO();
    private final PrenotazioneDAO prenDAO = new PrenotazioneDAO();

    private final String cfAcquirente;

    /** Factory: risolve cf acquirente a partire dallo username loggato. */
    public static ClienteController forUser(String loggedUsername) throws DAOException {
        if (loggedUsername == null || loggedUsername.isBlank()) {
            throw new IllegalArgumentException("Username loggato mancante.");
        }
        String cf = ClienteDAO.findCfByUsername(loggedUsername);
        if (cf == null || cf.isBlank()) {
            throw new DAOException("Account non collegato a un CF cliente. Registra il cliente o collega l'username in tabella cliente.");
        }
        return new ClienteController(cf);
    }

    /** Costruzione esplicita con cf acquirente già risolto. */
    public ClienteController(String cfAcquirente) {
        this.cfAcquirente = Objects.requireNonNull(cfAcquirente, "CF acquirente mancante");
    }

    public String getCfAcquirente() { return cfAcquirente; }

    public List<Tratta> listTratte() throws DAOException {
        return trattaDAO.listAll();
    }

    public List<Treno> listTreniByTratta(Tratta tratta) throws DAOException {
        if (tratta == null) throw new IllegalArgumentException("Tratta mancante.");
        return trenoDAO.listByTratta(tratta);
    }

    /** Restituisce i numeri di carrozza per treno e classe (PRIMA/SECONDA). */
    public List<Integer> listVagoni(String idTreno, String nomeClasse) throws DAOException {
        if (idTreno == null || idTreno.isBlank()) throw new IllegalArgumentException("idTreno mancante.");
        if (nomeClasse == null || nomeClasse.isBlank()) throw new IllegalArgumentException("Classe mancante.");
        return trenoDAO.vagoni(idTreno, nomeClasse);
    }

    /** Restituisce i posti liberi per treno/carrozza/classe/data. */
    public List<String> listPostiLiberi(String idTreno, int nCarrozza, String nomeClasse, LocalDate data)
            throws DAOException {
        if (idTreno == null || idTreno.isBlank()) throw new IllegalArgumentException("idTreno mancante.");
        if (nCarrozza <= 0) throw new IllegalArgumentException("Numero carrozza non valido.");
        if (nomeClasse == null || nomeClasse.isBlank()) throw new IllegalArgumentException("Classe mancante.");
        if (data == null) throw new IllegalArgumentException("Data mancante.");
        return postoDAO.postiLiberi(idTreno, nCarrozza, nomeClasse, data);
    }

    public Prenotazione creaPrenotazione(Prenotazione richiesta) throws DAOException {
        if (richiesta == null) throw new IllegalArgumentException("Dati prenotazione mancanti.");
        // cf acquirente deve essere quello dell'utente loggato
        if (richiesta.getCfAcquirente() == null || richiesta.getCfAcquirente().isBlank()) {
            throw new IllegalArgumentException("CF acquirente non impostato nella richiesta.");
        }
        return prenDAO.inserisci(richiesta);
    }

    public List<Prenotazione> getPrenotazioniCliente(String cf) throws DAOException {
        if (cf == null || cf.isBlank()) throw new IllegalArgumentException("Codice fiscale mancante.");
        return prenDAO.byCliente(cf);
    }
}
