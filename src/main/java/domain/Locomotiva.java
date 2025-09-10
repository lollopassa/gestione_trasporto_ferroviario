package domain;

import java.time.LocalDate;
import java.util.Objects;

public class Locomotiva {
    private Integer idComponente;
    private String idTreno;
    private String marca;
    private String modello;
    private LocalDate dataAcquisto;
    private String manutenzione;


    public Locomotiva(Integer idComponente, String idTreno, String marca, String modello, LocalDate dataAcquisto, String manutenzione) {
        this.idComponente = idComponente;
        this.idTreno = idTreno;
        this.marca = marca;
        this.modello = modello;
        this.dataAcquisto = dataAcquisto;
        this.manutenzione = manutenzione;
    }

    public Integer getIdComponente() { return idComponente; }
    public void setIdComponente(Integer idComponente) { this.idComponente = idComponente; }
    public String getIdTreno() { return idTreno; }
    public void setIdTreno(String idTreno) { this.idTreno = idTreno; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }
    public LocalDate getDataAcquisto() { return dataAcquisto; }
    public void setDataAcquisto(LocalDate dataAcquisto) { this.dataAcquisto = dataAcquisto; }
    public String getManutenzione() { return manutenzione; }
    public void setManutenzione(String manutenzione) { this.manutenzione = manutenzione; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locomotiva)) return false;
        Locomotiva that = (Locomotiva) o;
        return Objects.equals(idComponente, that.idComponente) &&
                Objects.equals(idTreno, that.idTreno);
    }

    @Override public int hashCode() {
        return Objects.hash(idComponente, idTreno);
    }

    @Override public String toString() {
        return "locomotiva{" +
                "idComponente=" + idComponente +
                ", idTreno='" + idTreno + '\'' +
                ", marca='" + marca + '\'' +
                ", modello='" + modello + '\'' +
                ", dataAcquisto=" + dataAcquisto +
                ", manutenzione='" + manutenzione + '\'' +
                '}';
    }
}
