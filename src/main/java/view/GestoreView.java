package view;

import controller.GestoreController;
import domain.*;
import domain.Personale.TipoPersonale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
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
            System.out.println("5) Turni (pianificazione mensile)");
            System.out.println("6) Carrozze");
            System.out.println("7) Posti");
            System.out.println("8) Tariffe");
            System.out.println("9) Assegnazioni Personale (macchinista/capotreno)");
            System.out.println("10) Coperture tratte");
            System.out.println("11) Registro manutenzione (consulta)");
            System.out.println("12) Indietro");
            System.out.print("Scelta: ");
            String choice = in.nextLine();
            switch (choice) {
                case "1": menuTipi();    break;
                case "2": menuStazioni();break;
                case "3": menuTratte();  break;
                case "4": menuTreni();   break;
                case "5": menuTurni();   break;
                case "6": menuCarrozze();break;
                case "7": menuPosti();   break;
                case "8": menuTariffe(); break;
                case "9": menuPersonale(); break;
                case "10": menuCoperture(); break;
                case "11": menuRegistro(); break;
                case "12": back = true;   break;
                default:
                    System.out.println("Scelta non valida.");
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
                        System.out.print("Provincia (2 lettere): "); String prov = in.nextLine().toUpperCase();
                        ctrl.addStazione(new Stazione(nome, citta, prov));
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("Nome stazione da aggiornare: "); String old = in.nextLine();
                        System.out.print("Nuova città: "); String c = in.nextLine();
                        System.out.print("Nuova provincia: "); String p = in.nextLine().toUpperCase();
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
            System.out.println("3) Aggiorna (capolinea/km/limite treni)");
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
                        System.out.print("Km (>0): "); int km = Integer.parseInt(in.nextLine());
                        System.out.print("Num. treni operativi (>=0): "); int lim = Integer.parseInt(in.nextLine());
                        ctrl.addTratta(new Tratta(dep, arr, km, lim));
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("Partenza attuale: "); String d = in.nextLine();
                        System.out.print("Arrivo attuale: "); String a = in.nextLine();
                        System.out.print("Nuova partenza: "); String nd = in.nextLine();
                        System.out.print("Nuovo arrivo: "); String na = in.nextLine();
                        System.out.print("Km (>0): "); int km = Integer.parseInt(in.nextLine());
                        System.out.print("Num. treni operativi (>=0): "); int lim = Integer.parseInt(in.nextLine());
                        ctrl.updateTratta(new Tratta(d, a, 0, 0), new Tratta(nd, na, km, lim));
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
                        System.out.print("ID Treno (4 cifre): "); String idTreno = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        System.out.print("Data acquisto (YYYY-MM-DD): "); LocalDate da = LocalDate.parse(in.nextLine());
                        System.out.print("Ora partenza (HH:MM): "); java.time.LocalTime op = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora arrivo (HH:MM): "); java.time.LocalTime oa = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Stazione partenza: "); String dep = in.nextLine();
                        System.out.print("Stazione arrivo: "); String arr = in.nextLine();
                        ctrl.addTreno(new Treno(idTreno, marca, modello, da, op, oa, dep, arr));
                        System.out.println("Inserito.");
                        break;
                    }
                    case "3": {
                        System.out.print("ID Treno da aggiornare (4 cifre): "); String id = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        System.out.print("Data acquisto (YYYY-MM-DD): "); LocalDate da = LocalDate.parse(in.nextLine());
                        System.out.print("Ora partenza (HH:MM): "); java.time.LocalTime op = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora arrivo (HH:MM): "); java.time.LocalTime oa = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Stazione partenza: "); String dep = in.nextLine();
                        System.out.print("Stazione arrivo: "); String arr = in.nextLine();
                        ctrl.updateTreno(new Treno(id, marca, modello, da, op, oa, dep, arr));
                        System.out.println("Aggiornato.");
                        break;
                    }
                    case "4": {
                        System.out.print("ID Treno (4 cifre): "); String id = in.nextLine();
                        ctrl.deleteTreno(id);
                        System.out.println("Eliminato.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Turni (pianificazione mensile) ===
    private void menuTurni() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Pianificazione Turni --");
            System.out.println("1) Elenca tutti");
            System.out.println("2) Elenca per mese e CF");
            System.out.println("3) Aggiungi");
            System.out.println("4) Aggiorna");
            System.out.println("5) Elimina");
            System.out.println("6) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listTurni().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("CF personale: "); String cf = in.nextLine();
                        System.out.print("Mese (YYYY-MM): "); YearMonth ym = YearMonth.parse(in.nextLine());
                        ctrl.listTurniMese(cf, ym).forEach(System.out::println);
                        break;
                    }
                    case "3": {
                        System.out.print("CF personale: "); String cf = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate ds = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); var oi = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); var of = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("ID Treno (4 cifre): "); String idTreno = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        ctrl.addTurno(new Turno(cf, ds, oi, of, idTreno, marca, modello));
                        System.out.println("Inserito.");
                        break;
                    }
                    case "4": {
                        System.out.println("== Vecchio turno ==");
                        System.out.print("CF: "); String ocf = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate od = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); var oi = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); var of = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("ID Treno: "); String oid = in.nextLine();
                        System.out.print("Marca: "); String oma = in.nextLine();
                        System.out.print("Modello: "); String omo = in.nextLine();
                        Turno oldT = new Turno(ocf, od, oi, of, oid, oma, omo);

                        System.out.println("== Nuovo turno ==");
                        System.out.print("CF: "); String ncf = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate nd = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); var ni = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); var nf = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("ID Treno: "); String nid = in.nextLine();
                        System.out.print("Marca: "); String nma = in.nextLine();
                        System.out.print("Modello: "); String nmo = in.nextLine();
                        Turno newT = new Turno(ncf, nd, ni, nf, nid, nma, nmo);

                        ctrl.updateTurno(oldT, newT);
                        System.out.println("Aggiornato.");
                        break;
                    }
                    case "5": {
                        System.out.println("== Dati turno da eliminare ==");
                        System.out.print("CF: "); String cf = in.nextLine();
                        System.out.print("Data (YYYY-MM-DD): "); LocalDate ds = LocalDate.parse(in.nextLine());
                        System.out.print("Ora inizio (HH:MM): "); var oi = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("Ora fine (HH:MM): "); var of = java.time.LocalTime.parse(in.nextLine());
                        System.out.print("ID Treno: "); String idTreno = in.nextLine();
                        System.out.print("Marca: "); String marca = in.nextLine();
                        System.out.print("Modello: "); String modello = in.nextLine();
                        ctrl.deleteTurno(new Turno(cf, ds, oi, of, idTreno, marca, modello));
                        System.out.println("Eliminato.");
                        break;
                    }
                    case "6":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Carrozze ===
    private void menuCarrozze() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Carrozze --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Cambia classe");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listCarrozze().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("N. Carrozza: "); int nc = Integer.parseInt(in.nextLine());
                        System.out.print("Classe (PRIMA/SECONDA): "); String cl = in.nextLine().toUpperCase();
                        ctrl.addCarrozza(new Carrozza(id, ma, mo, nc, cl));
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("N. Carrozza: "); int nc = Integer.parseInt(in.nextLine());
                        System.out.print("Nuova classe (PRIMA/SECONDA): "); String cl = in.nextLine().toUpperCase();
                        ctrl.updateCarrozzaClasse(id, ma, mo, nc, cl);
                        System.out.println("Aggiornata.");
                        break;
                    }
                    case "4": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("N. Carrozza: "); int nc = Integer.parseInt(in.nextLine());
                        ctrl.deleteCarrozza(id, ma, mo, nc);
                        System.out.println("Eliminata.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Posti ===
    private void menuPosti() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Posti --");
            System.out.println("1) Elenca (per carrozza)");
            System.out.println("2) Aggiungi");
            System.out.println("3) Elimina");
            System.out.println("4) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("N. Carrozza: "); int nc = Integer.parseInt(in.nextLine());
                        ctrl.listPosti(id, ma, mo, nc).forEach(System.out::println);
                        break;
                    }
                    case "2": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("N. Carrozza: "); int nc = Integer.parseInt(in.nextLine());
                        System.out.print("Numero posto (es. 1A): "); String np = in.nextLine();
                        ctrl.addPosto(new Posto(id, ma, mo, nc, np));
                        System.out.println("Inserito.");
                        break;
                    }
                    case "3": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("N. Carrozza: "); int nc = Integer.parseInt(in.nextLine());
                        System.out.print("Numero posto: "); String np = in.nextLine();
                        ctrl.deletePosto(id, ma, mo, nc, np);
                        System.out.println("Eliminato.");
                        break;
                    }
                    case "4":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Tariffe ===
    private void menuTariffe() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Gestione Tariffe --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Aggiorna prezzo");
            System.out.println("4) Elimina");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listTariffe().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("Classe (PRIMA/SECONDA): "); String cl = in.nextLine().toUpperCase();
                        System.out.print("Partenza: "); String dep = in.nextLine();
                        System.out.print("Arrivo: "); String arr = in.nextLine();
                        System.out.print("Prezzo: ");
                        String prStr = in.nextLine().trim();
                        BigDecimal pr = new BigDecimal(prStr);
                        ctrl.addTariffa(new Tariffa(ma, mo, cl, dep, arr, pr));
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("Classe (PRIMA/SECONDA): "); String cl = in.nextLine().toUpperCase();
                        System.out.print("Partenza: "); String dep = in.nextLine();
                        System.out.print("Arrivo: "); String arr = in.nextLine();
                        System.out.print("Nuovo prezzo: "); double pr = Double.parseDouble(in.nextLine());
                        ctrl.updateTariffaPrezzo(ma, mo, cl, dep, arr, pr);
                        System.out.println("Aggiornata.");
                        break;
                    }
                    case "4": {
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        System.out.print("Classe (PRIMA/SECONDA): "); String cl = in.nextLine().toUpperCase();
                        System.out.print("Partenza: "); String dep = in.nextLine();
                        System.out.print("Arrivo: "); String arr = in.nextLine();
                        ctrl.deleteTariffa(ma, mo, cl, dep, arr);
                        System.out.println("Eliminata.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Assegnazioni Personale ===
    private void menuPersonale() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Assegnazioni Personale --");
            System.out.println("1) Elenca assegnazioni");
            System.out.println("2) Promuovi cliente → personale (assegna a treno)");
            System.out.println("3) Cambia assegnazione (treno/tipo)");
            System.out.println("4) Rimuovi assegnazione");
            System.out.println("5) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listPersonale().forEach(System.out::println);
                        break;
                    case "2": { // PROMOZIONE via SP
                        System.out.print("CF cliente: "); String cf = in.nextLine();
                        System.out.print("Tipo (MACCHINISTA/CAPOTRENO): ");
                        TipoPersonale tipo = TipoPersonale.valueOf(in.nextLine().toUpperCase());
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        Personale p = ctrl.promuoviClienteAPersonale(cf, tipo, id, ma, mo);
                        System.out.println("Promosso e assegnato: " + p);
                        break;
                    }
                    case "3": {
                        System.out.print("CF: "); String cf = in.nextLine();
                        System.out.print("ID Treno attuale: "); String oid = in.nextLine();
                        System.out.print("Marca attuale: "); String oma = in.nextLine();
                        System.out.print("Modello attuale: "); String omo = in.nextLine();

                        System.out.print("Nuovo tipo (MACCHINISTA/CAPOTRENO): ");
                        TipoPersonale ntipo = TipoPersonale.valueOf(in.nextLine().toUpperCase());
                        System.out.print("Nuovo ID Treno: "); String nid = in.nextLine();
                        System.out.print("Nuova marca: "); String nma = in.nextLine();
                        System.out.print("Nuovo modello: "); String nmo = in.nextLine();

                        ctrl.cambiaAssegnazionePersonale(cf, oid, oma, omo, ntipo, nid, nma, nmo);
                        System.out.println("Aggiornata.");
                        break;
                    }
                    case "4": {
                        System.out.print("CF: "); String cf = in.nextLine();
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        ctrl.rimuoviAssegnazione(cf, id, ma, mo);
                        System.out.println("Rimossa.");
                        break;
                    }
                    case "5":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Coperture (tabella copre) ===
    private void menuCoperture() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Coperture Tratte --");
            System.out.println("1) Elenca");
            System.out.println("2) Aggiungi");
            System.out.println("3) Elimina");
            System.out.println("4) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listCoperture().forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("Partenza: "); String dep = in.nextLine();
                        System.out.print("Arrivo: "); String arr = in.nextLine();
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        ctrl.addCopertura(dep, arr, id, ma, mo);
                        System.out.println("Inserita.");
                        break;
                    }
                    case "3": {
                        System.out.print("Partenza: "); String dep = in.nextLine();
                        System.out.print("Arrivo: "); String arr = in.nextLine();
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        System.out.print("Marca: "); String ma = in.nextLine();
                        System.out.print("Modello: "); String mo = in.nextLine();
                        ctrl.deleteCopertura(dep, arr, id, ma, mo);
                        System.out.println("Eliminata.");
                        break;
                    }
                    case "4":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    // === Registro manutenzione ===
    private void menuRegistro() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Registro Manutenzione --");
            System.out.println("1) Elenca tutto");
            System.out.println("2) Filtra per ID Treno");
            System.out.println("3) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();
            try {
                switch (s) {
                    case "1":
                        ctrl.listRegistro(null).forEach(System.out::println);
                        break;
                    case "2": {
                        System.out.print("ID Treno: "); String id = in.nextLine();
                        ctrl.listRegistro(id).forEach(System.out::println);
                        break;
                    }
                    case "3":
                        back = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }
}