package domain;

import java.time.LocalTime;
import java.util.Objects;

public class Fermata {
    private Integer idTratta;
    private String nome;
    private String citta;
    private String provincia;
    private Integer progressivo;
    private LocalTime orarioArrPrev;
    private LocalTime orarioPartPrev;


    public Fermata(Integer idTratta, String nome, String citta, String provincia, Integer progressivo, LocalTime orarioArrPrev, LocalTime orarioPartPrev) {
        this.idTratta = idTratta;
        this.nome = nome;
        this.citta = citta;
        this.provincia = provincia;
        this.progressivo = progressivo;
        this.orarioArrPrev = orarioArrPrev;
        this.orarioPartPrev = orarioPartPrev;
    }

    public Integer getIdTratta() { return idTratta; }
    public void setIdTratta(Integer idTratta) { this.idTratta = idTratta; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public Integer getProgressivo() { return progressivo; }
    public void setProgressivo(Integer progressivo) { this.progressivo = progressivo; }
    public LocalTime getOrarioArrPrev() { return orarioArrPrev; }
    public void setOrarioArrPrev(LocalTime orarioArrPrev) { this.orarioArrPrev = orarioArrPrev; }
    public LocalTime getOrarioPartPrev() { return orarioPartPrev; }
    public void setOrarioPartPrev(LocalTime orarioPartPrev) { this.orarioPartPrev = orarioPartPrev; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fermata)) return false;
        Fermata that = (Fermata) o;
        return Objects.equals(idTratta, that.idTratta) &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(citta, that.citta) &&
                Objects.equals(provincia, that.provincia);
    }

    @Override public int hashCode() {
        return Objects.hash(idTratta, nome, citta, provincia);
    }

    @Override public String toString() {
        return "fermata{" +
                "idTratta=" + idTratta +
                ", nome='" + nome + '\'' +
                ", citta='" + citta + '\'' +
                ", provincia='" + provincia + '\'' +
                ", progressivo=" + progressivo +
                ", orarioArrPrev=" + orarioArrPrev +
                ", orarioPartPrev=" + orarioPartPrev +
                '}';
    }
}
