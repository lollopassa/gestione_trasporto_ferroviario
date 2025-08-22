package view;

import controller.LoginController;
import controller.RegistrazioneController;
import domain.Credentials;
import domain.Role;
import domain.Cliente;
import exception.DAOException;

import java.sql.SQLException;
import java.util.Scanner;

public class MainView {
    private final Scanner in = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n== Gestione Trasporto Ferroviario ==");
            System.out.println("1) Login");
            System.out.println("2) Registrazione Cliente");
            System.out.println("3) Esci");
            System.out.print("Scelta: ");
            String s = in.nextLine();

            switch (s) {
                case "1":
                    doLogin();
                    break;
                case "2":
                    doRegCliente();
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
        System.out.println("Arrivederci!");
    }

    private void doLogin() {
        try {
            LoginController ctrl = new LoginController();
            System.out.print("Username: ");
            String u = in.nextLine();
            System.out.print("Password: ");
            String p = in.nextLine();

            Role role = ctrl.login(u, p);
            if (role == null) {
                System.out.println("Credenziali errate.");
                return;
            }

            // Stampa uniforme
            Credentials creds = new Credentials(u, "********", role);
            System.out.println("Accesso eseguito: " + creds);

            switch (role) {
                case CLIENTE:
                    new ClienteView(u).show();
                    break;
                case PERSONALE:
                    new PersonaleView(u).show();
                    break;
                case GESTORE:
                    new GestoreView().show();
                    break;
                default:
                    System.out.println("Ruolo sconosciuto: " + role);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore accesso dati: " + e.getMessage());
            System.out.println("Suggerimento: registra il cliente (menu 2) oppure collega lo username a un CF.");
        } catch (SQLException e) {
            System.out.println("Errore login: " + e.getMessage());
        }
    }

    private void doRegCliente() {
        RegistrazioneController ctrl = new RegistrazioneController();
        System.out.println("\n== Registrazione Cliente ==");
        System.out.print("Codice Fiscale (16): ");
        String cf = in.nextLine();
        System.out.print("Username: ");
        String user = in.nextLine();
        System.out.print("Password: ");
        String pass = in.nextLine();
        try {
            Cliente c = ctrl.registraCliente(cf, user, pass);
            System.out.println("Cliente registrato: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore registrazione: " + e.getMessage());
        }
    }
}