// src/main/java/view/GestoreView.java
package view;

import domain.*;
import java.util.List;
import java.util.Scanner;

public final class GestoreView {
    private static final Scanner scanner = new Scanner(System.in);

    private GestoreView() {}

    public static void showMainMenu(String username) {
        System.out.println("\n========== MENU GESTORE ==========");
        System.out.println("Benvenuto, " + username);
        System.out.println("1) Gestione Tipi");
        System.out.println("2) Gestione Stazioni");
        System.out.println("3) Gestione Tratte");
        System.out.println("4) Gestione Treni");
        System.out.println("5) Gestione Turni");   // nuova voce
        System.out.println("6) Esci");              // spostata qui
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.err.println("Inserisci un numero tra " + min + " e " + max);
            }
        }
    }

    public static <T> void showIndexedList(List<T> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.printf(" %2d) %s%n", i + 1, items.get(i));
        }
    }

    public static void showMessage(String msg) {
        System.out.println(msg);
    }

    public static void showError(String err) {
        System.err.println("Errore: " + err);
    }
}
