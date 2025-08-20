package controller;

import dao.ManutenzioneDAO;
import dao.TurnoDAO;
import domain.Turno;
import exception.DAOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PersonaleController {
    private final TurnoDAO turnoDAO = new TurnoDAO();
    private final ManutenzioneDAO manutDAO = new ManutenzioneDAO();

    public List<Turno> getTurniSettimanali(String nome, String cognome, LocalDate refDate) throws DAOException {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome mancante.");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome mancante.");
        if (refDate == null) throw new IllegalArgumentException("Data di riferimento mancante.");
        return turnoDAO.getTurniSettimanali(nome, cognome, refDate);
    }
    public List<Turno> getStoricoTurni(String nome, String cognome, LocalDate from, LocalDate to) throws DAOException {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome mancante.");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome mancante.");
        if (from == null || to == null) throw new IllegalArgumentException("Intervallo date mancante.");
        return turnoDAO.getStoricoTurni(nome, cognome, from, to);
    }
    public void segnalaManutenzione(String matricola, String marca, String modello,
                                    LocalDateTime dataEvento, String descrizione) throws DAOException {
        if (matricola == null || matricola.isBlank()) throw new IllegalArgumentException("Matricola mancante.");
        if (marca == null || marca.isBlank()) throw new IllegalArgumentException("Marca mancante.");
        if (modello == null || modello.isBlank()) throw new IllegalArgumentException("Modello mancante.");
        if (dataEvento == null) throw new IllegalArgumentException("Data evento mancante.");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione mancante.");
        manutDAO.segnalaManutenzione(matricola, marca, modello, dataEvento, descrizione);
    }
}
