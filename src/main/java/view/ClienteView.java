// src/main/java/view/ClienteView.java
package view;

import controller.ClienteController;
import exception.DAOException;

import java.util.Scanner;

public final class ClienteView {

    private ClienteView() {}

    public static void menu(String username) throws DAOException {
        Scanner scanner = new Scanner(System.in);
        ClienteController controller = new ClienteController();

        while (true) {
            System.out.println();
            System.out.println("===== Menu Cliente: " + username + " =====");
            System.out.println("1) Effettua prenotazione");
            System.out.println("2) Visualizza le mie prenotazioni");
            System.out.println("3) Esci");
            System.out.print("Scelta > ");

            String line = scanner.nextLine();
            int scelta;
            try {
                scelta = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero da 1 a 3.");
                continue;
            }

            switch (scelta) {
                case 1: controller.effettuaPrenotazione();       break;
                case 2: controller.visualizzaPrenotazioni();     break;
                case 3: System.out.println("Arrivederci!"); return;
                default: System.out.println("Scelta non valida.");
            }
        }
    }
}
