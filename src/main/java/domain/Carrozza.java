package domain;

import java.util.Objects;

public class Carrozza {
    private final String idTreno;   // CHAR(4)
    private final String marca;     // VARCHAR(30)
    private final String modello;   // VARCHAR(30)
    private final int nCarrozza;    // SMALLINT > 0
    private final String nomeClasse; // ENUM('PRIMA','SECONDA')

    public Carrozza(String idTreno, String marca, String modello, int nCarrozza, String nomeClasse) {
        this.idTreno = idTreno;
        this.marca = marca;
        this.modello = modello;
        this.nCarrozza = nCarrozza;
        this.nomeClasse = nomeClasse;
    }

    public String getIdTreno() { return idTreno; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }
    public int getNCarrozza() { return nCarrozza; }
    public String getNomeClasse() { return nomeClasse; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carrozza)) return false;
        Carrozza c = (Carrozza) o;
        return nCarrozza == c.nCarrozza &&
                Objects.equals(idTreno, c.idTreno) &&
                Objects.equals(marca, c.marca) &&
                Objects.equals(modello, c.modello);
    }
    @Override public int hashCode() {
        return Objects.hash(idTreno, marca, modello, nCarrozza);
    }
    @Override public String toString() {
        return "Carrozza{" + idTreno + "," + marca + "," + modello + ", n=" + nCarrozza + ", classe=" + nomeClasse + "}";
    }
}
