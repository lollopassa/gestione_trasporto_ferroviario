package domain;

import java.time.LocalTime;
import java.util.Objects;

public class Tratta {
    private Integer idTratta;
    private Integer numTreniOperativi;
    private String nomePart;
    private String cittaPart;
    private String provPart;
    private String nomeArr;
    private String cittaArr;
    private String provArr;
    private LocalTime orarioPartenza;
    private LocalTime orarioArrivo;


    public Tratta(Integer idTratta, Integer numTreniOperativi, String nomePart, String cittaPart, String provPart, String nomeArr, String cittaArr, String provArr, LocalTime orarioPartenza, LocalTime orarioArrivo) {
        this.idTratta = idTratta;
        this.numTreniOperativi = numTreniOperativi;
        this.nomePart = nomePart;
        this.cittaPart = cittaPart;
        this.provPart = provPart;
        this.nomeArr = nomeArr;
        this.cittaArr = cittaArr;
        this.provArr = provArr;
        this.orarioPartenza = orarioPartenza;
        this.orarioArrivo = orarioArrivo;
    }

    public Integer getIdTratta() { return idTratta; }
    public void setIdTratta(Integer idTratta) { this.idTratta = idTratta; }
    public Integer getNumTreniOperativi() { return numTreniOperativi; }
    public void setNumTreniOperativi(Integer numTreniOperativi) { this.numTreniOperativi = numTreniOperativi; }
    public String getNomePart() { return nomePart; }
    public void setNomePart(String nomePart) { this.nomePart = nomePart; }
    public String getCittaPart() { return cittaPart; }
    public void setCittaPart(String cittaPart) { this.cittaPart = cittaPart; }
    public String getProvPart() { return provPart; }
    public void setProvPart(String provPart) { this.provPart = provPart; }
    public String getNomeArr() { return nomeArr; }
    public void setNomeArr(String nomeArr) { this.nomeArr = nomeArr; }
    public String getCittaArr() { return cittaArr; }
    public void setCittaArr(String cittaArr) { this.cittaArr = cittaArr; }
    public String getProvArr() { return provArr; }
    public void setProvArr(String provArr) { this.provArr = provArr; }
    public LocalTime getOrarioPartenza() { return orarioPartenza; }
    public void setOrarioPartenza(LocalTime orarioPartenza) { this.orarioPartenza = orarioPartenza; }
    public LocalTime getOrarioArrivo() { return orarioArrivo; }
    public void setOrarioArrivo(LocalTime orarioArrivo) { this.orarioArrivo = orarioArrivo; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tratta)) return false;
        Tratta tratta = (Tratta) o;
        return Objects.equals(idTratta, tratta.idTratta);
    }

    @Override public int hashCode() {
        return Objects.hash(idTratta);
    }

    @Override public String toString() {
        return "tratta{" +
                "idTratta=" + idTratta +
                ", numTreniOperativi=" + numTreniOperativi +
                ", nomePart='" + nomePart + '\'' +
                ", cittaPart='" + cittaPart + '\'' +
                ", provPart='" + provPart + '\'' +
                ", nomeArr='" + nomeArr + '\'' +
                ", cittaArr='" + cittaArr + '\'' +
                ", provArr='" + provArr + '\'' +
                ", orarioPartenza=" + orarioPartenza +
                ", orarioArrivo=" + orarioArrivo +
                '}';
    }
}
