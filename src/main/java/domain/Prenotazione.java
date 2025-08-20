package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Prenotazione {
    private final long       idPrenotazione;
    private final String     nomeAcquirente;
    private final String     cognomeAcquirente;
    private final LocalDate  dataNascitaAcquirente;
    private final String     cfAcquirente;
    private final String     cartaCredito;
    private final String     matricola;
    private final String     marca;
    private final String     modello;
    private final String     classe;               // PRIMA | SECONDA
    private final int        nCarrozza;
    private final String     numeroPosto;
    private final String     depStazione;
    private final String     depCitta;
    private final String     depProvincia;
    private final String     arrStazione;
    private final String     arrCitta;
    private final String     arrProvincia;
    private final LocalDate  dataViaggio;
    private final BigDecimal prezzo;

    private Prenotazione(Builder b) {
        this.idPrenotazione        = b.idPrenotazione;
        this.nomeAcquirente        = b.nomeAcquirente;
        this.cognomeAcquirente     = b.cognomeAcquirente;
        this.dataNascitaAcquirente = b.dataNascitaAcquirente;
        this.cfAcquirente          = b.cfAcquirente;
        this.cartaCredito          = b.cartaCredito;
        this.matricola             = b.matricola;
        this.marca                 = b.marca;
        this.modello               = b.modello;
        this.classe                = b.classe;
        this.nCarrozza             = b.nCarrozza;
        this.numeroPosto           = b.numeroPosto;
        this.depStazione           = b.depStazione;
        this.depCitta              = b.depCitta;
        this.depProvincia          = b.depProvincia;
        this.arrStazione           = b.arrStazione;
        this.arrCitta              = b.arrCitta;
        this.arrProvincia          = b.arrProvincia;
        this.dataViaggio           = b.dataViaggio;
        this.prezzo                = b.prezzo;
    }

    public static class Builder {
        private long       idPrenotazione = 0;
        private String     nomeAcquirente;
        private String     cognomeAcquirente;
        private LocalDate  dataNascitaAcquirente;
        private String     cfAcquirente;
        private String     cartaCredito;
        private String     matricola;
        private String     marca;
        private String     modello;
        private String     classe;
        private int        nCarrozza;
        private String     numeroPosto;
        private String     depStazione;
        private String     depCitta;
        private String     depProvincia;
        private String     arrStazione;
        private String     arrCitta;
        private String     arrProvincia;
        private LocalDate  dataViaggio;
        private BigDecimal prezzo;

        public Builder idPrenotazione(long id) { this.idPrenotazione = id; return this; }
        public Builder withNomeAcquirente(String n)        { this.nomeAcquirente = n; return this; }
        public Builder withCognomeAcquirente(String c)     { this.cognomeAcquirente = c; return this; }
        public Builder withDataNascitaAcquirente(LocalDate d) { this.dataNascitaAcquirente = d; return this; }
        public Builder withCfAcquirente(String cf)         { this.cfAcquirente = cf; return this; }
        public Builder withCartaCredito(String cc)         { this.cartaCredito = cc; return this; }
        public Builder withMatricola(String m)             { this.matricola = m; return this; }
        public Builder withMarca(String m)                 { this.marca = m; return this; }
        public Builder withModello(String m)               { this.modello = m; return this; }
        public Builder withClasse(String cl)               { this.classe = cl; return this; }
        public Builder withNCarrozza(int n)                { this.nCarrozza = n; return this; }
        public Builder withNumeroPosto(String np)          { this.numeroPosto = np; return this; }
        public Builder withDepStazione(String s)           { this.depStazione = s; return this; }
        public Builder withDepCitta(String c)              { this.depCitta = c; return this; }
        public Builder withDepProvincia(String p)          { this.depProvincia = p; return this; }
        public Builder withArrStazione(String s)           { this.arrStazione = s; return this; }
        public Builder withArrCitta(String c)              { this.arrCitta = c; return this; }
        public Builder withArrProvincia(String p)          { this.arrProvincia = p; return this; }
        public Builder withDataViaggio(LocalDate d)        { this.dataViaggio = d; return this; }
        public Builder withPrezzo(BigDecimal p)            { this.prezzo = p; return this; }
        public Prenotazione build()                       { return new Prenotazione(this); }
    }

    // getters ...
    public long       getIdPrenotazione()             { return idPrenotazione; }
    public String     getNomeAcquirente()             { return nomeAcquirente; }
    public String     getCognomeAcquirente()          { return cognomeAcquirente; }
    public LocalDate  getDataNascitaAcquirente()      { return dataNascitaAcquirente; }
    public String     getCfAcquirente()               { return cfAcquirente; }
    public String     getCartaCredito()               { return cartaCredito; }
    public String     getMatricola()                  { return matricola; }
    public String     getMarca()                      { return marca; }
    public String     getModello()                    { return modello; }
    public String     getClasse()                     { return classe; }
    public int        getNCarrozza()                  { return nCarrozza; }
    public String     getNumeroPosto()                { return numeroPosto; }
    public String     getDepStazione()                { return depStazione; }
    public String     getDepCitta()                   { return depCitta; }
    public String     getDepProvincia()               { return depProvincia; }
    public String     getArrStazione()                { return arrStazione; }
    public String     getArrCitta()                   { return arrCitta; }
    public String     getArrProvincia()               { return arrProvincia; }
    public LocalDate  getDataViaggio()                { return dataViaggio; }
    public BigDecimal getPrezzo()                    { return prezzo; }
}
