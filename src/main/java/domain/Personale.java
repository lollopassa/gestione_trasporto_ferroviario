package domain;

import java.util.Objects;

public class Personale {

    public enum TipoPersonale { MACCHINISTA, CAPOTRENO }

    private final String cf;           // CHAR(16)
    private final TipoPersonale tipo;  // ENUM
    private final String username;     // VARCHAR(20)
    private final String idTreno;      // CHAR(4)
    private final String marca;        // VARCHAR(30)
    private final String modello;      // VARCHAR(30)

    public Personale(String cf, TipoPersonale tipo, String username,
                     String idTreno, String marca, String modello) {
        this.cf = cf;
        this.tipo = tipo;
        this.username = username;
        this.idTreno = idTreno;
        this.marca = marca;
        this.modello = modello;
    }

    public String getCf() { return cf; }
    public TipoPersonale getTipo() { return tipo; }
    public String getUsername() { return username; }
    public String getIdTreno() { return idTreno; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personale)) return false;
        Personale p = (Personale) o;
        return Objects.equals(cf, p.cf) &&
                Objects.equals(idTreno, p.idTreno) &&
                Objects.equals(marca, p.marca) &&
                Objects.equals(modello, p.modello);
    }
    @Override public int hashCode() { return Objects.hash(cf, idTreno, marca, modello); }
    @Override public String toString() {
        return "Personale{cf='" + cf + "', tipo=" + tipo + ", user='" + username + "', treno=" +
                idTreno + " " + marca + " " + modello + "}";
    }
}
