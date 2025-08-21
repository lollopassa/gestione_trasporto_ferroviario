package domain;

import java.util.Objects;

public class Copre {

    private final String depNomeStazione;
    private final String arrNomeStazione;
    private final String matricola;
    private final String marca;
    private final String modello;

    public Copre(String depNomeStazione,
                 String arrNomeStazione,
                 String matricola,
                 String marca,
                 String modello) {
        this.depNomeStazione = depNomeStazione;
        this.arrNomeStazione = arrNomeStazione;
        this.matricola = matricola;
        this.marca = marca;
        this.modello = modello;
    }

    // Getters
    public String getDepNomeStazione() { return depNomeStazione; }
    public String getArrNomeStazione() { return arrNomeStazione; }
    public String getMatricola()       { return matricola; }
    public String getMarca()           { return marca; }
    public String getModello()         { return modello; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Copre)) return false;
        Copre that = (Copre) o;
        return Objects.equals(depNomeStazione, that.depNomeStazione)
                && Objects.equals(arrNomeStazione, that.arrNomeStazione)
                && Objects.equals(matricola, that.matricola)
                && Objects.equals(marca, that.marca)
                && Objects.equals(modello, that.modello);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depNomeStazione, arrNomeStazione, matricola, marca, modello);
    }

    @Override
    public String toString() {
        return "Copre{" +
                "tratta='" + depNomeStazione + " → " + arrNomeStazione + '\'' +
                ", treno='" + matricola + " (" + marca + " " + modello + ")'" +
                '}';
    }
}
