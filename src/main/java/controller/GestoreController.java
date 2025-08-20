package controller;

import dao.StazioneDAO;
import dao.TipoDAO;
import dao.TrattaDAO;
import dao.TrenoDAO;
import dao.TurnoDAO;
import domain.Stazione;
import domain.Tipo;
import domain.Tratta;
import domain.Treno;
import domain.Turno;
import exception.DAOException;
import java.util.List;

public class GestoreController {
    private final TipoDAO tipoDAO = new TipoDAO();
    private final StazioneDAO stazioneDAO = new StazioneDAO();
    private final TrattaDAO trattaDAO = new TrattaDAO();
    private final TrenoDAO trenoDAO = new TrenoDAO();
    private final TurnoDAO turnoDAO = new TurnoDAO();

    // Tipi
    public List<Tipo> listTipi() throws DAOException { return tipoDAO.listAll(); }
    public void addTipo(Tipo t) throws DAOException { if (t == null) throw new IllegalArgumentException("Tipo mancante."); tipoDAO.insert(t); }
    public void updateTipo(String oldMarca, String oldModello, Tipo nuovo) throws DAOException {
        if (nuovo == null) throw new IllegalArgumentException("Nuovi dati mancanti."); tipoDAO.update(oldMarca, oldModello, nuovo);
    }
    public void deleteTipo(String marca, String modello) throws DAOException { tipoDAO.delete(marca, modello); }

    // Stazioni
    public List<Stazione> listStazioni() throws DAOException { return stazioneDAO.listAll(); }
    public void addStazione(Stazione s) throws DAOException { if (s == null) throw new IllegalArgumentException("Stazione mancante."); stazioneDAO.insert(s); }
    public void updateStazione(String nomeVecchio, Stazione nuovo) throws DAOException {
        if (nomeVecchio == null || nomeVecchio.isBlank()) throw new IllegalArgumentException("Nome stazione mancante.");
        if (nuovo == null) throw new IllegalArgumentException("Dati stazione mancanti.");
        stazioneDAO.update(new Stazione(nomeVecchio, null, null), nuovo);
    }
    public void deleteStazione(String nome) throws DAOException {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome stazione mancante.");
        stazioneDAO.delete(new Stazione(nome, null, null));
    }

    // Tratte
    public List<Tratta> listTratte() throws DAOException { return trattaDAO.listAll(); }
    public void addTratta(Tratta t) throws DAOException { if (t == null) throw new IllegalArgumentException("Tratta mancante."); trattaDAO.insert(t); }
    public void updateTratta(Tratta oldT, Tratta newT) throws DAOException {
        if (oldT == null || newT == null) throw new IllegalArgumentException("Dati tratta mancanti.");
        trattaDAO.update(oldT, newT);
    }
    public void deleteTratta(Tratta t) throws DAOException { if (t == null) throw new IllegalArgumentException("Tratta mancante."); trattaDAO.delete(t); }

    // Treni
    public List<Treno> listTreni() throws DAOException { return trenoDAO.listAllFull(); }
    public void addTreno(Treno t) throws DAOException { if (t == null) throw new IllegalArgumentException("Treno mancante."); trenoDAO.insert(t); }
    public void updateTreno(Treno t) throws DAOException { if (t == null) throw new IllegalArgumentException("Treno mancante."); trenoDAO.update(t); }
    public void deleteTreno(String matricola) throws DAOException {
        if (matricola == null || matricola.isBlank()) throw new IllegalArgumentException("Matricola mancante.");
        trenoDAO.delete(matricola);
    }

    // Turni
    public List<Turno> listTurni() throws DAOException { return turnoDAO.listAll(); }
    public void addTurno(Turno t) throws DAOException { if (t == null) throw new IllegalArgumentException("Turno mancante."); turnoDAO.insert(t); }
    public void updateTurno(Turno oldT, Turno newT) throws DAOException {
        if (oldT == null || newT == null) throw new IllegalArgumentException("Dati turno mancanti.");
        turnoDAO.update(oldT, newT);
    }
    public void deleteTurno(Turno t) throws DAOException { if (t == null) throw new IllegalArgumentException("Turno mancante."); turnoDAO.delete(t); }
}
