package view;

import controller.ClienteController;
import domain.Prenotazione;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClienteView {
    private final Scanner in = new Scanner(System.in);
    private final ClienteController ctrl = new ClienteController();

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Menu Cliente --");
            System.out.println("1) Effettua prenotazione");
            System.out.println("2) Le mie prenotazioni");
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
            // Tratte
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

            // Treni
            List<Treno> treni = ctrl.listTreniByTratta(tratta);
            if (treni.isEmpty()) {
                System.out.println("Nessun treno per la tratta.");
                return;
            }
            for (int i = 0; i < treni.size(); i++) {
                System.out.println((i + 1) + ") " + treni.get(i));
            }
            System.out.print("Seleziona treno: ");
            Treno treno = treni.get(Integer.parseInt(in.nextLine()) - 1);

            // Data viaggio
            System.out.print("Data viaggio (YYYY-MM-DD): ");
            LocalDate dataViaggio = LocalDate.parse(in.nextLine());

            // Classe -> carrozze
            System.out.print("Classe (1A/2A): ");
            String classe = in.nextLine();
            List<Integer> vagoni = ctrl.listVagoni(treno.getMatricola(), classe);
            if (vagoni.isEmpty()) {
                System.out.println("Nessun vagone disponibile.");
                return;
            }
            for (int i = 0; i < vagoni.size(); i++) {
                System.out.println((i + 1) + ") Carrozza " + vagoni.get(i));
            }
            System.out.print("Seleziona carrozza: ");
            int nCarrozza = vagoni.get(Integer.parseInt(in.nextLine()) - 1);

            // Posti liberi
            List<String> posti = ctrl.listPostiLiberi(treno.getMatricola(), nCarrozza, classe, dataViaggio);
            if (posti.isEmpty()) {
                System.out.println("Nessun posto libero.");
                return;
            }
            for (int i = 0; i < posti.size(); i++) {
                System.out.println((i + 1) + ") Posto " + posti.get(i));
            }
            System.out.print("Seleziona posto: ");
            String nPosto = posti.get(Integer.parseInt(in.nextLine()) - 1);

            // Dati acquirente
            System.out.print("Codice fiscale acquirente: ");
            String cf = in.nextLine();
            System.out.print("Data di nascita (YYYY-MM-DD): ");
            LocalDate dn = LocalDate.parse(in.nextLine());
            System.out.print("Carta di credito: ");
            String carta = in.nextLine();

            System.out.print("Confermi? (s/n): ");
            if (!in.nextLine().equalsIgnoreCase("s")) return;

            // Inserimento (SP calcola classe/prezzo e li restituisce)
            Prenotazione richiesta = new Prenotazione.Builder()
                    .withDataViaggio(dataViaggio)
                    .withMatricola(treno.getMatricola())
                    .withMarca(treno.getMarca())
                    .withModello(treno.getModello())
                    .withNCarrozza(nCarrozza)
                    .withNumeroPosto(nPosto)
                    .withDepNomeStazione(tratta.getDepNomeStazione())
                    .withArrNomeStazione(tratta.getArrNomeStazione())
                    .withCfAcquirente(cf)
                    .withDataNascitaAcquirente(dn)
                    .withCartaCredito(carta)
                    .build();

            Prenotazione confermata = ctrl.creaPrenotazione(richiesta);
            System.out.println("Prenotazione effettuata! Classe: " + confermata.getNomeClasse()
                    + ", Prezzo: " + confermata.getPrezzo());
        } catch (IllegalArgumentException e) {
            System.out.println("Input non valido: " + e.getMessage());
        } catch (DAOException e) {
            System.out.println("Errore prenotazione: " + e.getMessage());
        }
    }

    private void miePrenotazioni() {
        System.out.print("Codice fiscale: ");
        String cf = in.nextLine();
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
}
