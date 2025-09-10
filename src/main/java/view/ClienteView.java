package view;

import controller.ClienteController;
import dao.ClienteDao.SintesiPrenotazione;
import domain.Carrozza;
import domain.Fermata;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ClienteView {
    private final Scanner in = new Scanner(System.in);
    private final ClienteController controller;
    private final String username;
    private final String cfPrenotante;

    public ClienteView(String username, String cfPrenotante, String propertiesFile) {
        this.username = username;
        this.cfPrenotante = cfPrenotante;
        this.controller = new ClienteController(propertiesFile);
    }

    public void show() {
        System.out.println("Benvenuto nell'area Clienti, " + username + " (CF " + cfPrenotante + ")");

        boolean loop = true;
        while (loop) {
            System.out.println("\n== Area Cliente ==");
            System.out.println("1) Elenco tratte");
            System.out.println("2) Dettaglio fermate di una tratta");
            System.out.println("3) Cerca posti liberi e prenota");
            System.out.println("4) Le mie prenotazioni");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();

            switch (s) {
                case "1": elencoTratte(); break;
                case "2": dettaglioFermate(); break;
                case "3": cercaPostiEPrenota(); break;
                case "4": miePrenotazioni(); break;
                case "5": loop = false; break;
                default:  System.out.println("Scelta non valida.");
            }
        }
    }

    private void elencoTratte() {
        try {
            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) {
                System.out.println("Nessuna tratta disponibile.");
                return;
            }
            System.out.println("\nID | Partenza -> Arrivo | Orari | Treni operativi");
            for (Tratta t : tratte) {
                System.out.printf("%d | %s/%s/%s -> %s/%s/%s | %s-%s | %d%n",
                        t.getIdTratta(),
                        t.getNomePart(), t.getCittaPart(), t.getProvPart(),
                        t.getNomeArr(), t.getCittaArr(), t.getProvArr(),
                        t.getOrarioPartenza(), t.getOrarioArrivo(),
                        t.getNumTreniOperativi());
            }
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void dettaglioFermate() {
        try {
            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) {
                System.out.println("Nessuna tratta disponibile.");
                return;
            }
            System.out.println("\nSeleziona una TRATTA per vedere le fermate:");
            for (int i = 0; i < tratte.size(); i++) {
                Tratta t = tratte.get(i);
                System.out.printf("%2d) ID %d  %s/%s/%s -> %s/%s/%s  %s-%s%n",
                        i + 1, t.getIdTratta(),
                        t.getNomePart(), t.getCittaPart(), t.getProvPart(),
                        t.getNomeArr(), t.getCittaArr(), t.getProvArr(),
                        t.getOrarioPartenza(), t.getOrarioArrivo());
            }
            int idxTratta = readIntInRange("Scelta: ", 1, tratte.size()) - 1;
            Tratta sceltaTratta = tratte.get(idxTratta);

            List<Fermata> fermate = controller.fermateTratta(sceltaTratta.getIdTratta());
            if (fermate.isEmpty()) {
                System.out.println("Nessuna fermata per la tratta " + sceltaTratta.getIdTratta());
                return;
            }

            System.out.println("\nFermate per tratta ID " + sceltaTratta.getIdTratta() + " (ordine di percorrenza):");
            for (Fermata f : fermate) {
                System.out.printf("%2d) %s, %s (%s)  arr:%s  part:%s%n",
                        f.getProgressivo(), f.getNome(), f.getCitta(), f.getProvincia(),
                        f.getOrarioArrPrev(), f.getOrarioPartPrev());
            }
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void cercaPostiEPrenota() {
        try {
            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) {
                System.out.println("Nessuna tratta disponibile.");
                return;
            }
            System.out.println("\nSeleziona una TRATTA:");
            for (int i = 0; i < tratte.size(); i++) {
                Tratta t = tratte.get(i);
                System.out.printf("%2d) ID %d  %s/%s/%s -> %s/%s/%s  %s-%s%n",
                        i + 1, t.getIdTratta(),
                        t.getNomePart(), t.getCittaPart(), t.getProvPart(),
                        t.getNomeArr(), t.getCittaArr(), t.getProvArr(),
                        t.getOrarioPartenza(), t.getOrarioArrivo());
            }
            int idxTratta = readIntInRange("Scelta: ", 1, tratte.size()) - 1;
            Tratta sceltaTratta = tratte.get(idxTratta);

            List<Treno> treni = controller.treniSuTratta(sceltaTratta.getIdTratta());
            if (treni.isEmpty()) {
                System.out.println("Nessun treno assegnato alla tratta selezionata.");
                return;
            }
            System.out.println("\nSeleziona un TRENO sulla tratta scelta:");
            for (int i = 0; i < treni.size(); i++) {
                Treno t = treni.get(i);
                System.out.printf("%2d) %s  (%s/%s/%s -> %s/%s/%s)%n",
                        i + 1, t.getMatricola(),
                        t.getNomePart(), t.getCittaPart(), t.getProvPart(),
                        t.getNomeArr(), t.getCittaArr(), t.getProvArr());
            }
            int idxTreno = readIntInRange("Scelta: ", 1, treni.size()) - 1;
            Treno sceltaTreno = treni.get(idxTreno);

            List<Carrozza> carrozze = controller.carrozzeDelTreno(sceltaTreno.getMatricola());
            if (carrozze.isEmpty()) {
                System.out.println("Nessuna carrozza per il treno selezionato.");
                return;
            }
            System.out.println("\nSeleziona una CARROZZA del treno " + sceltaTreno.getMatricola() + ":");
            for (int i = 0; i < carrozze.size(); i++) {
                Carrozza c = carrozze.get(i);
                int posti = controller.postiInCarrozza(sceltaTreno.getMatricola(), c.getIdComponente());
                System.out.printf("%2d) comp:%d  n.%d  classe:%s  %s %s  (posti:%d)%n",
                        i + 1, c.getIdComponente(), c.getNumero(), c.getClasse(), c.getMarca(), c.getModello(), posti);
            }
            int idxCarrozza = readIntInRange("Scelta: ", 1, carrozze.size()) - 1;
            Carrozza sceltaCarrozza = carrozze.get(idxCarrozza);

            LocalDate dataViaggio = readDate("Data viaggio (YYYY-MM-DD): ");

            List<Integer> liberi = controller.postiLiberi(
                    sceltaTreno.getMatricola(),
                    sceltaCarrozza.getIdComponente(),
                    dataViaggio
            );
            if (liberi.isEmpty()) {
                System.out.println("Nessun posto libero in quella carrozza per la data indicata.");
                return;
            }

            System.out.println("\nSeleziona un POSTO libero:");
            for (int i = 0; i < liberi.size(); i++) {
                System.out.printf("%2d) posto %d%n", i + 1, liberi.get(i));
            }
            int idxPosto = readIntInRange("Scelta: ", 1, liberi.size()) - 1;
            int numeroPosto = liberi.get(idxPosto);

            String cfPasseggero = readString("CF passeggero (16): ");
            if (cfPasseggero.length() != 16) {
                System.out.println("CF non valido.");
                return;
            }
            String nomeP = readString("Nome passeggero: ");
            String cognomeP = readString("Cognome passeggero: ");
            LocalDate dataNascita = readDate("Data nascita (YYYY-MM-DD): ");
            String cc = readString("Carta di credito (anche con spazi/punti): ");

            System.out.println("\nConferma prenotazione:");
            System.out.printf("Tratta ID %d  %s/%s/%s -> %s/%s/%s  Data: %s%n",
                    sceltaTratta.getIdTratta(),
                    sceltaTratta.getNomePart(), sceltaTratta.getCittaPart(), sceltaTratta.getProvPart(),
                    sceltaTratta.getNomeArr(), sceltaTratta.getCittaArr(), sceltaTratta.getProvArr(),
                    dataViaggio);
            System.out.printf("Treno %s  Carrozza comp:%d  Classe:%s  Posto:%d%n",
                    sceltaTreno.getMatricola(), sceltaCarrozza.getIdComponente(), sceltaCarrozza.getClasse(), numeroPosto);

            controller.prenota(
                    cfPasseggero, nomeP, cognomeP, dataNascita,
                    cc, dataViaggio, cfPrenotante,
                    sceltaTratta.getIdTratta(),
                    sceltaTreno.getMatricola(),
                    sceltaCarrozza.getIdComponente(),
                    numeroPosto
            );

            System.out.println("Prenotazione effettuata con successo.");

        } catch (DAOException e) {
            System.out.println("Errore prenotazione: " + e.getMessage());
        }
    }

    private void miePrenotazioni() {
        try {
            List<SintesiPrenotazione> list = controller.prenotazioniCliente(cfPrenotante);
            if (list.isEmpty()) {
                System.out.println("Nessuna prenotazione trovata.");
                return;
            }
            System.out.println("\nPrenotazioni di " + username + " (CF " + cfPrenotante + "):");
            for (SintesiPrenotazione p : list) {
                System.out.printf("#%d  %s  treno:%s  tratta:%d  %s -> %s  classe:%s  carrozza:%d  posto:%d  pax:%s %s (CF %s)  CC:****%s%n",
                        p.idPrenotazione, p.dataViaggio, p.idTreno, p.idTratta,
                        p.partenza, p.arrivo, p.classe, p.idComponente, p.numeroPosto,
                        p.nomePasseggero, p.cognomePasseggero, p.cfPasseggero, p.ccLast4);
            }
        } catch (DAOException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            try { return Integer.parseInt(s.trim()); }
            catch (NumberFormatException e) { System.out.println("Numero non valido."); }
        }
    }

    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) return val;
            System.out.printf("Inserisci un numero tra %d e %d.%n", min, max);
        }
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try { return LocalDate.parse(s); }
            catch (DateTimeParseException e) { System.out.println("Data non valida. Formato atteso YYYY-MM-DD."); }
        }
    }
}
