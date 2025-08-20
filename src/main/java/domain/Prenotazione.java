package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Prenotazione {
    private final String     codicePrenotazione;
    private final LocalDate  dataViaggio;
    private final String     matricola;
    private final String     marca;
    private final String     modello;
    private final int        nCarrozza;
    private final String     numeroPosto;
    private final String     depNomeStazione;
    private final String     arrNomeStazione;
    private final String     cfAcquirente;
    private final LocalDate  dataNascitaAcquirente;
    private final String     cartaCredito;
    private final String     nomeClasse;
    private final BigDecimal prezzo;

    private Prenotazione(Builder b) {
        this.codicePrenotazione    = b.codicePrenotazione;
        this.dataViaggio           = b.dataViaggio;
        this.matricola             = b.matricola;
        this.marca                 = b.marca;
        this.modello               = b.modello;
        this.nCarrozza             = b.nCarrozza;
        this.numeroPosto           = b.numeroPosto;
        this.depNomeStazione       = b.depNomeStazione;
        this.arrNomeStazione       = b.arrNomeStazione;
        this.cfAcquirente          = b.cfAcquirente;
        this.dataNascitaAcquirente = b.dataNascitaAcquirente;
        this.cartaCredito          = b.cartaCredito;
        this.nomeClasse            = b.nomeClasse;
        this.prezzo                = b.prezzo;
    }

    public static class Builder {
        private String     codicePrenotazione;
        private LocalDate  dataViaggio;
        private String     matricola;
        private String     marca;
        private String     modello;
        private int        nCarrozza;
        private String     numeroPosto;
        private String     depNomeStazione;
        private String     arrNomeStazione;
        private String     cfAcquirente;
        private LocalDate  dataNascitaAcquirente;
        private String     cartaCredito;
        private String     nomeClasse;
        private BigDecimal prezzo;

        public Builder withCodicePrenotazione(String codicePrenotazione) {
            this.codicePrenotazione = codicePrenotazione; return this;
        }
        public Builder withDataViaggio(LocalDate dataViaggio) {
            this.dataViaggio = dataViaggio; return this;
        }
        public Builder withMatricola(String matricola) {
            this.matricola = matricola; return this;
        }
        public Builder withMarca(String marca) {
            this.marca = marca; return this;
        }
        public Builder withModello(String modello) {
            this.modello = modello; return this;
        }
        public Builder withNCarrozza(int nCarrozza) {
            this.nCarrozza = nCarrozza; return this;
        }
        public Builder withNumeroPosto(String numeroPosto) {
            this.numeroPosto = numeroPosto; return this;
        }
        public Builder withDepNomeStazione(String depNomeStazione) {
            this.depNomeStazione = depNomeStazione; return this;
        }
        public Builder withArrNomeStazione(String arrNomeStazione) {
            this.arrNomeStazione = arrNomeStazione; return this;
        }
        public Builder withCfAcquirente(String cfAcquirente) {
            this.cfAcquirente = cfAcquirente; return this;
        }
        public Builder withDataNascitaAcquirente(LocalDate dataNascitaAcquirente) {
            this.dataNascitaAcquirente = dataNascitaAcquirente; return this;
        }
        public Builder withCartaCredito(String cartaCredito) {
            this.cartaCredito = cartaCredito; return this;
        }
        public Builder withNomeClasse(String nomeClasse) {
            this.nomeClasse = nomeClasse; return this;
        }
        public Builder withPrezzo(BigDecimal prezzo) {
            this.prezzo = prezzo; return this;
        }
        public Prenotazione build() {
            return new Prenotazione(this);
        }
    }

    public String getCodicePrenotazione() { return codicePrenotazione; }
    public LocalDate getDataViaggio() { return dataViaggio; }
    public String getMatricola() { return matricola; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }
    public int getNCarrozza() { return nCarrozza; }
    public String getNumeroPosto() { return numeroPosto; }
    public String getDepNomeStazione() { return depNomeStazione; }
    public String getArrNomeStazione() { return arrNomeStazione; }
    public String getCfAcquirente() { return cfAcquirente; }
    public LocalDate getDataNascitaAcquirente() { return dataNascitaAcquirente; }
    public String getCartaCredito() { return cartaCredito; }
    public String getNomeClasse() { return nomeClasse; }
    public BigDecimal getPrezzo() { return prezzo; }
}
