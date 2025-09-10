package domain;

import java.util.Objects;

public class Posto {
    private Integer numeroPosto;
    private Integer idComponente;
    private String idTreno;


    public Posto(Integer numeroPosto, Integer idComponente, String idTreno) {
        this.numeroPosto = numeroPosto;
        this.idComponente = idComponente;
        this.idTreno = idTreno;
    }

    public Integer getNumeroPosto() { return numeroPosto; }
    public void setNumeroPosto(Integer numeroPosto) { this.numeroPosto = numeroPosto; }
    public Integer getIdComponente() { return idComponente; }
    public void setIdComponente(Integer idComponente) { this.idComponente = idComponente; }
    public String getIdTreno() { return idTreno; }
    public void setIdTreno(String idTreno) { this.idTreno = idTreno; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posto)) return false;
        Posto posto = (Posto) o;
        return Objects.equals(numeroPosto, posto.numeroPosto) &&
                Objects.equals(idComponente, posto.idComponente) &&
                Objects.equals(idTreno, posto.idTreno);
    }

    @Override public int hashCode() {
        return Objects.hash(numeroPosto, idComponente, idTreno);
    }

    @Override public String toString() {
        return "posto{" +
                "numeroPosto=" + numeroPosto +
                ", idComponente=" + idComponente +
                ", idTreno='" + idTreno + '\'' +
                '}';
    }
}
