// src/main/java/controller/PersonaleController.java
package controller;

import dao.ManutenzioneDAO;
import dao.TurnoDAO;
import dao.TrenoDAO;
import domain.Personale;
import domain.Treno;
import domain.Turno;
import exception.DAOException;
import view.PersonaleView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class PersonaleController implements Controller {
    private final Personale personale;
    private final TurnoDAO turnoDAO;
    private final TrenoDAO trenoDAO;
    private final ManutenzioneDAO manutDAO;
    private final Scanner scanner;

    public PersonaleController(Personale personale,
                               TurnoDAO turnoDAO,
                               TrenoDAO trenoDAO,
                               ManutenzioneDAO manutDAO,
                               Scanner scanner) {
        this.personale = personale;
        this.turnoDAO  = turnoDAO;
        this.trenoDAO   = trenoDAO;
        this.manutDAO  = manutDAO;
        this.scanner   = scanner;
    }

    @Override
    public void start(String unused) throws DAOException {
        PersonaleView.showWelcome(personale);
        while (true) {
            PersonaleView.menu(personale);
            String scelta = PersonaleView.readLine("Scelta > ");
            switch (scelta) {
                case "1":
                    handleVisualizzaTurniSettimanali();
                    break;
                case "2":
                    handleVisualizzaStoricoTurni();
                    break;
                case "3":
                    handleSegnalaManutenzione();
                    break;
                case "4":
                    PersonaleView.showMessage("Arrivederci!");
                    return;
                default:
                    PersonaleView.showError("Scelta non valida, riprova.");
            }
        }
    }

    private void handleVisualizzaTurniSettimanali() {
        try {
            List<Turno> turni = turnoDAO.getTurniSettimanali(
                    personale.getNome(),
                    personale.getCognome(),
                    LocalDate.now()
            );
            PersonaleView.showTurni(turni);
        } catch (DAOException e) {
            PersonaleView.showError("Impossibile recuperare i turni: " + e.getMessage());
        }
    }

    private void handleVisualizzaStoricoTurni() {
        try {
            LocalDate from = PersonaleView.readDateWithRetry("Da data (YYYY-MM-DD) > ");
            LocalDate to   = PersonaleView.readDateWithRetry("A data  (YYYY-MM-DD) > ");
            List<Turno> stor = turnoDAO.getStoricoTurni(
                    personale.getNome(),
                    personale.getCognome(),
                    from, to
            );
            PersonaleView.showStorico(stor);
        } catch (DAOException e) {
            PersonaleView.showError("Impossibile recuperare lo storico: " + e.getMessage());
        }
    }

    private void handleSegnalaManutenzione() {
        try {
            // 1) elenco treni
            List<Treno> treni = trenoDAO.getAllTreni();
            PersonaleView.showElencoTreni(treni);

            // 2) scelta (1..n)
            int idx = PersonaleView.readInt(
                    "Seleziona treno (numero) > ",
                    1, treni.size()
            );
            Treno scelto = treni.get(idx - 1);

            // 3) descrizione
            String desc = PersonaleView.readLine("Descrizione problema > ");

            // 4) timestamp corrente
            LocalDateTime now = LocalDateTime.now();

            // 5) chiamo il DAO
            manutDAO.segnalaManutenzione(
                    scelto.getMatricola(),
                    scelto.getMarca(),
                    scelto.getModello(),
                    now,
                    desc
            );

            PersonaleView.showMessage(
                    "Segnalazione inserita con successo alle " + now
            );

        } catch (DAOException e) {
            PersonaleView.showError("Impossibile segnalare manutenzione: " + e.getMessage());
        }
    }
}
