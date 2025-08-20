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
    private final String depCitta;
    private final String depProv;
    private final String arrNomeStaz;
    private final String arrCitta;
    private final String arrProv;

    // costruttore completo (già esistente)
    public Treno(String matricola, String marca, String modello,
                 LocalDate dataAcquisto,
                 LocalTime orarioPartenza, LocalTime orarioArrivo,
                 String depNomeStaz, String depCitta, String depProv,
                 String arrNomeStaz, String arrCitta, String arrProv) {
        this.matricola      = matricola;
        this.marca          = marca;
        this.modello        = modello;
        this.dataAcquisto   = dataAcquisto;
        this.orarioPartenza = orarioPartenza;
        this.orarioArrivo   = orarioArrivo;
        this.depNomeStaz    = depNomeStaz;
        this.depCitta       = depCitta;
        this.depProv        = depProv;
        this.arrNomeStaz    = arrNomeStaz;
        this.arrCitta       = arrCitta;
        this.arrProv        = arrProv;
    }

    /** costruttore leggero per elenchi con solo matricola/marca/modello */
    public Treno(String matricola, String marca, String modello) {
        this(matricola, marca, modello,
                null, null, null,
                null, null, null,
                null, null, null);
    }

    public String getMatricola()       { return matricola; }
    public String getMarca()           { return marca; }
    public String getModello()         { return modello; }
    public LocalDate getDataAcquisto() { return dataAcquisto; }
    public LocalTime getOrarioPartenza(){ return orarioPartenza; }
    public LocalTime getOrarioArrivo()  { return orarioArrivo; }
    public String getDepNomeStaz()     { return depNomeStaz; }
    public String getDepCitta()        { return depCitta; }
    public String getDepProv()         { return depProv; }
    public String getArrNomeStaz()     { return arrNomeStaz; }
    public String getArrCitta()        { return arrCitta; }
    public String getArrProv()         { return arrProv; }

    @Override
    public String toString() {
        if (dataAcquisto==null) {
            return String.format("%s – %s %s", matricola, marca, modello);
        }
        return String.format(
                "%s – %s %s (%s → %s)",
                matricola, marca, modello,
                orarioPartenza, orarioArrivo
        );
    }
}
