package controller;

import dao.PersonaleDao;
import dao.PersonaleDao.ReportSettimanale;
import domain.Locomotiva;
import domain.Carrozza;
import domain.Treno;
import domain.Personale;
import exception.DAOException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PersonaleController {
    private final PersonaleDao dao;

    public PersonaleController(String propertiesFile) {
        this.dao = new PersonaleDao(propertiesFile);
    }

    public List<Treno> elencoTreni() throws DAOException {
        try { return dao.listTreni(); }
        catch (SQLException e) { throw new DAOException("Errore lettura treni", e); }
    }

    public List<Carrozza> carrozzeTreno(String idTreno) throws DAOException {
        try { return dao.listCarrozzeByTreno(idTreno); }
        catch (SQLException e) { throw new DAOException("Errore lettura carrozze", e); }
    }

    public List<ReportSettimanale> reportSettimanale(String cf, LocalDate dataInizio) throws DAOException {
        try { return dao.reportSettimanale(cf, dataInizio); }
        catch (SQLException e) { throw new DAOException("Errore report settimanale", e); }
    }

    public List<Locomotiva> locomotiveTreno(String idTreno) throws DAOException {
        try { return dao.listLocomotiveByTreno(idTreno); }
        catch (SQLException e) { throw new DAOException("Errore lettura locomotive", e); }
    }

    public int appendManutenzioneLocomotiva(int idComponente, String idTreno, String testo) throws DAOException {
        try { return dao.appendManutenzioneLocomotiva(idComponente, idTreno, testo); }
        catch (SQLException e) { throw new DAOException("Errore aggiornamento manutenzione locomotiva", e); }
    }

    public int appendManutenzioneCarrozza(int idComponente, String idTreno, String classe, String testo) throws DAOException {
        try { return dao.appendManutenzioneCarrozza(idComponente, idTreno, classe, testo); }
        catch (SQLException e) { throw new DAOException("Errore aggiornamento manutenzione carrozza", e); }
    }

    public Personale dettaglioPersonale(String cf) throws DAOException {
        try { return dao.getPersonale(cf); }
        catch (SQLException e) { throw new DAOException("Errore lettura personale", e); }
    }
}
