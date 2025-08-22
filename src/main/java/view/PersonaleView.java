package view;

import controller.PersonaleController;
import domain.Registro;
import domain.Turno;
import exception.DAOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class PersonaleView {
    private final Scanner in = new Scanner(System.in);
    private final PersonaleController ctrl;

    /** Passa qui lo username dell'utente loggato (es.: "luca"). */
    public PersonaleView(String loggedUsername) throws DAOException {
        this.ctrl = PersonaleController.forUser(loggedUsername);
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Menu Personale --");
            System.out.println("1) Pianificazione mensile");
            System.out.println("2) Report settimanale");
            System.out.println("3) Segnala evento a registro");
            System.out.println("4) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();

            try {
                switch (s) {
                    case "1": {
                        System.out.print("Mese (YYYY-MM): ");
                        YearMonth ym = YearMonth.parse(in.nextLine().trim());
                        String pianificazione = ctrl.stampaPianificazioneMensile(ym);
                        System.out.println(pianificazione);
                        break;
                    }
                    case "2": {
                        System.out.print("Data riferimento settimana (YYYY-MM-DD): ");
                        LocalDate ref = LocalDate.parse(in.nextLine().trim());
                        String report = ctrl.generaReportSettimanale(ref);
                        System.out.println(report);
                        break;
                    }
                    case "3": {
                        System.out.print("ID Treno (4 cifre): ");
                        String idTreno = in.nextLine().trim();
                        System.out.print("Descrizione evento: ");
                        String descr = in.nextLine().trim();

                        LocalDateTime adesso = LocalDateTime.now();
                        ctrl.segnalaRegistro(idTreno, adesso, descr);

                        String when = adesso.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        System.out.printf("Evento registrato: %s | %s | %s%n", idTreno, when, descr);
                        break;
                    }
                    case "4":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Input non valido: " + e.getMessage());
            } catch (DAOException e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }
}
