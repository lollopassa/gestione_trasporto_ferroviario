package domain;

import java.util.Objects;

public class Stazione {
    private final String nomeStazione;
    private final String citta;
    private final String provincia;

    public Stazione(String nomeStazione, String citta, String provincia) {
        this.nomeStazione = nomeStazione;
        this.citta = citta;
        this.provincia = provincia;
    }

    public String getNomeStazione() { return nomeStazione; }
    public String getCitta() { return citta; }
    public String getProvincia() { return provincia; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stazione)) return false;
        Stazione s = (Stazione) o;
        return Objects.equals(nomeStazione, s.nomeStazione);
    }
    @Override public int hashCode() { return Objects.hash(nomeStazione); }
    @Override public String toString() { return nomeStazione + " (" + citta + " - " + provincia + ")"; }
}
