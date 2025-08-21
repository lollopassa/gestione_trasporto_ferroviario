package view;

import controller.PersonaleController;
import domain.Turno;
import exception.DAOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class PersonaleView {
    private final Scanner in = new Scanner(System.in);
    private final PersonaleController ctrl = new PersonaleController();

    public void show() {
        System.out.print("Nome: ");
        String nome = in.nextLine();
        System.out.print("Cognome: ");
        String cognome = in.nextLine();

        boolean back = false;
        while (!back) {
            System.out.println("\n-- Menu Personale --");
            System.out.println("1) Turni settimanali");
            System.out.println("2) Storico turni");
            System.out.println("3) Segnala manutenzione");
            System.out.println("4) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();

            try {
                switch (s) {
                    case "1": {
                        System.out.print("Data riferimento (YYYY-MM-DD): ");
                        LocalDate ref = LocalDate.parse(in.nextLine());
                        List<Turno> turni = ctrl.getTurniSettimanali(nome, cognome, ref);
                        turni.forEach(System.out::println);
                        break;
                    }
                    case "2": {
                        System.out.print("Da (YYYY-MM-DD): ");
                        LocalDate from = LocalDate.parse(in.nextLine());
                        System.out.print("A (YYYY-MM-DD): ");
                        LocalDate to = LocalDate.parse(in.nextLine());
                        List<Turno> turni = ctrl.getStoricoTurni(nome, cognome, from, to);
                        turni.forEach(System.out::println);
                        break;
                    }
                    case "3": {
                        System.out.print("Matricola treno: "); String matr = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        System.out.print("Descrizione: "); String descr = in.nextLine();

                        // timestamp corrente
                        java.time.LocalDateTime adesso = java.time.LocalDateTime.now();
                        ctrl.segnalaManutenzione(matr, marca, modello, adesso, descr);
                        System.out.println("Segnalazione inviata in data/ora: " + adesso);
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
