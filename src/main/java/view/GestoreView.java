package view;

import controller.GestoreController;
import domain.Stazione;
import domain.Tipo;
import domain.Tratta;
import domain.Treno;
import domain.Turno;

import java.time.LocalDate;
import java.util.Scanner;

public class GestoreView {
    private final Scanner in = new Scanner(System.in);
    private final GestoreController ctrl = new GestoreController();

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Menu Gestore --");
            System.out.println("1) Tipi");
            System.out.println("2) Stazioni");
            System.out.println("3) Tratte");
            System.out.println("4) Treni");
            System.out.println("5) Turni");
            System.out.println("6) Indietro");
            System.out.print("Scelta: ");
            String choice = in.nextLine();
            switch (choice) {
                case "1":
                    menuTipi();
                    break;
                case "2":
                    menuStazioni();
                    break;
                case "3":
                    menuTratte();
                    break;
                case "4":
                    menuTreni();
                    break;
                case "5":
                    menuTurni();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Scelta non valida.");
                    break;
            }
        }
    }

    // === Tipi ===
    private void menuTipi() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Tipi --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Aggiorna");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listTipi().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        ctrl.addTipo(new Tipo(marca, modello));
                        System.out.println("Inserito.");
                        break;
                    }
                    case "3": {
                        System.out.print("Vecchia marca: "); String oldM = in.nextLine();
                        System.out.print("Vecchio modello: "); String oldMo = in.nextLine();
                        System.out.print("Nuova marca: "); String newM = in.nextLine();
                        System.out.print("Nuovo modello: "); String newMo = in.nextLine();
                        ctrl.updateTipo(oldM, oldMo, new Tipo(newM, newMo));
                        System.out.println("Aggiornato.");
                        break;
                    }
                    case "4": {
                        System.out.print("Marca: "); String m = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        ctrl.deleteTipo(m, mo);
                        System.out.println("Eliminato.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Stazioni ===
    private void menuStazioni() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Stazioni --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Aggiorna");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listStazioni().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Nome stazione: "); String nome = in.nextLine();
                        System.out.print("Città: "); String citta = in.nextLine();
                        System.out.print("Provincia: "); String prov = in.nextLine();
                        ctrl.addStazione(new Stazione(nome, citta, prov));
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("Nome stazione da aggiornare: "); String old = in.nextLine();
                        System.out.print("Nuova città: "); String c = in.nextLine();
                        System.out.print("Nuova provincia: "); String p = in.nextLine();
                        ctrl.updateStazione(old, new Stazione(old, c, p));
                        System.out.println("Aggiornata.");
                        break;
                    }
                    case "4": {
                        System.out.print("Nome stazione: "); String nome = in.nextLine();
                        ctrl.deleteStazione(nome);
                        System.out.println("Eliminata.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Tratte ===
    private void menuTratte() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Tratte --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Aggiorna");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listTratte().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Partenza: "); String dep = in.nextLine();
                        System.out.print("Arrivo: "); String arr = in.nextLine();
                        System.out.print("Km: "); int km = Integer.parseInt(in.nextLine());
                        ctrl.addTratta(new Tratta(dep, arr, km, 0));
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("Partenza attuale: "); String d = in.nextLine();
                        System.out.print("Arrivo attuale: "); String a = in.nextLine();
                        System.out.print("Nuova partenza: "); String nd = in.nextLine();
                        System.out.print("Nuovo arrivo: "); String na = in.nextLine();
                        System.out.print("Km: "); int km = Integer.parseInt(in.nextLine());
                        ctrl.updateTratta(new Tratta(d, a, 0, 0), new Tratta(nd, na, km, 0));
                        System.out.println("Aggiornata.");
                        break;
                    }
                    case "4": {
                        System.out.print("Partenza: "); String d = in.nextLine();
                        System.out.print("Arrivo: "); String a = in.nextLine();
                        ctrl.deleteTratta(new Tratta(d, a, 0, 0));
                        System.out.println("Eliminata.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Treni ===
    private void menuTreni() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Treni --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Aggiorna");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listTreni().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Matricola: "); String matr = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        System.out.print("Data acquisto (YYYY-MM-DD): "); LocalDate da = LocalDate.parse(in.nextLine());
                        System.out.print("Ora partenza (HH:MM): "); java.time.LocalTime op = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora arrivo (HH:MM): "); java.time.LocalTime oa = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Stazione partenza: "); String dep = in.nextLine();
                        System.out.print("Stazione arrivo: "); String arr = in.nextLine();
                        ctrl.addTreno(new Treno(matr, marca, modello, da, op, oa, dep, arr));
                        System.out.println("Inserito.");
                        break;
                    }
                    case "3": {
                        System.out.print("Matricola da aggiornare: "); String m = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        System.out.print("Data acquisto (YYYY-MM-DD): "); LocalDate da = LocalDate.parse(in.nextLine());
                        System.out.print("Ora partenza (HH:MM): "); java.time.LocalTime op = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora arrivo (HH:MM): "); java.time.LocalTime oa = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Stazione partenza: "); String dep = in.nextLine();
                        System.out.print("Stazione arrivo: "); String arr = in.nextLine();
                        ctrl.updateTreno(new Treno(m, marca, modello, da, op, oa, dep, arr));
                        System.out.println("Aggiornato.");
                        break;
                    }
                    case "4": {
                        System.out.print("Matricola: "); String m = in.nextLine();
                        ctrl.deleteTreno(m);
                        System.out.println("Eliminato.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Turni ===
    private void menuTurni() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Turni --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Aggiorna");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listTurni().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Nome: "); String nome = in.nextLine();
                        System.out.print("Cognome: "); String cognome = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate ds = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); java.time.LocalTime oi = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); java.time.LocalTime of = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Matricola treno: "); String matr = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        ctrl.addTurno(new Turno(nome, cognome, ds, oi, of, matr, marca, modello));
                        System.out.println("Inserito.");
                        break;
                    }
                    case "3": {
                        System.out.println("== Vecchio turno ==");
                        System.out.print("Nome: "); String on = in.nextLine();
                        System.out.print("Cognome: "); String oc = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate od = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); java.time.LocalTime oi = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); java.time.LocalTime of = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Matricola: "); String om = in.nextLine();
                        System.out.print("Marca: "); String oma = in.nextLine();
                        System.out.print("Modello: "); String omo = in.nextLine();
                        Turno oldT = new Turno(on, oc, od, oi, of, om, oma, omo);

                        System.out.println("== Nuovo turno ==");
                        System.out.print("Nome: "); String nn = in.nextLine();
                        System.out.print("Cognome: "); String nc = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate nd = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); java.time.LocalTime ni = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); java.time.LocalTime nf = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Matricola: "); String nm = in.nextLine();
                        System.out.print("Marca: "); String nma = in.nextLine();
                        System.out.print("Modello: "); String nmo = in.nextLine();
                        Turno newT = new Turno(nn, nc, nd, ni, nf, nm, nma, nmo);

                        ctrl.updateTurno(oldT, newT);
                        System.out.println("Aggiornato.");
                        break;
                    }
                    case "4": {
                        System.out.println("== Dati turno da eliminare ==");
                        System.out.print("Nome: "); String nome = in.nextLine();
                        System.out.print("Cognome: "); String cognome = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate ds = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); java.time.LocalTime oi = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); java.time.LocalTime of = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Matricola: "); String matr = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        ctrl.deleteTurno(new Turno(nome, cognome, ds, oi, of, matr, marca, modello));
                        System.out.println("Eliminato.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }
}
