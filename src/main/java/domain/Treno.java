package domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treno {
    private final String matricola;
    private final String marca;
    private final String modello;
    private final LocalDate dataAcquisto;
    private final LocalTime orarioPartenza;
    private final LocalTime orarioArrivo;
    private final String depNomeStaz;
    private final String arrNomeStaz;

    public Treno(String matricola, String marca, String modello,
                 LocalDate dataAcquisto,
                 LocalTime orarioPartenza, LocalTime orarioArrivo,
                 String depNomeStaz, String arrNomeStaz) {
        this.matricola      = matricola;
        this.marca          = marca;
        this.modello        = modello;
        this.dataAcquisto   = dataAcquisto;
        this.orarioPartenza = orarioPartenza;
        this.orarioArrivo   = orarioArrivo;
        this.depNomeStaz    = depNomeStaz;
        this.arrNomeStaz    = arrNomeStaz;
    }

    public Treno(String matricola, String marca, String modello) {
        this(matricola, marca, modello, null, null, null, null, null);
    }

    public String getMatricola()       { return matricola; }
    public String getMarca()           { return marca; }
    public String getModello()         { return modello; }
    public LocalDate getDataAcquisto() { return dataAcquisto; }
    public LocalTime getOrarioPartenza(){ return orarioPartenza; }
    public LocalTime getOrarioArrivo()  { return orarioArrivo; }
    public String getDepNomeStaz()     { return depNomeStaz; }
    public String getArrNomeStaz()     { return arrNomeStaz; }

    @Override
    public String toString() {
        if (dataAcquisto == null) {
            return String.format("%s – %s %s", matricola, marca, modello);
        }
        return String.format(
                "%s – %s %s (%s → %s)",
                matricola, marca, modello,
                orarioPartenza, orarioArrivo
        );
    }
}
