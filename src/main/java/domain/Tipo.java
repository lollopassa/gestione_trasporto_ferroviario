package domain;

import java.util.Objects;

public class Tipo {
    private final String marca;
    private final String modello;

    public Tipo(String marca, String modello) {
        this.marca = marca;
        this.modello = modello;
    }

    public String getMarca() { return marca; }
    public String getModello() { return modello; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tipo)) return false;
        Tipo t = (Tipo) o;
        return Objects.equals(marca, t.marca) && Objects.equals(modello, t.modello);
    }
    @Override public int hashCode() { return Objects.hash(marca, modello); }
    @Override public String toString() { return marca + " " + modello; }
}
