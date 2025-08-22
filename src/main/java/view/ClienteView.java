package view;

import controller.ClienteController;
import domain.Classe;
import domain.Prenotazione;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClienteView {
    private final Scanner in = new Scanner(System.in);
    private final ClienteController ctrl;

    /** Passa qui lo username dell'utente loggato (es.: "mario"). */
    public ClienteView(String loggedUsername) throws DAOException {
        this.ctrl = ClienteController.forUser(loggedUsername);
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Menu Cliente --");
            System.out.println("1) Effettua prenotazione");
            System.out.println("2) I miei biglietti (per CF viaggiatore)");
            System.out.println("3) Indietro");
            System.out.print("Scelta: ");
            String s = in.nextLine();

            switch (s) {
                case "1":
                    nuovaPrenotazione();
                    break;
                case "2":
                    miePrenotazioni();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    private void nuovaPrenotazione() {
        try {
            // 1) Selezione tratta
            List<Tratta> tratte = ctrl.listTratte();
            if (tratte.isEmpty()) {
                System.out.println("Nessuna tratta disponibile.");
                return;
            }
            for (int i = 0; i < tratte.size(); i++) {
                System.out.println((i + 1) + ") " + tratte.get(i));
            }
            System.out.print("Seleziona tratta: ");
            Tratta tratta = tratte.get(Integer.parseInt(in.nextLine()) - 1);

            // 2) Selezione treno che copre la tratta
            List<Treno> treni = ctrl.listTreniByTratta(tratta);
            if (treni.isEmpty()) {
                System.out.println("Nessun treno per la tratta selezionata.");
                return;
            }
            for (int i = 0; i < treni.size(); i++) {
                System.out.println((i + 1) + ") " + treni.get(i));
            }
            System.out.print("Seleziona treno: ");
            Treno treno = treni.get(Integer.parseInt(in.nextLine()) - 1);

            // 3) Data viaggio
            System.out.print("Data viaggio (YYYY-MM-DD): ");
            LocalDate dataViaggio = LocalDate.parse(in.nextLine());

            // 4) Classe -> elenco carrozze di quella classe
            System.out.print("Classe (PRIMA/SECONDA oppure 1A/2A): ");
            String clIn = in.nextLine().trim().toUpperCase();
            Classe.NomeClasse nomeClasse = parseClasse(clIn);

            List<Integer> vagoni = ctrl.listVagoni(treno.getIdTreno(), nomeClasse.name());
            if (vagoni.isEmpty()) {
                System.out.println("Nessuna carrozza disponibile per quella classe.");
                return;
            }
            for (int i = 0; i < vagoni.size(); i++) {
                System.out.println((i + 1) + ") Carrozza " + vagoni.get(i));
            }
            System.out.print("Seleziona carrozza: ");
            int nCarrozza = vagoni.get(Integer.parseInt(in.nextLine()) - 1);

            // 5) Posti liberi nella carrozza/classe/giorno
            List<String> posti = ctrl.listPostiLiberi(treno.getIdTreno(), nCarrozza, nomeClasse.name(), dataViaggio);
            if (posti.isEmpty()) {
                System.out.println("Nessun posto libero per i criteri selezionati.");
                return;
            }
            for (int i = 0; i < posti.size(); i++) {
                System.out.println((i + 1) + ") Posto " + posti.get(i));
            }
            System.out.print("Seleziona posto: ");
            String nPosto = posti.get(Integer.parseInt(in.nextLine()) - 1);

            // 6) Dati VIAGGIATORE (intestatario biglietto)
            System.out.print("Codice fiscale VIAGGIATORE: ");
            String cfViaggiatore = in.nextLine().trim().toUpperCase();
            System.out.print("Nome VIAGGIATORE: ");
            String nomeViagg = in.nextLine().trim();
            System.out.print("Cognome VIAGGIATORE: ");
            String cognomeViagg = in.nextLine().trim();
            System.out.print("Data di nascita VIAGGIATORE (YYYY-MM-DD): ");
            LocalDate dnViagg = LocalDate.parse(in.nextLine());

            // 7) Dati ACQUIRENTE: CF automatico dallo username loggato; chiedi solo carta
            String cfAcq = ctrl.getCfAcquirente();
            if (cfAcq == null || cfAcq.isBlank()) {
                System.out.println("Impossibile procedere: nessun CF collegato al tuo account.");
                return;
            }
            System.out.print("Carta di credito (16 cifre, solo numeri): ");
            String carta = in.nextLine().replaceAll("\\s+", "");

            // Conferma
            System.out.print("Confermi? (s/n): ");
            if (!in.nextLine().equalsIgnoreCase("s")) return;

            // 8) Build richiesta coerente con DB/procedure
            Prenotazione richiesta = new Prenotazione.Builder()
                    .withDataViaggio(dataViaggio)
                    .withIdTreno(treno.getIdTreno())
                    .withMarca(treno.getMarca())
                    .withModello(treno.getModello())
                    .withNCarrozza(nCarrozza)
                    .withNumeroPosto(nPosto)
                    .withDepNomeStazione(tratta.getDepNomeStazione())
                    .withArrNomeStazione(tratta.getArrNomeStazione())
                    .withCfViaggiatore(cfViaggiatore)
                    .withNomeViaggiatore(nomeViagg)
                    .withCognomeViaggiatore(cognomeViagg)
                    .withDataNascitaViaggiatore(dnViagg)
                    .withCfAcquirente(cfAcq)          // auto dal login
                    .withCartaCredito(carta)
                    .build();

            // 9) Insert + lettura classe/prezzo calcolati lato DB
            Prenotazione confermata = ctrl.creaPrenotazione(richiesta);

            System.out.println("\nPrenotazione effettuata!");
            System.out.println("Classe: " + confermata.getNomeClasse());
            System.out.println("Prezzo: €" + confermata.getPrezzo());

        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore prenotazione: " + e.getMessage());
        }
    }

    private void miePrenotazioni() {
        System.out.print("Codice fiscale VIAGGIATORE: ");
        String cf = in.nextLine().trim().toUpperCase();
        try {
            List<Prenotazione> list = ctrl.getPrenotazioniCliente(cf);
            if (list.isEmpty()) {
                System.out.println("Nessuna prenotazione trovata.");
            } else {
                list.forEach(p -> System.out.println(
                        p.getCodicePrenotazione() + " | " + p.getDataViaggio() + " | " +
                                p.getDepNomeStazione() + " → " + p.getArrNomeStazione() + " | " +
                                "Carrozza " + p.getNCarrozza() + " Posto " + p.getNumeroPosto() + " | " +
                                p.getNomeClasse() + " | €" + p.getPrezzo()
                ));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore recupero prenotazioni: " + e.getMessage());
        }
    }

    private Classe.NomeClasse parseClasse(String raw) {
        switch (raw) {
            case "1A": return Classe.NomeClasse.PRIMA;
            case "2A": return Classe.NomeClasse.SECONDA;
            default:   return Classe.NomeClasse.valueOf(raw);
        }
    }
}
