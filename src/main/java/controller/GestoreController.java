// src/main/java/controller/GestoreController.java
package controller;

import dao.*;
import domain.*;
import exception.DAOException;
import view.GestoreView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GestoreController implements Controller {

    // DAO usati come campi, in modo coerente
    private final TipoDAO tipoDAO       = new TipoDAO();
    private final StazioneDAO stazDAO   = new StazioneDAO();
    private final TrattaDAO trattaDAO   = new TrattaDAO();
    private final TrenoDAO trenoDAO     = new TrenoDAO();
    private final TurnoDAO turnoDAO     = new TurnoDAO();

    @Override
    public void start(String username) throws DAOException {
        boolean loop = true;
        while (loop) {
            GestoreView.showMainMenu(username);
            String choice = GestoreView.readLine("Scelta > ");
            switch (choice) {
                case "1": gestioneTipi();     break;
                case "2": gestioneStazioni(); break;
                case "3": gestioneTratte();   break;
                case "4": gestioneTreni();    break;
                case "5": gestioneTurni();    break;
                case "6": loop = false;       break;
                default:
                    GestoreView.showError("Scelta non valida.");
            }
        }
    }

    /* ===================== TIPI ===================== */
    private void gestioneTipi() {
        try {
            boolean back = false;
            while (!back) {
                System.out.println("\n--- Gestione Tipi ---");
                System.out.println("1) Elenca");
                System.out.println("2) Aggiungi");
                System.out.println("3) Modifica");
                System.out.println("4) Elimina");
                System.out.println("5) Indietro");
                String c = GestoreView.readLine("Scelta > ");
                List<Tipo> list;

                switch (c) {
                    case "1":
                        list = tipoDAO.listAll();
                        if (list.isEmpty()) GestoreView.showMessage("(nessun tipo)");
                        GestoreView.showIndexedList(list);
                        break;

                    case "2":
                        String marca = GestoreView.readLine("Marca   > ");
                        String modello = GestoreView.readLine("Modello > ");
                        tipoDAO.insert(new Tipo(marca, modello));
                        GestoreView.showMessage("Tipo inserito.");
                        break;

                    case "3":
                        list = tipoDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessun tipo da modificare."); break; }
                        GestoreView.showIndexedList(list);
                        int iMod = GestoreView.readInt("Seleziona da modificare > ", 1, list.size()) - 1;
                        Tipo old = list.get(iMod);
                        String newMarca = GestoreView.readLine("Nuova Marca   [" + old.getMarca() + "]   > ");
                        String newModello = GestoreView.readLine("Nuovo Modello [" + old.getModello() + "] > ");
                        tipoDAO.update(old.getMarca(), old.getModello(),
                                new Tipo(
                                        newMarca.isEmpty() ? old.getMarca()   : newMarca,
                                        newModello.isEmpty() ? old.getModello() : newModello
                                ));
                        GestoreView.showMessage("Tipo aggiornato.");
                        break;

                    case "4":
                        list = tipoDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessun tipo da eliminare."); break; }
                        GestoreView.showIndexedList(list);
                        int iDel = GestoreView.readInt("Seleziona da eliminare > ", 1, list.size()) - 1;
                        Tipo toDel = list.get(iDel);
                        tipoDAO.delete(toDel.getMarca(), toDel.getModello());
                        GestoreView.showMessage("Tipo eliminato.");
                        break;

                    case "5":
                        back = true;
                        break;

                    default:
                        GestoreView.showError("Opzione non valida.");
                }
            }
        } catch (DAOException e) {
            GestoreView.showError(e.getMessage());
        }
    }

    /* ===================== STAZIONI ===================== */
    private void gestioneStazioni() {
        try {
            boolean back = false;
            while (!back) {
                System.out.println("\n--- Gestione Stazioni ---");
                System.out.println("1) Elenca");
                System.out.println("2) Aggiungi");
                System.out.println("3) Modifica");
                System.out.println("4) Elimina");
                System.out.println("5) Indietro");
                String c = GestoreView.readLine("Scelta > ");
                List<Stazione> list;

                switch (c) {
                    case "1":
                        list = stazDAO.listAll();
                        if (list.isEmpty()) GestoreView.showMessage("(nessuna stazione)");
                        GestoreView.showIndexedList(list);
                        break;

                    case "2":
                        String ns = GestoreView.readLine("Nome stazione              > ");
                        String ci = GestoreView.readLine("Città                      > ");
                        String pr = GestoreView.readLine("Provincia (2 lettere)      > ");
                        stazDAO.insert(new Stazione(ns, ci, pr));
                        GestoreView.showMessage("Stazione inserita.");
                        break;

                    case "3":
                        list = stazDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessuna stazione da modificare."); break; }
                        GestoreView.showIndexedList(list);
                        int iMod = GestoreView.readInt("Seleziona da modificare > ", 1, list.size()) - 1;
                        Stazione oldS = list.get(iMod);
                        String newNs = GestoreView.readLine("Nuovo nome      [" + oldS.getNomeStazione() + "] > ");
                        String newCi = GestoreView.readLine("Nuova città     [" + oldS.getCitta() + "]        > ");
                        String newPr = GestoreView.readLine("Nuova provincia [" + oldS.getProvincia() + "]    > ");
                        stazDAO.update(oldS,
                                new Stazione(
                                        newNs.isEmpty() ? oldS.getNomeStazione() : newNs,
                                        newCi.isEmpty() ? oldS.getCitta()        : newCi,
                                        newPr.isEmpty() ? oldS.getProvincia()    : newPr
                                )
                        );
                        GestoreView.showMessage("Stazione aggiornata.");
                        break;

                    case "4":
                        list = stazDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessuna stazione da eliminare."); break; }
                        GestoreView.showIndexedList(list);
                        int iDel = GestoreView.readInt("Seleziona da eliminare > ", 1, list.size()) - 1;
                        stazDAO.delete(list.get(iDel));
                        GestoreView.showMessage("Stazione eliminata.");
                        break;

                    case "5":
                        back = true;
                        break;

                    default:
                        GestoreView.showError("Opzione non valida.");
                }
            }
        } catch (DAOException e) {
            GestoreView.showError(e.getMessage());
        }
    }

    /* ===================== TRATTE ===================== */
    private void gestioneTratte() {
        try {
            boolean back = false;
            while (!back) {
                System.out.println("\n--- Gestione Tratte ---");
                System.out.println("1) Elenca");
                System.out.println("2) Aggiungi");
                System.out.println("3) Modifica");
                System.out.println("4) Elimina");
                System.out.println("5) Indietro");
                String c = GestoreView.readLine("Scelta > ");
                List<Tratta> list;

                switch (c) {
                    case "1":
                        list = trattaDAO.listAll();
                        if (list.isEmpty()) GestoreView.showMessage("(nessuna tratta)");
                        GestoreView.showIndexedList(list);
                        break;

                    case "2":
                        String dSt = GestoreView.readLine("Stazione di partenza > ");
                        String dCi = GestoreView.readLine("Città partenza      > ");
                        String dPr = GestoreView.readLine("Prov. partenza (2)  > ");
                        String aSt = GestoreView.readLine("Stazione di arrivo  > ");
                        String aCi = GestoreView.readLine("Città arrivo        > ");
                        String aPr = GestoreView.readLine("Prov. arrivo (2)    > ");
                        int km  = Integer.parseInt(GestoreView.readLine("Km                  > "));
                        int num = Integer.parseInt(GestoreView.readLine("Num treni operativi > "));
                        trattaDAO.insert(new Tratta(dSt,dCi,dPr,aSt,aCi,aPr,km,num));
                        GestoreView.showMessage("Tratta inserita.");
                        break;

                    case "3":
                        list = trattaDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessuna tratta da modificare."); break; }
                        GestoreView.showIndexedList(list);
                        int iMod = GestoreView.readInt("Seleziona da modificare > ", 1, list.size()) - 1;
                        Tratta oldT = list.get(iMod);

                        String ndSt = GestoreView.readLine("Nuova partenza [" + oldT.getDepNomeStazione() + "] > ");
                        String ndCi = GestoreView.readLine("Nuova città   [" + oldT.getDepCitta()        + "] > ");
                        String ndPr = GestoreView.readLine("Nuova prov.   [" + oldT.getDepProvincia()    + "] > ");
                        String naSt = GestoreView.readLine("Nuovo arrivo  [" + oldT.getArrNomeStazione() + "] > ");
                        String naCi = GestoreView.readLine("Nuova città   [" + oldT.getArrCitta()        + "] > ");
                        String naPr = GestoreView.readLine("Nuova prov.   [" + oldT.getArrProvincia()    + "] > ");
                        String skm  = GestoreView.readLine("Nuovi km      [" + oldT.getKm()              + "] > ");
                        String snu  = GestoreView.readLine("Nuovi treni   [" + oldT.getNumTreniOperativi()+ "] > ");

                        Tratta newT = new Tratta(
                                ndSt.isEmpty() ? oldT.getDepNomeStazione() : ndSt,
                                ndCi.isEmpty() ? oldT.getDepCitta()        : ndCi,
                                ndPr.isEmpty() ? oldT.getDepProvincia()    : ndPr,
                                naSt.isEmpty() ? oldT.getArrNomeStazione() : naSt,
                                naCi.isEmpty() ? oldT.getArrCitta()        : naCi,
                                naPr.isEmpty() ? oldT.getArrProvincia()    : naPr,
                                skm.isEmpty() ? oldT.getKm()               : Integer.parseInt(skm),
                                snu.isEmpty() ? oldT.getNumTreniOperativi(): Integer.parseInt(snu)
                        );
                        trattaDAO.update(oldT, newT);
                        GestoreView.showMessage("Tratta aggiornata.");
                        break;

                    case "4":
                        list = trattaDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessuna tratta da eliminare."); break; }
                        GestoreView.showIndexedList(list);
                        int iDel = GestoreView.readInt("Seleziona da eliminare > ", 1, list.size()) - 1;
                        trattaDAO.delete(list.get(iDel));
                        GestoreView.showMessage("Tratta eliminata.");
                        break;

                    case "5":
                        back = true;
                        break;

                    default:
                        GestoreView.showError("Opzione non valida.");
                }
            }
        } catch (DAOException e) {
            GestoreView.showError(e.getMessage());
        }
    }

    /* ===================== TRENI ===================== */
    private void gestioneTreni() {
        try {
            boolean back = false;
            while (!back) {
                System.out.println("\n--- Gestione Treni ---");
                System.out.println("1) Elenca");
                System.out.println("2) Aggiungi");
                System.out.println("3) Modifica");
                System.out.println("4) Elimina");
                System.out.println("5) Indietro");
                String scelta = GestoreView.readLine("Scelta > ");
                List<Treno> list;

                switch (scelta) {
                    case "1":
                        list = trenoDAO.listAllFull();
                        if (list.isEmpty()) GestoreView.showMessage("(nessun treno)");
                        GestoreView.showIndexedList(list);
                        break;

                    case "2":
                        Treno nuovo = new Treno(
                                GestoreView.readLine("Matricola (4 cifre)           > "),
                                GestoreView.readLine("Marca                         > "),
                                GestoreView.readLine("Modello                       > "),
                                LocalDate.parse(GestoreView.readLine("Data acquisto (YYYY-MM-DD)    > ")),
                                LocalTime.parse(GestoreView.readLine("Ora partenza (HH:MM:SS)       > ")),
                                LocalTime.parse(GestoreView.readLine("Ora arrivo   (HH:MM:SS)       > ")),
                                GestoreView.readLine("Dep. stazione                 > "),
                                GestoreView.readLine("Dep. città                    > "),
                                GestoreView.readLine("Dep. provincia (2 lettere)    > "),
                                GestoreView.readLine("Arr. stazione                 > "),
                                GestoreView.readLine("Arr. città                    > "),
                                GestoreView.readLine("Arr. provincia (2 lettere)    > ")
                        );
                        trenoDAO.insert(nuovo);
                        GestoreView.showMessage("Treno inserito.");
                        break;

                    case "3":
                        list = trenoDAO.listAllFull();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessun treno da modificare."); break; }
                        GestoreView.showIndexedList(list);
                        int idxMod = GestoreView.readInt("Seleziona treno da modificare > ", 1, list.size()) - 1;
                        Treno orig = list.get(idxMod);

                        // Matricola NON modificabile -> niente prompt
                        String nMa  = GestoreView.readLine(String.format("Marca      [%s] > ", orig.getMarca()));
                        String nMo  = GestoreView.readLine(String.format("Modello    [%s] > ", orig.getModello()));
                        String nDa  = GestoreView.readLine(String.format("Data acquisto [%s] > ", orig.getDataAcquisto()));
                        String nOp  = GestoreView.readLine(String.format("Ora partenza [%s] > ", orig.getOrarioPartenza()));
                        String nOa  = GestoreView.readLine(String.format("Ora arrivo   [%s] > ", orig.getOrarioArrivo()));
                        String nDpn = GestoreView.readLine(String.format("Dep. stazione [%s] > ", orig.getDepNomeStaz()));
                        String nDpc = GestoreView.readLine(String.format("Dep. città    [%s] > ", orig.getDepCitta()));
                        String nDpp = GestoreView.readLine(String.format("Dep. prov.    [%s] > ", orig.getDepProv()));
                        String nApn = GestoreView.readLine(String.format("Arr. stazione [%s] > ", orig.getArrNomeStaz()));
                        String nApc = GestoreView.readLine(String.format("Arr. città    [%s] > ", orig.getArrCitta()));
                        String nApp = GestoreView.readLine(String.format("Arr. prov.    [%s] > ", orig.getArrProv()));

                        Treno aggiornato = new Treno(
                                orig.getMatricola(),
                                nMa .isEmpty() ? orig.getMarca()         : nMa,
                                nMo .isEmpty() ? orig.getModello()       : nMo,
                                nDa .isEmpty() ? orig.getDataAcquisto()  : LocalDate.parse(nDa),
                                nOp .isEmpty() ? orig.getOrarioPartenza(): LocalTime.parse(nOp),
                                nOa .isEmpty() ? orig.getOrarioArrivo()  : LocalTime.parse(nOa),
                                nDpn.isEmpty() ? orig.getDepNomeStaz()   : nDpn,
                                nDpc.isEmpty() ? orig.getDepCitta()      : nDpc,
                                nDpp.isEmpty() ? orig.getDepProv()       : nDpp,
                                nApn.isEmpty() ? orig.getArrNomeStaz()   : nApn,
                                nApc.isEmpty() ? orig.getArrCitta()      : nApc,
                                nApp.isEmpty() ? orig.getArrProv()       : nApp
                        );
                        trenoDAO.update(aggiornato);
                        GestoreView.showMessage("Treno aggiornato.");
                        break;

                    case "4":
                        list = trenoDAO.listAllFull();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessun treno da eliminare."); break; }
                        GestoreView.showIndexedList(list);
                        int idxDel = GestoreView.readInt("Seleziona treno da eliminare > ", 1, list.size()) - 1;
                        trenoDAO.delete(list.get(idxDel).getMatricola());
                        GestoreView.showMessage("Treno eliminato.");
                        break;

                    case "5":
                        back = true;
                        break;

                    default:
                        GestoreView.showError("Opzione non valida.");
                }
            }
        } catch (DAOException e) {
            GestoreView.showError("Errore gestione treni: " + e.getMessage());
        }
    }

    /* ===================== TURNI ===================== */
    private void gestioneTurni() {
        try {
            boolean back = false;
            while (!back) {
                System.out.println("\n--- Gestione Turni ---");
                System.out.println("1) Elenca");
                System.out.println("2) Aggiungi");
                System.out.println("3) Modifica");
                System.out.println("4) Elimina");
                System.out.println("5) Indietro");
                String scelta = GestoreView.readLine("Scelta > ");
                List<Turno> list;

                switch (scelta) {
                    case "1":
                        list = turnoDAO.listAll();
                        if (list.isEmpty()) GestoreView.showMessage("(nessun turno)");
                        GestoreView.showIndexedList(list);
                        break;

                    case "2":
                        Turno nuovo = new Turno(
                                GestoreView.readLine("Nome                         > "),
                                GestoreView.readLine("Cognome                      > "),
                                LocalDate.parse(GestoreView.readLine("Data servizio (YYYY-MM-DD)  > ")),
                                LocalTime.parse(GestoreView.readLine("Ora inizio   (HH:MM:SS)     > ")),
                                LocalTime.parse(GestoreView.readLine("Ora fine     (HH:MM:SS)     > ")),
                                GestoreView.readLine("Matricola treno (4 cifre)    > "),
                                GestoreView.readLine("Marca treno                  > "),
                                GestoreView.readLine("Modello treno                > ")
                        );
                        turnoDAO.insert(nuovo);
                        GestoreView.showMessage("Turno inserito.");
                        break;

                    case "3":
                        list = turnoDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessun turno da modificare."); break; }
                        GestoreView.showIndexedList(list);
                        int idxMod = GestoreView.readInt("Seleziona turno da modificare > ", 1, list.size()) - 1;
                        Turno orig = list.get(idxMod);

                        String nNome = GestoreView.readLine(String.format("Nome     [%s] > ", orig.getNome()));
                        String nCog  = GestoreView.readLine(String.format("Cognome  [%s] > ", orig.getCognome()));
                        String nData = GestoreView.readLine(String.format("Data     [%s] > ", orig.getDataServ()));
                        String nIni  = GestoreView.readLine(String.format("Ora inizio [%s] > ", orig.getOraInizio()));
                        String nFin  = GestoreView.readLine(String.format("Ora fine   [%s] > ", orig.getOraFine()));
                        String nMat  = GestoreView.readLine(String.format("Matricola [%s] > ", orig.getMatricola()));
                        String nMar  = GestoreView.readLine(String.format("Marca     [%s] > ", orig.getMarca()));
                        String nMod  = GestoreView.readLine(String.format("Modello   [%s] > ", orig.getModello()));

                        Turno aggiornato = new Turno(
                                nNome.isEmpty() ? orig.getNome()       : nNome,
                                nCog .isEmpty() ? orig.getCognome()     : nCog,
                                nData.isEmpty() ? orig.getDataServ()    : LocalDate.parse(nData),
                                nIni .isEmpty() ? orig.getOraInizio()   : LocalTime.parse(nIni),
                                nFin .isEmpty() ? orig.getOraFine()     : LocalTime.parse(nFin),
                                nMat .isEmpty() ? orig.getMatricola()   : nMat,
                                nMar .isEmpty() ? orig.getMarca()       : nMar,
                                nMod .isEmpty() ? orig.getModello()     : nMod
                        );
                        turnoDAO.update(orig, aggiornato);
                        GestoreView.showMessage("Turno aggiornato.");
                        break;

                    case "4":
                        list = turnoDAO.listAll();
                        if (list.isEmpty()) { GestoreView.showMessage("Nessun turno da eliminare."); break; }
                        GestoreView.showIndexedList(list);
                        int idxDel = GestoreView.readInt("Seleziona turno da eliminare > ", 1, list.size()) - 1;
                        turnoDAO.delete(list.get(idxDel));
                        GestoreView.showMessage("Turno eliminato.");
                        break;

                    case "5":
                        back = true;
                        break;

                    default:
                        GestoreView.showError("Opzione non valida.");
                }
            }
        } catch (DAOException e) {
            GestoreView.showError("Errore gestione turni: " + e.getMessage());
        }
    }
}
