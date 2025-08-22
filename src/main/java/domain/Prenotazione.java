package domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Prenotazione {

    // Chiave e riferimenti viaggio/posto
    private final String codicePrenotazione;   // CHAR(36) UUID
    private final LocalDate dataViaggio;
    private final String idTreno;
    private final String marca;
    private final String modello;
    private final int nCarrozza;
    private final String numeroPosto;          // n_posto
    private final String depNomeStazione;
    private final String arrNomeStazione;

    // Intestatario del biglietto (VIAGGIATORE)
    private final String cfViaggiatore;        // cf_viaggiatore
    private final String nomeViaggiatore;      // nome_viagg
    private final String cognomeViaggiatore;   // cognome_viagg
    private final LocalDate dataNascitaViaggiatore; // data_nascita_viagg

    // Dati ACQUIRENTE (solo CF + carta)
    private final String cfAcquirente;         // cf_acq
    private final String cartaCredito;         // carta_credito

    // Dati calcolati/di supporto (non persistiti in tabella prenotazione)
    private final String nomeClasse;           // dalla carrozza
    private final BigDecimal prezzo;           // da fn_prezzo_dinamico

    private Prenotazione(Builder b) {
        this.codicePrenotazione     = b.codicePrenotazione;
        this.dataViaggio            = b.dataViaggio;
        this.idTreno                = b.idTreno;
        this.marca                  = b.marca;
        this.modello                = b.modello;
        this.nCarrozza              = b.nCarrozza;
        this.numeroPosto            = b.numeroPosto;
        this.depNomeStazione        = b.depNomeStazione;
        this.arrNomeStazione        = b.arrNomeStazione;
        this.cfViaggiatore          = b.cfViaggiatore;
        this.nomeViaggiatore        = b.nomeViaggiatore;
        this.cognomeViaggiatore     = b.cognomeViaggiatore;
        this.dataNascitaViaggiatore = b.dataNascitaViaggiatore;
        this.cfAcquirente           = b.cfAcquirente;
        this.cartaCredito           = b.cartaCredito;
        this.nomeClasse             = b.nomeClasse;
        this.prezzo                 = b.prezzo;
    }

    // Getters
    public String getCodicePrenotazione() { return codicePrenotazione; }
    public LocalDate getDataViaggio() { return dataViaggio; }
    public String getIdTreno() { return idTreno; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }
    public int getNCarrozza() { return nCarrozza; }
    public String getNumeroPosto() { return numeroPosto; }
    public String getDepNomeStazione() { return depNomeStazione; }
    public String getArrNomeStazione() { return arrNomeStazione; }

    public String getCfViaggiatore() { return cfViaggiatore; }
    public String getNomeViaggiatore() { return nomeViaggiatore; }
    public String getCognomeViaggiatore() { return cognomeViaggiatore; }
    public LocalDate getDataNascitaViaggiatore() { return dataNascitaViaggiatore; }

    public String getCfAcquirente() { return cfAcquirente; }
    public String getCartaCredito() { return cartaCredito; }

    public String getNomeClasse() { return nomeClasse; }
    public BigDecimal getPrezzo() { return prezzo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prenotazione)) return false;
        Prenotazione that = (Prenotazione) o;
        return Objects.equals(codicePrenotazione, that.codicePrenotazione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePrenotazione);
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "codice=" + codicePrenotazione +
                ", " + idTreno + " " + nCarrozza + "/" + numeroPosto +
                " " + depNomeStazione + "→" + arrNomeStazione +
                " " + dataViaggio +
                ", cfViaggiatore=" + cfViaggiatore +
                ", classe=" + nomeClasse +
                ", prezzo=" + prezzo +
                "}";
    }

    // Builder
    public static class Builder {
        private String codicePrenotazione;
        private LocalDate dataViaggio;
        private String idTreno;
        private String marca;
        private String modello;
        private int nCarrozza;
        private String numeroPosto;
        private String depNomeStazione;
        private String arrNomeStazione;

        private String cfViaggiatore;
        private String nomeViaggiatore;
        private String cognomeViaggiatore;
        private LocalDate dataNascitaViaggiatore;

        private String cfAcquirente;
        private String cartaCredito;

        private String nomeClasse;
        private BigDecimal prezzo;

        public Builder withCodicePrenotazione(String v) { this.codicePrenotazione = v; return this; }
        public Builder withDataViaggio(LocalDate v) { this.dataViaggio = v; return this; }
        public Builder withIdTreno(String v) { this.idTreno = v; return this; }
        public Builder withMarca(String v) { this.marca = v; return this; }
        public Builder withModello(String v) { this.modello = v; return this; }
        public Builder withNCarrozza(int v) { this.nCarrozza = v; return this; }
        public Builder withNumeroPosto(String v) { this.numeroPosto = v; return this; }
        public Builder withDepNomeStazione(String v) { this.depNomeStazione = v; return this; }
        public Builder withArrNomeStazione(String v) { this.arrNomeStazione = v; return this; }

        public Builder withCfViaggiatore(String v) { this.cfViaggiatore = v; return this; }
        public Builder withNomeViaggiatore(String v) { this.nomeViaggiatore = v; return this; }
        public Builder withCognomeViaggiatore(String v) { this.cognomeViaggiatore = v; return this; }
        public Builder withDataNascitaViaggiatore(LocalDate v) { this.dataNascitaViaggiatore = v; return this; }

        public Builder withCfAcquirente(String v) { this.cfAcquirente = v; return this; }
        public Builder withCartaCredito(String v) { this.cartaCredito = v; return this; }

        public Builder withNomeClasse(String v) { this.nomeClasse = v; return this; }
        public Builder withPrezzo(BigDecimal v) { this.prezzo = v; return this; }

        public Prenotazione build() { return new Prenotazione(this); }
    }
}
