package domain;

import java.util.Objects;

public class Tratta {
    private final String depNomeStazione;
    private final String arrNomeStazione;
    private final int km;
    private final int numTreniOperativi;

    public Tratta(String depNomeStazione, String arrNomeStazione, int km, int numTreniOperativi) {
        this.depNomeStazione = depNomeStazione;
        this.arrNomeStazione = arrNomeStazione;
        this.km = km;
        this.numTreniOperativi = numTreniOperativi;
    }

    public String getDepNomeStazione() { return depNomeStazione; }
    public String getArrNomeStazione() { return arrNomeStazione; }
    public int getKm() { return km; }
    public int getNumTreniOperativi() { return numTreniOperativi; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tratta)) return false;
        Tratta t = (Tratta) o;
        return Objects.equals(depNomeStazione, t.depNomeStazione) &&
                Objects.equals(arrNomeStazione, t.arrNomeStazione);
    }
    @Override public int hashCode() { return Objects.hash(depNomeStazione, arrNomeStazione); }
    @Override public String toString() { return depNomeStazione + " → " + arrNomeStazione; }
}
