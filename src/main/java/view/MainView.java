// src/main/java/view/MainView.java
package view;

import controller.ClienteController;
import controller.Controller;
import controller.GestoreController;
import controller.LoginController;
import controller.PersonaleController;
import controller.RegistrazioneController;
import domain.Role;
import exception.DAOException;

import java.util.Scanner;

public final class MainView {

    private MainView() {}

    public static void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("========== SISTEMA PRENOTAZIONI TRENI ==========");
            System.out.println("1) Login");
            System.out.println("2) Registrazione cliente");
            System.out.println("3) Registrazione personale");
            System.out.println("4) Esci");
            System.out.print("Scelta > ");

            final String line = scanner.nextLine().trim();
            int scelta;
            try {
                scelta = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero da 1 a 4.\n");
                continue;
            }

            switch (scelta) {
                case 1:
                    handleLogin(scanner);
                    break;
                case 2:
                    RegistrazioneController.registraCliente(scanner);
                    break;
                case 3:
                    RegistrazioneController.registraPersonale(scanner);
                    break;
                case 4:
                    running = false;
                    System.out.println("Uscita. A presto!");
                    break;
                default:
                    System.out.println("Scelta non valida (1-4).\n");
                    break;
            }
        }
    }

    private static void handleLogin(Scanner scanner) {
        System.out.print("\nUsername > ");
        String username = scanner.nextLine().trim();
        System.out.print("Password > ");
        String password = scanner.nextLine();

        Role role = LoginController.login(username, password);
        if (role == null) {
            System.out.println("Credenziali non valide.\n");
            return;
        }

        Controller ctrl;
        switch (role) {
            case CLIENTE:
                ctrl = new ClienteController();
                break;
            case PERSONALE:
                ctrl = new PersonaleController();
                break;
            case GESTORE:
                ctrl = new GestoreController();
                break;
            default:
                System.err.println("Ruolo non riconosciuto.");
                return;
        }

        try {
            ctrl.start(username);
        } catch (DAOException e) {
            System.err.println("Errore inizializzazione sessione: " + e.getMessage());
        }
        System.out.println();
    }
}
