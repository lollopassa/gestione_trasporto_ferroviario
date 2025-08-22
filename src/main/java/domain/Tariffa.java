package domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Tariffa {
    private final String marca;
    private final String modello;
    private final String nomeClasse; // PRIMA/SECONDA
    private final String depNomeStazione;
    private final String arrNomeStazione;
    private final BigDecimal prezzo;

    public Tariffa(String marca, String modello, String nomeClasse,
                   String depNomeStazione, String arrNomeStazione, BigDecimal prezzo) {
        this.marca = marca;
        this.modello = modello;
        this.nomeClasse = nomeClasse;
        this.depNomeStazione = depNomeStazione;
        this.arrNomeStazione = arrNomeStazione;
        this.prezzo = prezzo;
    }

    public String getMarca() { return marca; }
    public String getModello() { return modello; }
    public String getNomeClasse() { return nomeClasse; }
    public String getDepNomeStazione() { return depNomeStazione; }
    public String getArrNomeStazione() { return arrNomeStazione; }
    public BigDecimal getPrezzo() { return prezzo; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tariffa)) return false;
        Tariffa t = (Tariffa) o;
        return Objects.equals(marca, t.marca) &&
                Objects.equals(modello, t.modello) &&
                Objects.equals(nomeClasse, t.nomeClasse) &&
                Objects.equals(depNomeStazione, t.depNomeStazione) &&
                Objects.equals(arrNomeStazione, t.arrNomeStazione);
    }
    @Override public int hashCode() {
        return Objects.hash(marca, modello, nomeClasse, depNomeStazione, arrNomeStazione);
    }
    @Override public String toString() {
        return "Tariffa{" + marca + " " + modello + ", " + nomeClasse + ", " +
                depNomeStazione + "→" + arrNomeStazione + ", prezzo=" + prezzo + "}";
    }
}
