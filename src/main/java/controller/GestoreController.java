package controller;

import dao.GestoreDao;
import dao.GestoreDao.EsitoMese;
import dao.GestoreDao.PersonaleLight;
import domain.Stazione;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

public class GestoreController {
    private final GestoreDao dao;

    public GestoreController() { this("gestoreuser.properties"); }
    public GestoreController(String propertiesFile) { this.dao = new GestoreDao(propertiesFile); }

    public List<Treno> elencoTreni() throws DAOException {
        try { return dao.listTreni(); }
        catch (SQLException e) { throw new DAOException("Errore lettura treni", e); }
    }
    public List<PersonaleLight> personale(String tipoOrNull) throws DAOException {
        try { return dao.listPersonaleByTipo(tipoOrNull); }
        catch (SQLException e) { throw new DAOException("Errore lettura personale", e); }
    }
    public List<Tratta> elencoTratte() throws DAOException {
        try { return dao.listTratte(); }
        catch (SQLException e) { throw new DAOException("Errore lettura tratte", e); }
    }
    public List<Treno> treniByTratta(int idTratta) throws DAOException {
        try { return dao.listTreniByTratta(idTratta); }
        catch (SQLException e) { throw new DAOException("Errore lettura treni su tratta", e); }
    }
    public List<Stazione> elencoStazioni() throws DAOException {
        try { return dao.listStazioni(); }
        catch (SQLException e) { throw new DAOException("Errore lettura stazioni", e); }
    }

    public EsitoMese programmaMeseTreno(String idTreno, YearMonth mese,
                                        String cfMacchinista, LocalTime mInizio, LocalTime mFine,
                                        String cfCapotreno, LocalTime cInizio, LocalTime cFine) throws DAOException {
        try { return dao.programmaMeseTreno(idTreno, mese, cfMacchinista, mInizio, mFine, cfCapotreno, cInizio, cFine); }
        catch (SQLException e) { throw new DAOException("Errore programmazione mensile", e); }
    }

    public void assegnaTrenoATratta(String idTreno, int idTratta) throws DAOException {
        try { dao.assegnaTrenoATratta(idTreno, idTratta); }
        catch (SQLException e) { throw new DAOException("Errore assegnazione treno a tratta", e); }
    }
    public void spostaTrenoSuTratta(String idTreno, int nuovaIdTratta) throws DAOException {
        try { dao.spostaTrenoSuTratta(idTreno, nuovaIdTratta); }
        catch (SQLException e) { throw new DAOException("Errore spostamento treno su nuova tratta", e); }
    }
    public void aggiornaNumTreniOperativi(int idTratta, int nuovoValore) throws DAOException {
        try { dao.aggiornaNumTreniOperativi(idTratta, nuovoValore); }
        catch (SQLException e) { throw new DAOException("Errore aggiornamento num. treni operativi", e); }
    }

    public void creaTreno(String matricola, int idTratta) throws DAOException {
        try { dao.creaTreno(matricola, idTratta); }
        catch (SQLException e) { throw new DAOException("Errore creazione treno", e); }
    }
    public void creaLocomotiva(int idComponente, String idTreno, String marca, String modello,
                               LocalDate dataAcquisto, String manutenzioneNullable) throws DAOException {
        try { dao.creaLocomotiva(idComponente, idTreno, marca, modello, dataAcquisto, manutenzioneNullable); }
        catch (SQLException e) { throw new DAOException("Errore creazione locomotiva", e); }
    }
    public void creaCarrozzaConPosti(int idComponente, String idTreno, String classe, int numero,
                                     String marca, String modello, LocalDate dataAcquisto, int numPosti) throws DAOException {
        try { dao.creaCarrozzaConPosti(idComponente, idTreno, classe, numero, marca, modello, dataAcquisto, numPosti); }
        catch (SQLException e) { throw new DAOException("Errore creazione carrozza con posti", e); }
    }

    public void aggiungiFermata(int idTratta, String nome, String citta, String prov,
                                LocalTime orarioArr, LocalTime orarioPart, int progressivo) throws DAOException {
        try { dao.aggiungiFermata(idTratta, nome, citta, prov, progressivo, orarioArr, orarioPart); }
        catch (SQLException e) { throw new DAOException("Errore aggiunta fermata", e); }
    }

    public void creaStazione(String nome, String citta, String prov) throws DAOException {
        try { dao.creaStazione(nome, citta, prov); }
        catch (SQLException e) { throw new DAOException("Errore creazione stazione", e); }
    }

    public List<Treno> elencoTreniSenzaLocomotiva() throws DAOException {
        try { return dao.listTreniSenzaLocomotiva(); }
        catch (SQLException e) { throw new DAOException("Errore lettura treni senza locomotiva", e); }
    }

    public void creaTratta(int idTratta, int numTreniOperativi,
                           String nomePart, String cittaPart, String provPart,
                           String nomeArr, String cittaArr, String provArr,
                           LocalTime orarioPartenza, LocalTime orarioArrivo) throws DAOException {
        try {
            dao.creaTratta(idTratta, numTreniOperativi,
                    nomePart, cittaPart, provPart,
                    nomeArr, cittaArr, provArr,
                    orarioPartenza, orarioArrivo);
        } catch (SQLException e) {
            throw new DAOException("Errore creazione tratta", e);
        }
    }
}
