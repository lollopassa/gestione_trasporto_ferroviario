package domain;

public class Stazione {
    private String nomeStazione;
    private String citta;
    private String provincia;

    public Stazione() {}

    public Stazione(String nomeStazione, String citta, String provincia) {
        this.nomeStazione = nomeStazione;
        this.citta        = citta;
        this.provincia    = provincia;
    }

    public String getNomeStazione() { return nomeStazione; }
    public void setNomeStazione(String nomeStazione) { this.nomeStazione = nomeStazione; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    @Override
    public String toString() {
        return nomeStazione + (citta != null && !citta.isEmpty() ? ", " + citta : "");
    }
}
