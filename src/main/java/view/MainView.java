package view;

import controller.MainController;
import domain.Cliente;
import exception.DAOException;

import java.util.Scanner;

public class MainView {
    private final Scanner in = new Scanner(System.in);
    private final MainController controller;

    public MainView() {
        this.controller = new MainController("loginuser.properties");
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n== Gestione Trasporto Ferroviario ==");
            System.out.println("1) Login");
            System.out.println("2) Registrazione Cliente");
            System.out.println("3) Registrazione Personale");
            System.out.println("4) Esci");
            System.out.print("Scelta: ");
            String s = in.nextLine().trim();

            switch (s) {
                case "1": doLogin(); break;
                case "2": doRegCliente(); break;
                case "3": doRegPersonale(); break;
                case "4": running = false; break;
                default:  System.out.println("Scelta non valida.");
            }
        }
        System.out.println("Arrivederci!");
    }

    private void doLogin() {
        try {
            System.out.print("Username: ");
            String u = in.nextLine().trim();
            System.out.print("Password: ");
            String p = in.nextLine();

            MainController.EsitoLogin esito = controller.login(u, p);
            if (esito == null || esito.ruolo == null) {
                System.out.println("Credenziali errate.");
                return;
            }

            System.out.println("Accesso eseguito. Ruolo: " + esito.ruolo + "  (CF: " + esito.cf + ")");

            switch (esito.ruolo) {
                case CLIENTE:
                    new ClienteView(u, esito.cf, "clienteuser.properties").show();
                    break;
                case PERSONALE:
                    new PersonaleView(u, esito.cf, "personaleuser.properties").show();
                    break;
                case GESTORE:
                    new GestoreView().show();
                    break;
                default:
                    System.out.println("Ruolo sconosciuto.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore accesso dati: " + e.getMessage());
        }
    }

    private void doRegCliente() {
        try {
            System.out.println("\n== Registrazione Cliente ==");
            System.out.print("Codice Fiscale (16): ");
            String cf = in.nextLine().trim();
            System.out.print("Nome: ");
            String nome = in.nextLine().trim();
            System.out.print("Cognome: ");
            String cognome = in.nextLine().trim();
            System.out.print("Username: ");
            String user = in.nextLine().trim();
            System.out.print("Password: ");
            String pass = in.nextLine();

            Cliente c = controller.registraCliente(cf, nome, cognome, user, pass);
            System.out.println("Cliente registrato: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore registrazione: " + e.getMessage());
        }
    }

    private void doRegPersonale() {
        try {
            System.out.println("\n== Registrazione Personale ==");
            System.out.print("Codice Fiscale (16): ");
            String cf = in.nextLine().trim();
            System.out.print("Nome: ");
            String nome = in.nextLine().trim();
            System.out.print("Cognome: ");
            String cognome = in.nextLine().trim();
            System.out.print("Username: ");
            String user = in.nextLine().trim();
            System.out.print("Password: ");
            String pass = in.nextLine();

            System.out.println("Tipo personale:");
            System.out.println("  1) MACCHINISTA");
            System.out.println("  2) CAPOTRENO");
            System.out.print("Scelta: ");
            String scelta = in.nextLine().trim();
            String tipo;
            switch (scelta) {
                case "1": tipo = "MACCHINISTA"; break;
                case "2": tipo = "CAPOTRENO";   break;
                default:
                    System.out.println("Scelta tipo non valida.");
                    return;
            }

            controller.registraPersonale(cf, nome, cognome, user, pass, tipo);
            System.out.println("Personale registrato: " + cognome + " " + nome + " (" + tipo + ", CF " + cf + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore registrazione personale: " + e.getMessage());
        }
    }
}
