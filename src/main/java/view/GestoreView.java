package view;

import controller.GestoreController;
import dao.GestoreDao.EsitoMese;
import dao.GestoreDao.PersonaleLight;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class GestoreView {
    private final Scanner in = new Scanner(System.in);
    private final GestoreController controller = new GestoreController();

    public void show() {
        boolean loop = true;
        while (loop) {
            System.out.println("\n== Area Gestore ==");

            System.out.println("1) Crea STAZIONE");
            System.out.println("2) Aggiungi FERMATA a una Tratta");
            System.out.println("3) Aggiorna n. treni operativi per Tratta");

            System.out.println("4) Crea TRENO");
            System.out.println("5) Crea LOCOMOTIVA");
            System.out.println("6) Crea CARROZZA con POSTI");

            System.out.println("7) Gestisci associazioni dei Treni per Tratta");
            System.out.println("8) Programma Turno MENSILE (macchinista + capotreno)");

            System.out.println("9) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine().trim();

            switch (s) {
                case "1": creaStazioneView(); break;
                case "2": aggiungiFermataView(); break;
                case "3": aggiornaNumTreniOperativiView(); break;
                case "4": creaTrenoView(); break;
                case "5": creaLocomotivaView(); break;
                case "6": creaCarrozzaConPostiView(); break;
                case "7": gestisciAssociazioniTreniTratte(); break;
                case "8": programmaMeseTreno(); break;
                case "9": loop = false; break;
                default:  System.out.println("Scelta non valida.");
            }
        }
    }


    private void programmaMeseTreno() {
        try {
            List<Treno> treni = controller.elencoTreni();
            if (treni.isEmpty()) { System.out.println("Nessun treno presente."); return; }
            System.out.println("\nSeleziona un TRENO:");
            for (int i=0;i<treni.size();i++) System.out.printf("  %d) %s%n", i+1, labelTreno(treni.get(i)));
            String idTreno = treni.get(readIndex("Scelta: ", treni.size())).getMatricola();

            YearMonth mese = readYearMonth("Mese (YYYY-MM): ");

            List<PersonaleLight> macs = controller.personale("MACCHINISTA");
            if (macs.isEmpty()) { System.out.println("Nessun macchinista presente."); return; }
            System.out.println("\nSeleziona MACCHINISTA (per tutto il mese):");
            for (int i=0;i<macs.size();i++) System.out.printf("  %d) %s%n", i+1, labelPersonale(macs.get(i)));
            String cfMac = macs.get(readIndex("Scelta: ", macs.size())).cf;
            LocalTime mInizio = readTime("Ora inizio macchinista (HH:MM): ");
            LocalTime mFine   = readTime("Ora fine macchinista (HH:MM): ");

            List<PersonaleLight> capi = controller.personale("CAPOTRENO");
            if (capi.isEmpty()) { System.out.println("Nessun capotreno presente."); return; }
            System.out.println("\nSeleziona CAPOTRENO (per tutto il mese):");
            for (int i=0;i<capi.size();i++) System.out.printf("  %d) %s%n", i+1, labelPersonale(capi.get(i)));
            String cfCapo = capi.get(readIndex("Scelta: ", capi.size())).cf;
            LocalTime cInizio = readTime("Ora inizio capotreno (HH:MM): ");
            LocalTime cFine   = readTime("Ora fine capotreno (HH:MM): ");

            EsitoMese esito = controller.programmaMeseTreno(idTreno, mese, cfMac, mInizio, mFine, cfCapo, cInizio, cFine);
            System.out.printf("Pianificazione mensile: %d giorni creati, %d conflitti, %d errori%n",
                    esito.pianificati.size(), esito.conflitti.size(), esito.errori.size());
            if (!esito.conflitti.isEmpty()) System.out.println("  Giorni in conflitto: " + esito.conflitti);
            if (!esito.errori.isEmpty())     System.out.println("  Giorni in errore: " + esito.errori);
        } catch (DAOException e) {
            printErr(e, "Errore programmazione mensile");
        }
    }


    private void gestisciAssociazioniTreniTratte() {
        try {
            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) { System.out.println("Nessuna tratta presente."); return; }

            System.out.println("\nSeleziona TRATTA:");
            for (int i=0;i<tratte.size();i++) System.out.printf("  %d) %s%n", i+1, labelTratta(tratte.get(i)));
            Tratta scelta = tratte.get(readIndex("Scelta: ", tratte.size()));

            System.out.println("\nAzione:");
            System.out.println("  1) Assegna/Sposta un TRENO su questa tratta");
            System.out.println("  2) Sposta un TRENO da questa tratta su un'altra");
            System.out.print("Scelta: ");
            String az = in.nextLine().trim();

            switch (az) {
                case "1": {
                    List<Treno> tutti = controller.elencoTreni();
                    tutti.removeIf(t -> t.getIdTratta() == scelta.getIdTratta());
                    if (tutti.isEmpty()) { System.out.println("Nessun treno disponibile da spostare."); return; }
                    System.out.println("\nSeleziona TRENO da assegnare a " + labelTratta(scelta) + ":");
                    for (int i=0;i<tutti.size();i++) System.out.printf("  %d) %s%n", i+1, labelTreno(tutti.get(i)));
                    Treno t = tutti.get(readIndex("Scelta: ", tutti.size()));
                    controller.assegnaTrenoATratta(t.getMatricola(), scelta.getIdTratta());
                    System.out.println("Operazione completata.");
                    break;
                }
                case "2": {
                    List<Treno> suTratta = controller.treniByTratta(scelta.getIdTratta());
                    if (suTratta.isEmpty()) { System.out.println("Nessun treno su questa tratta."); return; }
                    System.out.println("\nSeleziona TRENO da spostare:");
                    for (int i=0;i<suTratta.size();i++) System.out.printf("  %d) %s%n", i+1, labelTreno(suTratta.get(i)));
                    Treno t = suTratta.get(readIndex("Scelta: ", suTratta.size()));

                    System.out.println("\nSeleziona nuova TRATTA:");
                    for (int i=0;i<tratte.size();i++) System.out.printf("  %d) %s%n", i+1, labelTratta(tratte.get(i)));
                    Tratta nuova = tratte.get(readIndex("Scelta: ", tratte.size()));

                    controller.spostaTrenoSuTratta(t.getMatricola(), nuova.getIdTratta());
                    System.out.println("Treno spostato su nuova tratta.");
                    break;
                }
                default:
                    System.out.println("Scelta non valida.");
            }
        } catch (DAOException e) {
            printErr(e, "Errore gestione associazioni treni/tratte");
        }
    }


    private void aggiornaNumTreniOperativiView() {
        try {
            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) { System.out.println("Nessuna tratta presente."); return; }

            System.out.println("\nSeleziona TRATTA da aggiornare:");
            for (int i=0;i<tratte.size();i++) System.out.printf("  %d) %s%n", i+1, labelTratta(tratte.get(i)));
            Tratta scelta = tratte.get(readIndex("Scelta: ", tratte.size()));

            System.out.printf("Valore attuale n. treni operativi: %d%n", scelta.getNumTreniOperativi());
            int nuovo = readInt("Nuovo valore (intero ≥ 0): ");
            if (nuovo < 0) { System.out.println("Valore non valido."); return; }

            controller.aggiornaNumTreniOperativi(scelta.getIdTratta(), nuovo);
            System.out.println("Numero treni operativi aggiornato.");
        } catch (DAOException e) {
            printErr(e, "Errore aggiornamento n. treni operativi");
        }
    }

    private void creaStazioneView() {
        try {
            String nome  = readString("Nome stazione: ");
            String citta = readString("Città: ");
            String prov  = readString("Provincia (2 lettere): ").toUpperCase();
            if (prov.length()!=2) { System.out.println("Provincia non valida."); return; }
            controller.creaStazione(nome, citta, prov);
            System.out.println("Stazione creata con successo.");
        } catch (DAOException e) {
            System.out.println("Errore creazione stazione: " + e.getMessage());
            if (e.getCause()!=null) System.out.println("Dettagli: " + e.getCause().getMessage());
        }
    }

    private void aggiungiFermataView() {
        try {
            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) { System.out.println("Nessuna tratta disponibile."); return; }
            System.out.println("\nSeleziona TRATTA a cui aggiungere la fermata:");
            for (int i=0;i<tratte.size();i++) System.out.printf("  %d) %s%n", i+1, labelTratta(tratte.get(i)));
            Tratta t = tratte.get(readIndex("Scelta: ", tratte.size()));

            String nome = readString("Nome stazione: ");
            String citta = readString("Città: ");
            String prov = readString("Provincia (2 lettere): ").toUpperCase();
            int progressivo = readInt("Progressivo (>=1): ");
            if (progressivo < 1) { System.out.println("Progressivo non valido."); return; }
            LocalTime arr = readTime("Orario arrivo previsto (HH:MM): ");
            LocalTime part = readTime("Orario partenza prevista (HH:MM): ");

            controller.aggiungiFermata(t.getIdTratta(), nome, citta, prov, progressivo, arr, part);
            System.out.println("Fermata aggiunta con successo.");
        } catch (DAOException e) {
            printErr(e, "Errore aggiunta fermata");
        }
    }

    private void creaTrenoView() {
        try {
            String matricola = readString("Matricola (4 cifre): ").trim();
            if (!matricola.matches("\\d{4}")) {
                System.out.println("Matricola non valida (servono 4 cifre).");
                return;
            }

            List<Tratta> tratte = controller.elencoTratte();
            if (tratte.isEmpty()) { System.out.println("Nessuna tratta disponibile."); return; }
            System.out.println("\nSeleziona TRATTA su cui assegnare il nuovo treno:");
            for (int i=0;i<tratte.size();i++) System.out.printf("  %d) %s%n", i+1, labelTratta(tratte.get(i)));
            int idTratta = tratte.get(readIndex("Scelta: ", tratte.size())).getIdTratta();

            controller.creaTreno(matricola, idTratta);
            System.out.println("Treno creato con successo.");
        } catch (DAOException e) {
            printErr(e, "Errore creazione treno");
        }
    }

    private void creaLocomotivaView() {
        try {
            List<Treno> treni = controller.elencoTreniSenzaLocomotiva();
            if (treni.isEmpty()) {
                System.out.println("Tutti i treni hanno già una locomotiva. Usa la sostituzione (vedi sotto) oppure crea un nuovo treno.");
                return;
            }
            System.out.println("\nSeleziona TRENO per la locomotiva:");
            for (int i=0;i<treni.size();i++) System.out.printf("  %d) %s%n", i+1, labelTreno(treni.get(i)));
            Treno t = treni.get(readIndex("Scelta: ", treni.size()));

            int idComp = readInt("ID componente (intero univoco): ");
            String marca = readString("Marca: ");
            String modello = readString("Modello: ");
            LocalDate dataAcq = readDate("Data acquisto (YYYY-MM-DD): ");
            String manut = readString("Note manutenzione (opzionale, invio per vuoto): ");
            if (manut.isEmpty()) manut = null;

            controller.creaLocomotiva(idComp, t.getMatricola(), marca, modello, dataAcq, manut);
            System.out.println("Locomotiva creata e associata al treno.");
        } catch (DAOException e) {
            printErr(e, "Errore creazione locomotiva");
        }
    }


    private void creaCarrozzaConPostiView() {
        try {
            List<Treno> treni = controller.elencoTreni();
            if (treni.isEmpty()) { System.out.println("Nessun treno presente."); return; }
            System.out.println("\nSeleziona TRENO su cui creare la carrozza:");
            for (int i=0;i<treni.size();i++) System.out.printf("  %d) %s%n", i+1, labelTreno(treni.get(i)));
            Treno t = treni.get(readIndex("Scelta: ", treni.size()));

            int idComp = readInt("ID componente (intero univoco): ");
            String classe;
            while (true) {
                classe = readString("Classe (PRIMA/SECONDA): ").toUpperCase();
                if (classe.equals("PRIMA") || classe.equals("SECONDA")) break;
                System.out.println("Classe non valida. Inserisci PRIMA o SECONDA.");
            }
            int numero = readInt("Numero carrozza nel treno (>=1): ");
            if (numero < 1) { System.out.println("Numero carrozza non valido."); return; }
            String marca = readString("Marca: ");
            String modello = readString("Modello: ");
            LocalDate dataAcq = readDate("Data acquisto (YYYY-MM-DD): ");
            int numPosti = readInt("Numero posti da generare (>=1): ");
            if (numPosti < 1) { System.out.println("Numero posti non valido."); return; }

            controller.creaCarrozzaConPosti(idComp, t.getMatricola(), classe, numero, marca, modello, dataAcq, numPosti);
            System.out.println("Carrozza creata con successo e posti generati.");
        } catch (DAOException e) {
            printErr(e, "Errore creazione carrozza con posti");
        }
    }


    private String labelTreno(Treno t) {
        return t.getMatricola() + "  (" +
                t.getNomePart()+"/"+t.getCittaPart()+"/"+t.getProvPart() + " -> " +
                t.getNomeArr()+"/"+t.getCittaArr()+"/"+t.getProvArr() + ")";
    }
    private String labelTratta(Tratta t) {
        return "ID " + t.getIdTratta() + "  " +
                t.getNomePart()+"/"+t.getCittaPart()+"/"+t.getProvPart() + " -> " +
                t.getNomeArr()+"/"+t.getCittaArr()+"/"+t.getProvArr() +
                "  (treni operativi: " + t.getNumTreniOperativi() + ")";
    }
    private String labelPersonale(PersonaleLight p) { return p.cognome + " " + p.nome + " (" + p.cf + ")"; }

    private int readIndex(String prompt, int max) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try { int v = Integer.parseInt(s); if (v>=1 && v<=max) return v-1; } catch (NumberFormatException ignored) {}
            System.out.println("Scelta non valida. Inserisci un numero tra 1 e " + max + ".");
        }
    }
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try { return Integer.parseInt(s); } catch (NumberFormatException ignored) {
                System.out.println("Numero non valido.");
            }
        }
    }
    private LocalTime readTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try { if (s.length()==5) s += ":00"; return LocalTime.parse(s); }
            catch (DateTimeParseException e) { System.out.println("Ora non valida (HH:MM)."); }
        }
    }
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try { return LocalDate.parse(s); }
            catch (DateTimeParseException e) { System.out.println("Data non valida (YYYY-MM-DD)."); }
        }
    }
    private YearMonth readYearMonth(String prompt) {
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM");
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try { return YearMonth.parse(s, fmt); }
            catch (DateTimeParseException e) { System.out.println("Formato non valido. Usa YYYY-MM."); }
        }
    }
    private String readString(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }
    private void printErr(DAOException e, String prefix) {
        System.out.println(prefix + ": " + e.getMessage());
        if (e.getCause()!=null) System.out.println("Dettagli: " + e.getCause().getMessage());
    }
}
