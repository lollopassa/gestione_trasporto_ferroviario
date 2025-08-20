package view;

import controller.LoginController;
import controller.RegistrazioneController;
import domain.Personale.TipoPersonale;
import domain.Role;
import domain.Cliente;
import domain.Personale;
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
            System.out.println("3) Registrazione Personale");
            System.out.println("4) Esci");
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
                    doRegPersonale();
                    break;
                case "4":
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

            // Java 11: switch classico
            switch (role) {
                case CLIENTE:
                    new ClienteView().show();
                    break;
                case PERSONALE:
                    new PersonaleView().show();
                    break;
                case GESTORE:
                    new GestoreView().show();
                    break;
                default:
                    System.out.println("Ruolo sconosciuto: " + role);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Errore login: " + e.getMessage());
        }
    }

    private void doRegCliente() {
        RegistrazioneController ctrl = new RegistrazioneController();
        System.out.println("\n== Registrazione Cliente ==");
        System.out.print("Nome: ");
        String nome = in.nextLine();
        System.out.print("Cognome: ");
        String cognome = in.nextLine();
        System.out.print("Username: ");
        String user = in.nextLine();
        System.out.print("Password: ");
        String pass = in.nextLine();
        try {
            Cliente c = ctrl.registraCliente(nome, cognome, user, pass);
            System.out.println("Cliente registrato: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore registrazione: " + e.getMessage());
        }
    }

    private void doRegPersonale() {
        RegistrazioneController ctrl = new RegistrazioneController();
        System.out.println("\n== Registrazione Personale ==");
        System.out.print("Nome: ");
        String nome = in.nextLine();
        System.out.print("Cognome: ");
        String cognome = in.nextLine();
        System.out.print("Tipo (MACCHINISTA/CAPOTRENO): ");
        String tipo = in.nextLine();
        System.out.print("Username: ");
        String user = in.nextLine();
        System.out.print("Password: ");
        String pass = in.nextLine();
        try {
            TipoPersonale tp = TipoPersonale.valueOf(tipo.toUpperCase());
            Personale p = ctrl.registraPersonale(nome, cognome, tp, user, pass);
            System.out.println("Personale registrato: " + p);
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore registrazione: " + e.getMessage());
        }
    }
}
