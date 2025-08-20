// src/main/java/view/PersonaleView.java
package view;

import domain.Personale;
import domain.Treno;
import domain.Turno;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public final class PersonaleView {
    private static final Scanner scanner = new Scanner(System.in);

    private PersonaleView() { }

    public static void showWelcome(Personale p) {
        System.out.println("==============================================");
        System.out.println("  Benvenuto nel sistema Personale: " + p);
        System.out.println("==============================================");
    }

    public static void menu(Personale p) {
        System.out.println();
        System.out.println("===== Menu Personale: "
                + p.getNome() + " " + p.getCognome() + " =====");
        System.out.println("1) Visualizza turni della settimana");
        System.out.println("2) Visualizza storico turni (per date)");
        System.out.println("3) Segnala problema/manutenzione");
        System.out.println("4) Esci");
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    showError("Inserisci un numero fra " + min + " e " + max);
                } else {
                    return v;
                }
            } catch (NumberFormatException e) {
                showError("Numero non valido, riprova.");
            }
        }
    }

    public static LocalDate readDateWithRetry(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                return LocalDate.parse(line);
            } catch (DateTimeException e) {
                showError("Formato data non valido, riprova.");
            }
        }
    }

    /** Stampa i turni settimanali */
    public static void showTurni(List<Turno> turni) {
        if (turni.isEmpty()) {
            System.out.println("Nessun turno trovato.");
            return;
        }
        System.out.println("\n--- Turni Settimanali ---");
        System.out.printf("%-12s %-8s %-8s %s%n",
                "Data", "Inizio", "Fine", "Treno");
        for (Turno t : turni) {
            System.out.printf("%-12s %-8s %-8s %s (%s %s)%n",
                    t.getDataServ(),
                    t.getOraInizio(),
                    t.getOraFine(),
                    t.getMatricola(),
                    t.getMarca(),
                    t.getModello());
        }
    }

    /** Stampa lo storico dei turni nell’intervallo richiesto */
    public static void showStorico(List<Turno> storici) {
        if (storici.isEmpty()) {
            System.out.println("Nessun turno nello storico per l’intervallo richiesto.");
            return;
        }
        System.out.println("\n--- Storico Turni ---");
        System.out.printf("%-12s %-8s %-8s %s%n",
                "Data", "Inizio", "Fine", "Treno");
        for (Turno t : storici) {
            System.out.printf("%-12s %-8s %-8s %s (%s %s)%n",
                    t.getDataServ(),
                    t.getOraInizio(),
                    t.getOraFine(),
                    t.getMatricola(),
                    t.getMarca(),
                    t.getModello());
        }
    }

    /** Nuovo: mostra elenco treni con indice */
    public static void showElencoTreni(List<Treno> treni) {
        System.out.println("\n--- Elenco Treni ---");
        for (int i = 0; i < treni.size(); i++) {
            Treno t = treni.get(i);
            System.out.printf(" %2d) %s (%s %s)%n",
                    i + 1,
                    t.getMatricola(),
                    t.getMarca(),
                    t.getModello());
        }
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }

    public static void showError(String error) {
        System.err.println("Errore: " + error);
    }
}
