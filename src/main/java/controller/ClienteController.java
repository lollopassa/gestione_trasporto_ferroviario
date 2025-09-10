package controller;

import dao.ClienteDao;
import dao.ClienteDao.SintesiPrenotazione;
import domain.Carrozza;
import domain.Fermata;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteController {
    private final ClienteDao dao;

    public ClienteController(String propertiesFile) {
        this.dao = new ClienteDao(propertiesFile);
    }

    public List<Tratta> elencoTratte() throws DAOException {
        try { return dao.elencoTratte(); }
        catch (SQLException e) { throw new DAOException("Errore lettura tratte", e); }
    }

    public List<Fermata> fermateTratta(int idTratta) throws DAOException {
        try { return dao.fermateTratta(idTratta); }
        catch (SQLException e) { throw new DAOException("Errore lettura fermate", e); }
    }

    public List<Treno> treniSuTratta(int idTratta) throws DAOException {
        try { return dao.treniSuTratta(idTratta); }
        catch (SQLException e) { throw new DAOException("Errore lettura treni", e); }
    }

    public List<Carrozza> carrozzeDelTreno(String idTreno) throws DAOException {
        try { return dao.listCarrozzeByTreno(idTreno); }
        catch (SQLException e) { throw new DAOException("Errore lettura carrozze", e); }
    }

    public int postiInCarrozza(String idTreno, int idComponente) throws DAOException {
        try { return dao.countPostiInCarrozza(idTreno, idComponente); }
        catch (SQLException e) { throw new DAOException("Errore conteggio posti", e); }
    }

    public List<Integer> postiLiberi(String idTreno, int idComponente, LocalDate dataViaggio) throws DAOException {
        try { return dao.postiLiberi(idTreno, idComponente, dataViaggio); }
        catch (SQLException e) { throw new DAOException("Errore ricerca posti liberi", e); }
    }

    public void prenota(String cfPasseggero, String nomePasseggero, String cognomePasseggero, LocalDate dataNascita,
                        String ccFull, LocalDate dataViaggio, String cfPrenotante,
                        int idTratta, String idTreno, int idComponente, int numeroPosto) throws DAOException {
        try {
            dao.prenota(cfPasseggero, nomePasseggero, cognomePasseggero, dataNascita,
                    ccFull, dataViaggio, cfPrenotante, idTratta, idTreno, idComponente, numeroPosto);
        } catch (SQLException e) {
            throw new DAOException("Errore prenotazione", e);
        }
    }

    public List<SintesiPrenotazione> prenotazioniCliente(String cfPrenotante) throws DAOException {
        try { return dao.prenotazioniPerPrenotante(cfPrenotante); }
        catch (SQLException e) { throw new DAOException("Errore lettura prenotazioni", e); }
    }
}
