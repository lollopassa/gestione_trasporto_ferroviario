package view;

import controller.PersonaleController;
import dao.PersonaleDao.ReportSettimanale;
import domain.Carrozza;
import domain.Locomotiva;
import domain.Treno;
import exception.DAOException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PersonaleView {
    private final Scanner in = new Scanner(System.in);
    private final PersonaleController controller;
    private final String username;
    private final String cf;

    public PersonaleView(String username, String cf, String propertiesFile) {
        this.username = username;
        this.cf = cf;
        this.controller = new PersonaleController(propertiesFile);
    }

    public void show() {
        System.out.println("Benvenuto nell'area Personale, " + username + " (CF " + cf + ")");

        boolean loop = true;
        while (loop) {
            System.out.println("\n== Area Personale ==");
            System.out.println("1) Report turni settimanale");
            System.out.println("2) Registra nota manutenzione LOCOMOTIVA");
            System.out.println("3) Registra nota manutenzione CARROZZA");
            System.out.println("4) Indietro");
            System.out.print("Scelta: ");

            String s = in.nextLine().trim();
            switch (s) {
                case "1": reportSettimanale(); break;
                case "2": notaManutenzioneLocomotivaInteractive(); break;
                case "3": notaManutenzioneCarrozzaInteractive(); break;
                case "4": loop = false; break;
                default:  System.out.println("Scelta non valida.");
            }
        }
    }

    private void reportSettimanale() {
        LocalDate start = leggiData("Data inizio settimana (YYYY-MM-DD) [invio = lunedì corrente]: ", true);
        if (start == null) {
            LocalDate today = LocalDate.now();
            start = today.with(DayOfWeek.MONDAY);
        }
        try {
            List<ReportSettimanale> lista = controller.reportSettimanale(cf, start);
            if (lista.isEmpty()) {
                System.out.println("Nessun turno nella settimana che inizia il " + start);
                return;
            }
            System.out.println("\nTurni dal " + start + " al " + start.plusDays(6) + ":");
            for (ReportSettimanale r : lista) {
                System.out.printf("%s  treno:%s  tipo:%s  %s-%s  durata:%s%n",
                        r.dataServizio, r.idTreno, r.tipo, r.oraInizio, r.oraFine, r.durata);
            }
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void notaManutenzioneLocomotivaInteractive() {
        Treno treno = scegliTreno();
        if (treno == null) return;

        try {
            List<Locomotiva> locos = controller.locomotiveTreno(treno.getMatricola());
            if (locos.isEmpty()) {
                System.out.println("Nessuna locomotiva associata al treno " + treno.getMatricola());
                return;
            }
            System.out.println("\nSeleziona LOCOMOTIVA per treno " + treno.getMatricola() + ":");
            for (int i = 0; i < locos.size(); i++) {
                Locomotiva l = locos.get(i);
                System.out.printf(" %2d) comp:%d  %s %s  acquisto:%s%n",
                        i + 1, l.getIdComponente(), l.getMarca(), l.getModello(), l.getDataAcquisto());
            }
            int idx = scegliIndice("Scelta: ", 1, locos.size()) - 1;
            Locomotiva scelta = locos.get(idx);

            String testo = leggiStringa("Nota da aggiungere: ");
            int n = controller.appendManutenzioneLocomotiva(scelta.getIdComponente(), treno.getMatricola(), testo);
            System.out.println("Nota locomotiva aggiunta. Righe aggiornate: " + n);
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void notaManutenzioneCarrozzaInteractive() {
        Treno treno = scegliTreno();
        if (treno == null) return;

        try {
            List<Carrozza> cs = controller.carrozzeTreno(treno.getMatricola());
            if (cs.isEmpty()) {
                System.out.println("Nessuna carrozza per il treno " + treno.getMatricola());
                return;
            }
            System.out.println("\nSeleziona CARROZZA del treno " + treno.getMatricola() + ":");
            for (int i = 0; i < cs.size(); i++) {
                Carrozza c = cs.get(i);
                System.out.printf(" %2d) comp:%d  n.%d  classe:%s  %s %s  acquisto:%s%n",
                        i + 1, c.getIdComponente(), c.getNumero(), c.getClasse(), c.getMarca(), c.getModello(), c.getDataAcquisto());
            }
            int idx = scegliIndice("Scelta: ", 1, cs.size()) - 1;
            Carrozza scelta = cs.get(idx);

            String testo = leggiStringa("Nota da aggiungere: ");
            int n = controller.appendManutenzioneCarrozza(
                    scelta.getIdComponente(),
                    treno.getMatricola(),
                    scelta.getClasse(),
                    testo
            );
            System.out.println("Nota carrozza aggiunta. Righe aggiornate: " + n);
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private Treno scegliTreno() {
        try {
            List<Treno> treni = controller.elencoTreni();
            if (treni.isEmpty()) {
                System.out.println("Nessun treno presente.");
                return null;
            }
            System.out.println("\nSeleziona un TRENO:");
            for (int i = 0; i < treni.size(); i++) {
                Treno t = treni.get(i);
                System.out.printf(" %2d) %s  (%s/%s/%s -> %s/%s/%s)%n",
                        i + 1, t.getMatricola(),
                        t.getNomePart(), t.getCittaPart(), t.getProvPart(),
                        t.getNomeArr(), t.getCittaArr(), t.getProvArr());
            }
            int idx = scegliIndice("Scelta: ", 1, treni.size()) - 1;
            return treni.get(idx);
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
            return null;
        }
    }

    private int scegliIndice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Inserisci un numero tra %d e %d.%n", min, max);
        }
    }

    private String leggiStringa(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private LocalDate leggiData(String prompt, boolean consentiVuoto) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            if (consentiVuoto && s.isEmpty()) return null;
            try { return LocalDate.parse(s); }
            catch (DateTimeParseException e) { System.out.println("Data non valida. Formato YYYY-MM-DD."); }
        }
    }
}
