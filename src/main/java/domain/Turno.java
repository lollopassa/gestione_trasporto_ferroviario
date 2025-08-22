package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Turno {
    private final String cf;        // CHAR(16)
    private final LocalDate dataServ;
    private final LocalTime oraInizio;
    private final LocalTime oraFine;
    private final String idTreno;   // CHAR(4)
    private final String marca;
    private final String modello;

    public Turno(String cf, LocalDate dataServ, LocalTime oraInizio, LocalTime oraFine,
                 String idTreno, String marca, String modello) {
        this.cf = cf;
        this.dataServ = dataServ;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.idTreno = idTreno;
        this.marca = marca;
        this.modello = modello;
    }

    public String getCf() { return cf; }
    public LocalDate getDataServ() { return dataServ; }
    public LocalTime getOraInizio() { return oraInizio; }
    public LocalTime getOraFine() { return oraFine; }
    public String getIdTreno() { return idTreno; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;
        Turno t = (Turno) o;
        return Objects.equals(cf, t.cf) &&
                Objects.equals(idTreno, t.idTreno) &&
                Objects.equals(marca, t.marca) &&
                Objects.equals(modello, t.modello) &&
                Objects.equals(dataServ, t.dataServ) &&
                Objects.equals(oraInizio, t.oraInizio);
    }
    @Override public int hashCode() { return Objects.hash(cf, idTreno, marca, modello, dataServ, oraInizio); }
    @Override public String toString() {
        return "Turno{" + cf + ", " + dataServ + " " + oraInizio + "-" + oraFine +
                ", treno=" + idTreno + " " + marca + " " + modello + "}";
    }
}
