package controller;

import dao.PersonaleDAO;
import dao.RegistroDAO;
import dao.TurnoDAO;
import domain.Turno;
import exception.DAOException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class PersonaleController {
    private final TurnoDAO turnoDAO = new TurnoDAO();
    private final RegistroDAO registroDAO = new RegistroDAO();

    private final String cfPersonale;

    /** Factory: risolve il CF del personale a partire dallo username loggato. */
    public static PersonaleController forUser(String loggedUsername) throws DAOException {
        if (loggedUsername == null || loggedUsername.isBlank()) {
            throw new IllegalArgumentException("Username loggato mancante.");
        }
        String cf = PersonaleDAO.findCfByUsername(loggedUsername);
        if (cf == null || cf.isBlank()) {
            throw new DAOException("Account personale non collegato a un CF. Registra/collega lo username in tabella personale.");
        }
        return new PersonaleController(cf);
    }

    /** Costruttore esplicito con CF già risolto. */
    public PersonaleController(String cfPersonale) {
        this.cfPersonale = Objects.requireNonNull(cfPersonale, "CF personale mancante");
    }

    public String getCfPersonale() { return cfPersonale; }

    /* ========================
       Pianificazione MENSILE
       ======================== */

    /** Lista turni dell'intero mese (base mensile di pianificazione). */
    public List<Turno> getPianificazioneMensile(YearMonth ym) throws DAOException {
        if (ym == null) throw new IllegalArgumentException("Mese mancante.");
        LocalDate from = ym.atDay(1);
        LocalDate to   = ym.atEndOfMonth();
        return turnoDAO.getStoricoTurni(cfPersonale, from, to);
    }

    /** Rende una stampa leggibile della pianificazione mensile. */
    public String stampaPianificazioneMensile(YearMonth ym) throws DAOException {
        List<Turno> turni = getPianificazioneMensile(ym);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder()
                .append("\n== Pianificazione mensile (")
                .append(ym).append(") ==\n");

        if (turni.isEmpty()) {
            sb.append("Nessun turno nel mese.\n");
            return sb.toString();
        }

        for (Turno t : turni) {
            sb.append(String.format(
                    "%s | %s-%s | Treno %s (%s %s)",
                    df.format(t.getDataServ()),
                    t.getOraInizio(),
                    t.getOraFine(),
                    t.getIdTreno(),
                    t.getMarca(),
                    t.getModello()
            )).append('\n');
        }
        return sb.toString();
    }

    /* ========================
       Report SETTIMANALE
       ======================== */

    /** Turni nella settimana [refDate, refDate+6] per il personale loggato. */
    public List<Turno> getTurniSettimanali(LocalDate refDate) throws DAOException {
        if (refDate == null) throw new IllegalArgumentException("Data di riferimento mancante.");
        return turnoDAO.getTurniSettimanali(cfPersonale, refDate);
    }

    /** Report settimanale: orari e ID Treno sui quali ha prestato servizio. */
    public String generaReportSettimanale(LocalDate refDate) throws DAOException {
        List<Turno> turni = getTurniSettimanali(refDate);
        LocalDate to = refDate.plusDays(6);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder()
                .append("\n== Report settimanale (")
                .append(df.format(refDate)).append(" → ").append(df.format(to)).append(") ==\n");

        if (turni.isEmpty()) {
            sb.append("Nessun turno in questa settimana.\n");
            return sb.toString();
        }

        for (Turno t : turni) {
            sb.append(String.format(
                    "%s | %s-%s | Treno %s",
                    df.format(t.getDataServ()),
                    t.getOraInizio(),
                    t.getOraFine(),
                    t.getIdTreno()
            )).append('\n');
        }
        return sb.toString();
    }

    /* ========================
       Registro
       ======================== */

    /** Segnala un evento a registro per il personale loggato. */
    public void segnalaRegistro(String idTreno, java.time.LocalDateTime dataEvento, String descrizione) throws DAOException {
        if (idTreno == null || idTreno.isBlank()) throw new IllegalArgumentException("idTreno mancante.");
        if (dataEvento == null) throw new IllegalArgumentException("Data evento mancante.");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione mancante.");
        registroDAO.segnalaEvento(cfPersonale, idTreno, dataEvento, descrizione);
    }
}
