package controller;

import dao.PostoDAO;
import dao.PrenotazioneDAO;
import dao.TrattaDAO;
import dao.TrenoDAO;
import domain.Prenotazione;
import domain.Tratta;
import domain.Treno;
import exception.DAOException;
import view.ClienteView;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import utility.DBConnection;

public class ClienteController implements Controller {

    private final Scanner         scanner     = new Scanner(System.in);
    private final TrattaDAO       trattaDAO    = new TrattaDAO();
    private final TrenoDAO        trenoDAO     = new TrenoDAO();
    private final PostoDAO        postoDAO     = new PostoDAO();
    private final PrenotazioneDAO prenDAO      = new PrenotazioneDAO();

    @Override
    public void start(String username) throws DAOException {
        ClienteView.menu(username);
    }

    public void effettuaPrenotazione() throws DAOException {
        // 1) Dati acquirente
        System.out.print("Nome acquirente > ");
        String nomeAcq = scanner.nextLine().trim();
        System.out.print("Cognome acquirente > ");
        String cognAcq = scanner.nextLine().trim();
        System.out.print("Data di nascita (YYYY-MM-DD) > ");
        LocalDate nascita = LocalDate.parse(scanner.nextLine());
        System.out.print("Codice fiscale > ");
        String cfAcq = scanner.nextLine().trim();
        System.out.print("Carta di credito (16 cifre) > ");
        String carta = scanner.nextLine().replaceAll("\\s+", "");

        // 2) Scegli tratta
        List<Tratta> tratte = trattaDAO.listAll();
        Tratta t = scegli("tratta", tratte);

        // 3) Scegli treno
        List<Treno> treni = trenoDAO.listByTratta(t);
        Treno treno = scegli("treno", treni);

        // 4) Data viaggio e classe
        System.out.print("Data viaggio (YYYY-MM-DD) > ");
        LocalDate dataViaggio = LocalDate.parse(scanner.nextLine());
        System.out.print("Classe (PRIMA/SECONDA) > ");
        String cls = scanner.nextLine().trim().toUpperCase();

        // 5) Scegli vagone
        List<Integer> vagoni = trenoDAO.vagoni(treno.getMatricola(), cls);
        int nCarrozza = scegli("vagone (classe " + cls + ")", vagoni);

        // 6) Scegli posto
        List<String> posti = postoDAO.postiLiberi(
                treno.getMatricola(), nCarrozza, cls, dataViaggio);
        if (posti.isEmpty()) {
            throw new DAOException("Nessun posto libero nel vagone " + nCarrozza);
        }
        String nPosto = scegli("posto", posti);

        // 7) Calcola prezzo dinamico (solo per mostrare all’utente)
        BigDecimal prezzo = calcolaPrezzo(t, cls);

        System.out.println("Prezzo del biglietto: €" + prezzo);
        System.out.print("Confermi prenotazione? (S/N) > ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
            System.out.println("Operazione annullata.");
            return;
        }

        // 8) Costruisci domain e invochi DAO
        Prenotazione p = new Prenotazione.Builder()
                .withNomeAcquirente(nomeAcq)
                .withCognomeAcquirente(cognAcq)
                .withDataNascitaAcquirente(nascita)
                .withCfAcquirente(cfAcq)
                .withCartaCredito(carta)
                .withMatricola(treno.getMatricola())
                .withMarca(treno.getMarca())
                .withModello(treno.getModello())
                .withClasse(cls)
                .withNCarrozza(nCarrozza)
                .withNumeroPosto(nPosto)
                .withDepStazione(t.getDepNomeStazione())
                .withDepCitta(t.getDepCitta())
                .withDepProvincia(t.getDepProvincia())
                .withArrStazione(t.getArrNomeStazione())
                .withArrCitta(t.getArrCitta())
                .withArrProvincia(t.getArrProvincia())
                .withDataViaggio(dataViaggio)
                .withPrezzo(prezzo)
                .build();

        try {
            prenDAO.inserisci(p);
            System.out.println("✅ Prenotazione effettuata con successo!");
        } catch (DAOException e) {
            System.out.println("❌ Impossibile effettuare la prenotazione: " + e.getMessage());
        }
    }

    public void visualizzaPrenotazioni() throws DAOException {
        System.out.print("Nome cliente > ");
        String nome = scanner.nextLine().trim();
        System.out.print("Cognome cliente > ");
        String cognome = scanner.nextLine().trim();

        List<Prenotazione> list = prenDAO.byCliente(nome, cognome);
        if (list.isEmpty()) {
            System.out.println("❌ Nessuna prenotazione trovata.");
            return;
        }

        System.out.println("\n-- Le mie prenotazioni --");
        for (Prenotazione p : list) {
            System.out.println("────────────────────────────────────────");
            System.out.println("Cliente          : " +
                    p.getNomeAcquirente() + " " + p.getCognomeAcquirente());
            System.out.println("Data di nascita  : " + p.getDataNascitaAcquirente());
            System.out.println("Codice fiscale   : " + p.getCfAcquirente());
            System.out.println("Carta di credito : " + p.getCartaCredito());
            System.out.println();
            System.out.println("Tratta           : " +
                    p.getDepStazione() + " → " + p.getArrStazione());
            System.out.println("Data viaggio     : " + p.getDataViaggio());
            System.out.println("Prezzo           : €" + p.getPrezzo());
            System.out.println("Treno            : " +
                    p.getMatricola() + " (" + p.getMarca() + " " + p.getModello() + ")");
            System.out.println("Classe           : " + p.getClasse());
            System.out.println("Carrozza / Posto : " +
                    p.getNCarrozza() + " / " + p.getNumeroPosto());
        }
        System.out.println("────────────────────────────────────────");
    }

    // --- metodi di utilità per il menu a scelta ---
    private <T> T scegli(String label, List<T> items) {
        while (true) {
            System.out.println("-- Scegli " + label + " --");
            for (int i = 0; i < items.size(); i++) {
                System.out.printf("%2d) %s%n", i+1, items.get(i));
            }
            String in = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(in) - 1;
                if (idx >= 0 && idx < items.size()) {
                    return items.get(idx);
                }
            } catch (NumberFormatException ignored) {}
            System.out.println("Scelta non valida, riprova.");
        }
    }

    private BigDecimal calcolaPrezzo(Tratta t, String cls) throws DAOException {
        String CALC = "SELECT fn_prezzo_dinamico(?, ?, ?, ?, ?, ?, ?) AS prezzo";
        try (Connection conn = DBConnection.getConnection("clienteuser.properties");
             PreparedStatement ps = conn.prepareStatement(CALC)) {
            ps.setString(1, t.getDepNomeStazione());
            ps.setString(2, t.getDepCitta());
            ps.setString(3, t.getDepProvincia());
            ps.setString(4, t.getArrNomeStazione());
            ps.setString(5, t.getArrCitta());
            ps.setString(6, t.getArrProvincia());
            ps.setString(7, cls);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("prezzo");
                }
                throw new DAOException("Errore nel calcolo del prezzo");
            }
        } catch (SQLException e) {
            throw new DAOException("Errore calcolo prezzo: " + e.getMessage(), e);
        }
    }
}
