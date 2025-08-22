package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Treno {
    private final String idTreno;             // CHAR(4)
    private final String marca;               // VARCHAR(30)
    private final String modello;             // VARCHAR(30)
    private final LocalDate dataAcquisto;     // DATE
    private final LocalTime orarioPartenza;   // TIME
    private final LocalTime orarioArrivo;     // TIME
    private final String depNomeStaz;         // VARCHAR(50)
    private final String arrNomeStaz;         // VARCHAR(50)

    // Costruttore "light" usato da getAllTreni()
    public Treno(String idTreno, String marca, String modello) {
        this(idTreno, marca, modello, null, null, null, null, null);
    }

    // Costruttore "full"
    public Treno(String idTreno, String marca, String modello,
                 LocalDate dataAcquisto,
                 LocalTime orarioPartenza,
                 LocalTime orarioArrivo,
                 String depNomeStaz,
                 String arrNomeStaz) {
        this.idTreno = idTreno;
        this.marca = marca;
        this.modello = modello;
        this.dataAcquisto = dataAcquisto;
        this.orarioPartenza = orarioPartenza;
        this.orarioArrivo = orarioArrivo;
        this.depNomeStaz = depNomeStaz;
        this.arrNomeStaz = arrNomeStaz;
    }

    public String getIdTreno() { return idTreno; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }
    public LocalDate getDataAcquisto() { return dataAcquisto; }
    public LocalTime getOrarioPartenza() { return orarioPartenza; }
    public LocalTime getOrarioArrivo() { return orarioArrivo; }
    public String getDepNomeStaz() { return depNomeStaz; }
    public String getArrNomeStaz() { return arrNomeStaz; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treno)) return false;
        Treno t = (Treno) o;
        return Objects.equals(idTreno, t.idTreno) &&
                Objects.equals(marca, t.marca) &&
                Objects.equals(modello, t.modello);
    }
    @Override public int hashCode() { return Objects.hash(idTreno, marca, modello); }
    @Override public String toString() {
        return "Treno{" + idTreno + ", " + marca + " " + modello +
                (depNomeStaz != null ? ", " + depNomeStaz + "→" + arrNomeStaz : "") + "}";
    }
}
