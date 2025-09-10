package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Turno {
    private String cf;
    private LocalDate dataServizio;
    private String idTreno;
    private LocalTime oraInizio;
    private LocalTime oraFine;


    public Turno(String cf, LocalDate dataServizio, String idTreno, LocalTime oraInizio, LocalTime oraFine) {
        this.cf = cf;
        this.dataServizio = dataServizio;
        this.idTreno = idTreno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    public String getCf() { return cf; }
    public void setCf(String cf) { this.cf = cf; }
    public LocalDate getDataServizio() { return dataServizio; }
    public void setDataServizio(LocalDate dataServizio) { this.dataServizio = dataServizio; }
    public String getIdTreno() { return idTreno; }
    public void setIdTreno(String idTreno) { this.idTreno = idTreno; }
    public LocalTime getOraInizio() { return oraInizio; }
    public void setOraInizio(LocalTime oraInizio) { this.oraInizio = oraInizio; }
    public LocalTime getOraFine() { return oraFine; }
    public void setOraFine(LocalTime oraFine) { this.oraFine = oraFine; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;
        Turno turno = (Turno) o;
        return Objects.equals(cf, turno.cf) &&
                Objects.equals(dataServizio, turno.dataServizio);
    }

    @Override public int hashCode() {
        return Objects.hash(cf, dataServizio);
    }

    @Override public String toString() {
        return "turno{" +
                "cf='" + cf + '\'' +
                ", dataServizio=" + dataServizio +
                ", idTreno='" + idTreno + '\'' +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                '}';
    }
}
