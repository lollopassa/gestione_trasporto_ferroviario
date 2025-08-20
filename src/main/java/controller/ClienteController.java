package controller;

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

public class ClienteController {
    private final TrattaDAO trattaDAO = new TrattaDAO();
    private final TrenoDAO trenoDAO = new TrenoDAO();
    private final PostoDAO postoDAO = new PostoDAO();
    private final PrenotazioneDAO prenDAO = new PrenotazioneDAO();

    public List<Tratta> listTratte() throws DAOException { return trattaDAO.listAll(); }
    public List<Treno> listTreniByTratta(Tratta tratta) throws DAOException {
        if (tratta == null) throw new IllegalArgumentException("Tratta mancante.");
        return trenoDAO.listByTratta(tratta);
    }
    public List<Integer> listVagoni(String matricola, String nomeClasse) throws DAOException {
        if (matricola == null || matricola.isBlank()) throw new IllegalArgumentException("Matricola mancante.");
        if (nomeClasse == null || nomeClasse.isBlank()) throw new IllegalArgumentException("Classe mancante.");
        return trenoDAO.vagoni(matricola, nomeClasse);
    }
    public List<String> listPostiLiberi(String matricola, int nCarrozza, String nomeClasse, LocalDate data) throws DAOException {
        if (matricola == null || matricola.isBlank()) throw new IllegalArgumentException("Matricola mancante.");
        if (nCarrozza <= 0) throw new IllegalArgumentException("Numero carrozza non valido.");
        if (nomeClasse == null || nomeClasse.isBlank()) throw new IllegalArgumentException("Classe mancante.");
        if (data == null) throw new IllegalArgumentException("Data mancante.");
        return postoDAO.postiLiberi(matricola, nCarrozza, nomeClasse, data);
    }
    public Prenotazione creaPrenotazione(Prenotazione richiesta) throws DAOException {
        if (richiesta == null) throw new IllegalArgumentException("Dati prenotazione mancanti.");
        return prenDAO.inserisci(richiesta);
    }
    public List<Prenotazione> getPrenotazioniCliente(String cf) throws DAOException {
        if (cf == null || cf.isBlank()) throw new IllegalArgumentException("Codice fiscale mancante.");
        return prenDAO.byCliente(cf);
    }
}
