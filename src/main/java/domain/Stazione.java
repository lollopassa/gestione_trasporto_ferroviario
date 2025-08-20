package domain;

public class Stazione {
    private String nomeStazione;
    private String citta;
    private String provincia;   // sigla a 2 lettere

    public Stazione() {}
    public Stazione(String nomeStazione, String citta, String provincia) {
        this.nomeStazione = nomeStazione;
        this.citta = citta;
        this.provincia = provincia;
    }

    public String getNomeStazione() { return nomeStazione; }
    public String getCitta()        { return citta; }
    public String getProvincia()    { return provincia; }

    public void setNomeStazione(String n) { this.nomeStazione = n; }
    public void setCitta(String c)        { this.citta = c; }
    public void setProvincia(String p)    { this.provincia = p; }

    @Override public String toString() { return nomeStazione + " (" + citta + ")"; }
}
